package com.rocui.boot;

import com.rocui.util.jsonx.Jsonx;

public interface IBootService {

    public void serviceStart(Jsonx option);

    public void serviceStop();
}