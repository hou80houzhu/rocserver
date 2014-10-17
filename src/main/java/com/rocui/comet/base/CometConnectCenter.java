package com.rocui.comet.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CometConnectCenter {

    private static CometConnectCenter center = null;

    private HashMap<String, CometConnect> connects;
    private ScheduledExecutorService schedule;
    private ExecutorService workers;

    public static CometConnectCenter getCenter() {
        if (CometConnectCenter.center == null) {
            CometConnectCenter.center = new CometConnectCenter();
        }
        return CometConnectCenter.center;
    }

    private CometConnectCenter() {
        this.connects = new HashMap<String, CometConnect>();
        this.schedule = Executors.newScheduledThreadPool(1);
        this.schedule.schedule(new CometPolice(), 5, TimeUnit.MINUTES);
        this.workers = Executors.newCachedThreadPool();
    }

    protected String createConnect() {
        String id = System.currentTimeMillis() + "";
        CometConnect con = new CometConnect(id);
        con.center = this;
        this.connects.put(id, con);
        return id;
    }

    protected void closeConnect(String id) {
        if (this.connects.containsKey(id)) {
            this.connects.remove(id);
        }
    }

    public CometConnect getConnect(String id) {
        if (this.connects.containsKey(id)) {
            return this.connects.get(id);
        } else {
            return null;
        }
    }

    public HashMap<String, CometConnect> getConnects() {
        return this.connects;
    }

    public CometResultSet read(String id) throws Exception {
        if (this.connects.containsKey(id)) {
            return this.connects.get(id).read();
        } else {
            return null;
        }
    }

    public void broadCast(CometResultSet mes) throws Exception {
        for (Entry<String, CometConnect> c : this.connects.entrySet()) {
            c.getValue().write(mes);
        }
    }

    public void sendTo(String id, CometResultSet mes) throws Exception {
        if (this.connects.containsKey(id)) {
            this.connects.get(id).write(mes);
        }
    }

    public void sendAll(List<String> ids, CometResultSet ms) throws Exception {
        for (String x : ids) {
            this.sendTo(x, ms);
        }
    }

    protected void asySend(final CometResultSet mes) {
        this.workers.submit(new Runnable() {
            public void run() {
                List<String> tos = mes.to();
                try {
                    CometConnectCenter.getCenter().sendAll(tos, mes);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    protected void asyBroadCast(final CometResultSet mes) {
        this.workers.submit(new Runnable() {
            public void run() {
                List<String> tos = mes.to();
                try {
                    CometConnectCenter.getCenter().broadCast(mes);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    protected void shutDown() {
        if (this.schedule != null) {
            this.schedule.shutdownNow();
        }
        if (this.workers != null) {
            this.workers.shutdownNow();
        }
    }

    public List<String> getIdsByName(String name) {
        List<String> list = new ArrayList<String>();
        for (Entry<String, CometConnect> x : this.connects.entrySet()) {
            if (x.getValue().name.equals(name)) {
                list.add(x.getValue().id());
            }
        }
        return list;
    }

    public class CometConnect {

        public static final String CONNECT_STATE_ALIVE = "alive";
        public static final String CONNECT_STATE_WAIT = "wait";
        public static final String CONNECT_STATE_QUIT = "quit";
        private final String id;
        private final String state;
        private final LinkedBlockingQueue<CometResultSet> queue;
        private final CometSession session;
        private int wait = 0;

        private CometConnectCenter center;

        private String name = "";

        private CometConnect(String id) {
            this.id = id;
            this.session = new CometSession();
            this.state = CometConnect.CONNECT_STATE_ALIVE;
            this.queue = new LinkedBlockingQueue<CometResultSet>();
        }

        private void write(CometResultSet mes) throws Exception {
            mes.time(System.currentTimeMillis());
            this.queue.offer(mes, 10, TimeUnit.SECONDS);
        }

        private CometResultSet read() throws Exception {
            return this.queue.poll(20, TimeUnit.SECONDS);
        }

        public String id() {
            return this.id;
        }

        public CometSession getSession() {
            return this.session;
        }

        public int waitTime() {
            return this.wait;
        }

        public String name() {
            return this.name;
        }

        public void name(String name) {
            this.name = name;
        }

        public CometConnectCenter center() {
            return this.center;
        }

        public List<CometResultSet> message() {
            List<CometResultSet> list = new ArrayList<CometResultSet>();
            LinkedBlockingQueue<CometResultSet> queuex = this.queue;
            for (CometResultSet cometResultSet : queuex) {
                list.add(cometResultSet);
            }
            return list;
        }
    }

    public class CometPolice implements Runnable {

        public void run() {
            System.out.println("I am police,I am running...");
        }
    }
}
