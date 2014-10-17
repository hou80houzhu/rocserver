package com.rocui.jmail;

import com.rocui.boot.IBootService;
import com.rocui.jmail.data.JmailConfig;
import com.rocui.jmail.page.JmailPage;
import com.rocui.util.jsonx.Jsonx;
import com.rocui.util.worker.JworkerContainer;

public class JmailBoot implements IBootService {

    @Override
    public void serviceStart(Jsonx option) {
        try {
            JmailConfig.init(option);
            JmailPage.init(option);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void serviceStop() {
        JworkerContainer.getContainer().getSinglePool("xxmailonly").stop();
    }

}
