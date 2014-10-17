package com.rocui.comet;

import com.rocui.boot.IBootService;
import com.rocui.comet.base.Comet;
import com.rocui.util.jsonx.Jsonx;

public class CometBoot implements IBootService {

    public void serviceStart(Jsonx option) {
        Comet.shutup(option);
    }

    public void serviceStop() {
        Comet.shutDown();
    }

}
