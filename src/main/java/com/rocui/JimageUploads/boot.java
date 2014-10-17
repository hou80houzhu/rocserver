package com.rocui.JimageUploads;

import com.rocui.boot.IBootService;
import com.rocui.util.jsonx.Jsonx;
import java.util.logging.Level;
import java.util.logging.Logger;

public class boot implements IBootService {

    public void serviceStart(Jsonx option) {
        try {
            Jgallery.init(option);
        } catch (Exception ex) {
            Logger.getLogger(boot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void serviceStop() {
    }

}
