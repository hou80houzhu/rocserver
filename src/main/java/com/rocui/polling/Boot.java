package com.rocui.polling;

import com.rocui.boot.IBootService;
import com.rocui.polling.base.Polling;
import com.rocui.util.jsonx.Jsonx;

public class Boot implements IBootService {

    @Override
    public void serviceStart(Jsonx option) {
        try {
            Polling.init(option);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void serviceStop() {
        Polling.getPolling().stop();
    }

}
