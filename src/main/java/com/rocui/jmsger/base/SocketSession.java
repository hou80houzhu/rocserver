package com.rocui.jmsger.base;

import com.rocui.jmsger.base.SocketSession.SessionEach.EachResult;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author hou80houzhu
 */
public class SocketSession {

    private HashMap<String, Object> session = new HashMap<String, Object>();

    public void setAttribute(String key, Object value) {
        this.session.put(key, value);
    }

    public Object getAttribute(String key) {
        return this.session.get(key);
    }

    public Object each(SessionEach each) {
        HashMap<String, Object> map = this.session;
        Object data = null;
        for (Entry<String, Object> x : map.entrySet()) {
            EachResult q = each.each(x.getKey(), x.getValue());
            data = q.data;
            if (q.isbreak == false) {
                break;
            }
        }
        return data;
    }

    public abstract class SessionEach {

        public abstract EachResult each(String key, Object value);

        public class EachResult {

            private Boolean isbreak = false;
            private Object data = null;

            public EachResult() {
            }

            public EachResult(Boolean isBreak, Object data) {
                this.isbreak = isBreak;
                this.data = data;
            }

            public Boolean getIsbreak() {
                return isbreak;
            }

            public void setIsbreak(Boolean isbreak) {
                this.isbreak = isbreak;
            }

            public Object getData() {
                return data;
            }

            public void setData(Object data) {
                this.data = data;
            }

        }
    }
}
