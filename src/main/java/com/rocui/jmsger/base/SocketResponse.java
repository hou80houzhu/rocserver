package com.rocui.jmsger.base;

import com.rocui.jmsger.base.result.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hou80houzhu
 */
public class SocketResponse {

    private List<ResultSet> resultlist = new ArrayList<ResultSet>();

    public SocketResponse addResult(ResultSet result) {
        this.resultlist.add(result);
        return this;
    }

    public List<ResultSet> getResults() {
        return this.resultlist;
    }

    public SocketResponse removeAllResult() {
        this.resultlist = new ArrayList<ResultSet>();
        return this;
    }
}
