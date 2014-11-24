package com.rocui.polling.base;

import com.rocui.polling.base.ConnectManager.PollingConnect;
import com.rocui.polling.base.message.MessageWrapper;
import com.rocui.polling.base.message.PollingMessage;
import com.rocui.polling.base.event.BaseEventHandler;
import com.rocui.polling.base.event.LogEventHandler;
import com.rocui.polling.router.BaseRouter;
import com.rocui.polling.router.PollingRouterManager;
import com.rocui.polling.service.BaseService;
import com.rocui.polling.service.PollingServiceManager;
import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.jsonx.Jsonx;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Polling {

    private static Polling polling;
    private final ConnectManager manager;
    private final PollingServiceManager serviceManager;
    private final PollingRouterManager routerManager;
    private final ScheduledExecutorService schedule;
    private BaseEventHandler event;

    private Polling(Jsonx option) throws Exception {
        this.manager = ConnectManager.getManager();
        this.serviceManager = PollingServiceManager.init(option.get("servicePackage").toString());
        this.routerManager = PollingRouterManager.init(option.get("routerPackage").toString());
        this.schedule = Executors.newScheduledThreadPool(1);
        this.schedule.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("=rubish connect cleanning=");
                List<PollingConnect> list = manager.getTimeoutConnects();
                for (PollingConnect connect : list) {
                    PollingResponse pollingresponse = new PollingResponse();
                    manager.close(connect.getId());
                    event.onClose(connect, pollingresponse);
                    try {
                        rout(connect, pollingresponse);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }, 1, 5, TimeUnit.SECONDS);
        this.event = new LogEventHandler();
        String n = option.get("eventHandler").toString();
        if (null != n && !n.equals("")) {
            Object a = ObjectSnooper.snoop(n).object();
            if (a instanceof BaseEventHandler) {
                this.event = (BaseEventHandler) a;
            }
        }
    }

    public static Polling init(Jsonx option) throws Exception {
        if (null == polling) {
            polling = new Polling(option);
        }
        return polling;
    }

    public static Polling getPolling() {
        return polling;
    }

    public void asReadChannel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String connectId = request.getParameter("id");
        PollingConnect connect = manager.get(connectId);
        if (null != connect) {
            PollingMessage message = connect.read();
            if (null == message) {
                message = new PollingMessage();
                message.setCode("1");
                message.setContent("nodata");
            }
            back(response, message);
        } else {
            PollingMessage message = new PollingMessage();
            message.setCode("1");
            message.setContent("offline");
            back(response, message);
        }
    }

    public void asWriteChannel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String serviceName = request.getParameter("service");
        String connectId = request.getParameter("id");
        PollingConnect cot;
        PollingResponse pollingresponse = new PollingResponse();
        switch (serviceName) {
            case "HANDSHAKE":
                cot = manager.create();
                this.event.onConnect(cot, request, pollingresponse);
                rout(cot, pollingresponse);
                back(response, "{\"code\":\"1\",\"data\":\"" + cot.getId() + "\"}");
                break;
            case "QUIT":
                cot = manager.get(connectId);
                if (null != cot) {
                    manager.close(connectId);
                    this.event.onClose(cot, pollingresponse);
                    rout(cot, pollingresponse);
                    back(response, "{\"code\":\"1\"}");
                } else {
                    back(response, "{\"code\":\"1\",\"callback\":\"tohandshake\"}");
                }
                break;
            default:
                String content = request.getParameter("content");
                cot = manager.get(connectId);
                if (null != cot) {
                    PollingRequest pollingrequest = new PollingRequest(serviceName, content, cot);
                    BaseService service = serviceManager.getService(serviceName);
                    if (null != service) {
                        ObjectSnooper.snoop(service).sett("request", request);
                        service.excute(pollingrequest, pollingresponse);
                        rout(cot, pollingresponse);
                    }
                    back(response, "{\"code\":\"1\"}");
                } else {
                    back(response, "{\"code\":\"1\",\"callback\":\"tohandshake\"}");
                }
                break;
        }
    }

    private void back(HttpServletResponse response, PollingMessage message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(Jsonx.create(message).toString());
        response.getWriter().close();
    }

    private void back(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
        response.getWriter().close();
    }

    public void rout(PollingConnect connect, PollingResponse response) throws Exception {
        List<MessageWrapper> carries = response.getMessageWrappers();
        for (MessageWrapper carrier : carries) {
            String type = carrier.getRouterType();
            BaseRouter router = this.routerManager.getRouter(type);
            if (null != router) {
                router.rout(connect, carrier);
            }
        }
    }

    public static void post(PollingResponse response) throws Exception {
        PollingConnect connect = ConnectManager.getManager().getFirstConnect();
        if (null != connect) {
            Polling.getPolling().rout(connect, response);
        }
    }

    public void stop() {
        if (this.schedule != null) {
            this.schedule.shutdownNow();
        }
    }

}
