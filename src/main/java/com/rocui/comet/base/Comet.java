package com.rocui.comet.base;

import com.rocui.comet.base.CometConnectCenter.CometConnect;
import com.rocui.comet.base.handler.CometBaseHandler;
import com.rocui.comet.service.CometBaseService;
import com.rocui.comet.service.CometServiceManager;
import com.rocui.comet.service.controller.CometControllerManager;
import com.rocui.util.jsonx.Jsonx;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Comet {

    private static Comet comet = null;
    private CometBaseHandler handler;

    public static Comet getComet() {
        return Comet.comet;
    }

    public Comet(Jsonx json) {
        try {
            String servicePackage = json.get("servicePackage").toString();
            String controllerPackage = json.get("controllerPackage").toString();
            String handlername = json.get("eventHandler").toString();
            CometServiceManager.init(servicePackage);
            CometControllerManager.init(controllerPackage);
            Object t = Class.forName(handlername).newInstance();
            if (t instanceof CometBaseHandler) {
                this.handler = (CometBaseHandler) t;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void shutup(Jsonx json) {
        Comet.comet = new Comet(json);
    }

    public static void shutDown() {
        CometConnectCenter.getCenter().shutDown();
    }

    public void asReadChannel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        if (id != null && id.trim().length() > 0) {
            CometConnectCenter center = CometConnectCenter.getCenter();
            try {
                CometResultSet result = center.read(id);
                if (this.handler != null) {
                    result = this.handler.onBeforeSendMessage(result, center.getConnect(id));
                }
                response.getWriter().write(Jsonx.create(result.getInfo()).toString());
            } catch (Exception ex) {
                CometConnect con = center.getConnect(id);
                if (con != null) {
                    int time = center.getConnect(id).waitTime();
                    response.getWriter().write(Jsonx.create("{\"code\":1,\"data\":\"NODATA\",\"type\":\"" + CometBaseService.SERVICE_NODATA + "\",\"wait\":" + time + "}").toString());
                } else {
                    response.getWriter().write(Jsonx.create("{\"code\":1,\"data\":\"OFFLINE\",\"type\":\"" + CometBaseService.SERVICE_OFFLINE + "\"}").toString());
                }
            }
        } else {
            response.getWriter().write(Jsonx.create("{\"code\":1,\"data\":\"OFFLINE\",\"type\":\"" + CometBaseService.SERVICE_OFFLINE + "\"}").toString());
        }
        response.getWriter().close();
    }

    public void asWriteChannel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        String paras = request.getParameter("parameter");

        CometRequest rquest = new CometRequest();
        rquest.setId(id);
        rquest.setParameter(paras);
        rquest.setType(type);

        if (type != null && type.trim().length() > 0) {
            try {
                CometConnectCenter center = CometConnectCenter.getCenter();
                if (type.equals("HANDSHAKE")) {
                    String idis = center.createConnect();
                    if (handler != null) {
                        this.handler.onConnect(idis);
                    }
                    response.getWriter().write("{\"code\":1,\"data\":" + idis + "}");
                } else if (type.equals("QUIT")) {
                    CometConnect conn = center.getConnect(id);
                    if (handler != null) {
                        this.handler.onClose(conn);
                    }
                    center.closeConnect(id);
                    response.getWriter().write("{\"code\":1}");
                } else {
                    rquest.setSession(center.getConnect(id).getSession());
                    rquest.setRequest(request);
                    CometBaseService service = CometServiceManager.getManager().getService(type);
                    if (service != null) {
                        service.setRequest(rquest);
                        service.setReq(request);
                        service.setConnect(center.getConnect(id));
                        CometResponse rep = service.excute();
                        List<CometResultSet> results = rep.get();
                        HashMap<String, Object> info = null;
                        for (CometResultSet set : results) {
                            List<String> tos = set.to();
                            for (String to : tos) {
                                if (to.equals(CometRouter.ROUT_BRAODCAST)) {
                                    center.broadCast(set);
                                } else if (to.equals(CometRouter.ROUT_SELF)) {
                                    info = set.getInfo();
                                } else {
                                    center.sendTo(to, set);
                                }
                            }
                        }
                        if (info != null) {
                            response.getWriter().write(Jsonx.create(info).toString());
                        } else {
                            response.getWriter().write("{\"code\":1}");
                        }
                    } else {
                        response.getWriter().write("{\"code\":0,\"msg\":\"has no this service\"}");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                response.getWriter().write("{\"code\":0}");
            } finally {
                response.getWriter().close();
            }
        }
    }

    public class CometRouter {

        public static final String ROUT_BRAODCAST = "broadcast";
        public static final String ROUT_SELF = "self";
        public static final String ROUT_OFF_SELF = "offself";

    }
}
