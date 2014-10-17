package com.rocui.comet.base;

import java.util.ArrayList;
import java.util.List;

public class CometResponse {
    
    private List<CometResultSet> results;
    
    public CometResponse() {
        this.results = new ArrayList<CometResultSet>();
    }
    
    public CometResponse add(CometResultSet result) {
        this.results.add(result);
        return this;
    }
    
    public CometResponse add(Object mes, List<String> toIds, String type) {
        this.results.add(new CometResultSet(toIds, mes, type));
        return this;
    }
    
    public CometResponse add(Object mes, String to, String type) {
        this.results.add(new CometResultSet(to, mes, type));
        return this;
    }
    
    public CometResponse add(Object mes, String to, String code, String type) {
        CometResultSet set = new CometResultSet();
        set.code(code).data(mes).to(to).type(type);
        this.results.add(set);
        return this;
    }
    
    public List<CometResultSet> get() {
        return this.results;
    }
}
