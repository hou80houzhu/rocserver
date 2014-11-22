package com.rocui.polling.base;

import com.rocui.polling.base.message.PollingMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectManager {

    private static ConnectManager manager;
    private final HashMap<String, PollingConnect> mapping;
    private Object ids;

    private ConnectManager() {
        this.mapping = new HashMap<>();
    }

    public static ConnectManager getManager() {
        if (null == manager) {
            manager = new ConnectManager();
        }
        return manager;
    }

    public PollingConnect create() {
        String id = UUID.randomUUID().toString().replace("-", "");
        PollingConnect connection = new PollingConnect(id);
        connection.manager = this;
        this.mapping.put(id, connection);
        return connection;
    }

    public PollingConnect get(String id) {
        return this.mapping.get(id);
    }

    public void postMessage(String id, PollingMessage message) throws Exception {
        PollingConnect connect = this.get(id);
        if (null != connect) {
            connect.write(message);
        }
    }

    public void broadCast(PollingMessage message) throws Exception {
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            polls.getValue().write(message);
        }
    }

    public void broadCastWithOutId(String id, PollingMessage message) throws Exception {
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (!polls.getKey().equals(id)) {
                polls.getValue().write(message);
            }
        }
    }

    public void broadCastWithOutGroupId(String groupId, PollingMessage message) throws Exception {
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (!polls.getValue().getGroupId().equals(groupId)) {
                polls.getValue().write(message);
            }
        }
    }

    public void postGroupMessage(String groupId, PollingMessage message) throws Exception {
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (polls.getValue().getGroupId().equals(groupId)) {
                polls.getValue().write(message);
            }
        }
    }

    public void postSomeMessage(List<String> ids, PollingMessage message) throws Exception {
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (ids.contains(polls.getValue().getId())) {
                polls.getValue().write(message);
            }
        }
    }

    public void postSomeGroupMessage(List<String> groupids, PollingMessage message) throws Exception {
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (groupids.contains(polls.getValue().getGroupId())) {
                polls.getValue().write(message);
            }
        }
    }

    public void postFilterMessage(ConnectFilter filter, PollingMessage message) throws Exception {
        int i = 0;
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            boolean r = filter.filter(polls.getValue(), i);
            if (r) {
                polls.getValue().write(message);
            }
            i++;
        }
    }

    public List<PollingConnect> getConnectsByGroupId(String groupId) {
        List<PollingConnect> list = new ArrayList<>();
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (polls.getValue().getGroupId().equals(groupId)) {
                list.add(polls.getValue());
            }
        }
        return list;
    }

    public List<PollingConnect> getTimeoutConnects() {
        List<PollingConnect> list = new ArrayList<>();
        Long a = System.currentTimeMillis();
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (a - polls.getValue().getTime() > 1000 * 30) {
                list.add(polls.getValue());
            }
        }
        return list;
    }

    public List<PollingConnect> getAllConnects() {
        List<PollingConnect> list = new ArrayList<>();
        Long a = System.currentTimeMillis();
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            list.add(polls.getValue());
        }
        return list;
    }

    public List<PollingConnect> getGroupsOnline() {
        List<PollingConnect> list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            PollingConnect connect = polls.getValue();
            String id = connect.groupId;
            if (null != id && !id.equals("")) {
                if (!map.containsKey(id)) {
                    list.add(connect);
                    map.put(id, "");
                }
            }
        }
        return list;
    }

    public List<String> filterConnectsId(ConnectEach each) {
        List<String> list = new ArrayList<>();
        int i = 0;
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            boolean r = each.each(polls.getValue(), i, mapping);
            if (r) {
                list.add(polls.getValue().getId());
            }
            i++;
        }
        return list;
    }

    public List<String> filterConnectsGroupId(ConnectEach each) {
        List<String> list = new ArrayList<>();
        int i = 0;
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            boolean r = each.each(polls.getValue(), i, mapping);
            if (r) {
                list.add(polls.getValue().getGroupId());
            }
            i++;
        }
        return list;
    }
    
    public void connectEach(ConnectEach each) {
        int i = 0;
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            boolean r = each.each(polls.getValue(), i, mapping);
            if (r) {
                break;
            }
            i++;
        }
    }

    public void close(String connectId) {
        this.mapping.remove(connectId);
    }

    public void closeGroup(String groupId) {
        for (Entry<String, PollingConnect> polls : this.mapping.entrySet()) {
            if (polls.getValue().getGroupId().equals(groupId)) {
                this.mapping.remove(polls.getKey());
            }
        }
    }

    public class PollingConnect {

        private final LinkedBlockingQueue<PollingMessage> queue;
        private final String id;
        private final PollingSession session;
        private String groupId;
        private Long time;
        private ConnectManager manager;

        public PollingConnect(String id) {
            this.id = id;
            this.session = new PollingSession();
            this.queue = new LinkedBlockingQueue<>();
            this.time = System.currentTimeMillis();
        }

        public void write(PollingMessage mes) throws Exception {
            this.queue.offer(mes, 10, TimeUnit.SECONDS);
        }

        public PollingMessage read() throws Exception {
            this.time = System.currentTimeMillis();
            return this.queue.poll(20, TimeUnit.SECONDS);
        }

        public String getId() {
            return id;
        }

        public PollingSession getSession() {
            return session;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public Long getTime() {
            return this.time;
        }

        public ConnectManager getConnectManager() {
            return this.manager;
        }
    }
}
