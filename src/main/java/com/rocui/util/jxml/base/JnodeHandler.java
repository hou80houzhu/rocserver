package com.rocui.util.jxml.base;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class JnodeHandler extends DefaultHandler {

    private Stack<Jnode> sta = new Stack<Jnode>();
    private Jnode root = null;
    private List<Jnode> nodes = new ArrayList<Jnode>();

    private HashMap<String, String> ns = new HashMap<String, String>();
    private Jnode current;

    private boolean istext = false;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String fullName, Attributes attributes) throws SAXException {
        this.istext = false;
        HashMap<String, Object> has = this.getMap(attributes);
        Jnode prenode = new Jnode(fullName, has, null);
        prenode.liteTagName = localName;
        if (root == null) {
            root = prenode;
        }
        sta.push(prenode);
        nodes.add(prenode);
        prenode.allNodes = nodes;
        prenode.root = root;
        prenode.nameSpace = ns;
        ns = new HashMap<String, String>();

        this.current = prenode;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        Jnode prenode = sta.peek();
        if (!istext && prenode.equals(this.current)) {
            prenode.isSingleTag = true;
        }
        sta.pop();
        if (!sta.empty()) {
            root = sta.peek().addChild(prenode);
            prenode.parentNode = sta.peek();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        istext = true;
        String tmp = new String(ch, start, length);
        tmp = tmp.trim();
        if (tmp.length() > 0) {
            sta.peek().addChild(new Jnode(tmp, sta.peek()));
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        ns.put(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public Jnode getRoot() {
        return this.root;
    }

    private HashMap<String, Object> getMap(Attributes attributes) {
        HashMap<String, Object> rt = new HashMap<String, Object>();
        for (int i = 0; i < attributes.getLength(); i++) {
            try {
                rt.put(attributes.getQName(i), new String(attributes.getValue(i).getBytes(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                rt.put(attributes.getLocalName(i), "");
            }
        }
        return rt;
    }
}
