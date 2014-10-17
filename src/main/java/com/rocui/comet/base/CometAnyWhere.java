package com.rocui.comet.base;

public class CometAnyWhere {

    public static void send(CometResultSet mes) {
        CometConnectCenter.getCenter().asySend(mes);
    }

    public static void broadCast(CometResultSet mes) {
        CometConnectCenter.getCenter().asyBroadCast(mes);
    }
}
