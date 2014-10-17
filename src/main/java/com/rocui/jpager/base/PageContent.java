package com.rocui.jpager.base;

import java.util.HashMap;
import java.util.List;

public class PageContent {

    private int total;
    private List<HashMap<String, Object>> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HashMap<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<HashMap<String, Object>> rows) {
        this.rows = rows;
    }

}
