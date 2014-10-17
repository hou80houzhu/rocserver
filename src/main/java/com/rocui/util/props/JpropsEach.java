package com.rocui.util.props;

public abstract class JpropsEach {

    protected Object[] arguments;

    public JpropsEach(Object... args) {
        this.arguments = args;
    }

    public abstract boolean each(String key, String value);
}
