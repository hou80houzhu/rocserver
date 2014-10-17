package com.rocui.mvc;

import java.util.Stack;

public class SessionState {

    private Stack<ConnectionManager> stack = new Stack<ConnectionManager>();

    public ConnectionManager getConnectionManager() {
        return this.stack.peek();
    }

    public void push() {
        this.stack.push(new ConnectionManager());
    }

    public void pop() {
        this.stack.pop();
    }
    
    public int length(){
        return this.stack.size();
    }
    
    public void clear(){
        this.stack.clear();
    }
}
