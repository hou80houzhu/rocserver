package com.rocui.util.jsonx;

public abstract class JsonFullEach {

    protected Object[] arguments;

    public JsonFullEach(Object... args) {
        this.arguments = args;
    }

    public abstract void each(Jsonx json) throws Exception;
}
