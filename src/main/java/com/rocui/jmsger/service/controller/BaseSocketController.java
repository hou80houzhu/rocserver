/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rocui.jmsger.service.controller;

import com.rocui.jmsger.base.SocketRequest;
import com.rocui.jmsger.base.SocketResponse;


/**
 *
 * @author hou80houzhu
 */
public abstract class BaseSocketController {
    protected SocketRequest request;
    protected SocketResponse response;
    public void doAction(){}

    private void setRequest(SocketRequest request) {
        this.request = request;
    }

    public void setResponse(SocketResponse response) {
        this.response = response;
    }
    
}
