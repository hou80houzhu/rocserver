package com.rocui.util.jxml.base;

import com.rocui.util.jxml.Jxml;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Jquery {

    private List<Jnode> list = new ArrayList<Jnode>();
    private Jnode node;

    public static Jquery createTag(String tagName) {
        Jnode nodex = new Jnode(tagName);
        nodex.root = nodex;
        return new Jquery(nodex);
    }

    public static Jquery createSingleTag(String tagName) {
        Jnode node = new Jnode(tagName);
        node.isSingleTag = true;
        node.root = node;
        return new Jquery(node);
    }

    public Jquery(Jnode node) {
        this.node = node;
        this.list.add(node);
    }

    public Jquery(List<Jnode> nodes) {
        if (nodes.size() > 0) {
            this.node = nodes.get(0);
        }
        this.list = nodes;
    }

    public Jquery find(String tagName) {
        return new Jquery(node.getElementsByTagName(tagName));
    }

    public Jquery eq(int index) {
        if (index >= 0 && index < this.list.size()) {
            return new Jquery(this.list.get(index));
        } else {
            return this;
        }
    }

    public Jquery addNameSpace(String key, String value) {
        this.node.addNameSpace(key, value);
        return this;
    }

    public Jnode get(int index) {
        if (index >= 0 && index < this.list.size()) {
            return this.list.get(index);
        } else {
            return null;
        }
    }

    public Jquery xml(String xmlstr) {
        try {
            this.node.setInnerXML(xmlstr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Jquery children() {
        return new Jquery(this.node.getChildren());
    }

    public String attribute(String key) {
        return this.node.getAttributeByName(key);
    }

    public Jquery attribute(String key, String value) {
        this.node.setAttribute(key, value);
        return this;
    }

    public Jquery text(String text) {
        this.node.setText(text);
        return this;
    }

    public String text() {
        return this.node.getText();
    }

    public String subText() {
        return this.node.getSubText();
    }

    public Jquery cdata(String content) {
        this.node.setCdata(content);
        return this;
    }

    public Jquery appendCdata(String content) {
        this.node.appendCdata(content);
        return this;
    }

    public String xml() {
        return this.node.getXML();
    }

    public int length() {
        return this.list.size();
    }

    public Jquery parent() {
        return new Jquery(this.node.getParentNode());
    }

    public Jquery append(String xmlstr) throws Exception {
        Jnode nodex;
        if (xmlstr.charAt(0) == '<') {
            nodex = Jxml.parse(xmlstr);
        } else {
            nodex = new Jnode(xmlstr);
        }
        this.node.addChild(nodex);
        return this;
    }

    public Jquery appendAll(String xmlstr) {
        for (Jnode nodex : this.list) {
            nodex.addChild(xmlstr);
        }
        return this;
    }

    public Jquery append(Jquery query) {
        Jnode nodex = query.get(0);
        this.node.addChild(nodex);
        return this;
    }

    public Jquery append(Jnode node) {
        this.node.addChild(node);
        return this;
    }

    public Jquery appendSingle(String tagName) {
        Jnode nodex = new Jnode(tagName);;
        this.node.addChild(nodex);
        return this;
    }

    public Jquery appendTo(Jquery query) {
        query.node.addChild(this.node);
        return this;
    }

    public Jquery appendWith(String tagName) throws Exception {
        Jnode nodex;
        if (tagName.charAt(0) == '<') {
            nodex = Jxml.parse(tagName);
        } else {
            nodex = new Jnode(tagName);
        }
        this.node.addChild(nodex);
        return new Jquery(nodex);
    }

    public Jquery appendSingleWith(String tagName) {
        Jnode node = new Jnode(tagName);
        node.isSingleTag = true;
        this.append(node);
        return new Jquery(node);
    }

    public Jquery remove() {
        Jnode nodex = this.get(0);
        if (nodex.getParentNode() != null) {
            nodex.getParentNode().removeChild(nodex);
        }
        return this;
    }

    public Jquery empty() {
        Jnode nodex = this.get(0);
        nodex.removeChildren();
        return this;
    }

    public Jquery replace(String xmlstr) throws Exception {
        Jnode nodex = Jxml.parse(xmlstr);
        Jnode nod = this.get(0);
        nod.replaceChild(nod, nodex);
        return this;
    }

    public Jquery replace(Jquery newnode) {
        Jnode x = newnode.get(0);
        Jnode q = this.get(0);
        q.replaceChild(q, x);
        return this;
    }

    public Jquery each(JqueryEach each) {
        int index = 0;
        for (Jnode nodex : this.list) {
            boolean isbreak = each.each(new Jquery(nodex), index);
            index++;
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public Jquery root() {
        return new Jquery(this.node.root);
    }

    public void print() {
        this.node.print();
    }

    public void toFile(String fileName) throws Exception {
        this.node.toFile(fileName);
    }

    public void toOut(OutputStream out) throws IOException {
        this.node.toOut(out);
    }
}
