package com.rocui.util.jxml.base;

import com.rocui.util.base.reflect.ObjectSnooper;
import com.rocui.util.jxml.Jxml;
import com.rocui.util.file.Jile;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class Jnode {

    public static final String NODE_TYPE_TEXT = "text";
    public static final String NODE_TYPE_ELEMENT = "element";

    private String nodeType = "element";
    protected String tagName;
    protected String liteTagName;
    protected HashMap<String, Object> attributes = new HashMap<String, Object>();
    protected Jnode parentNode;
    protected List<Jnode> children = new ArrayList<Jnode>();
    protected String text;
    protected List<Jnode> allNodes;
    protected Jnode root;

    protected HashMap<String, String> nameSpace = new HashMap<String, String>();
    protected boolean isSingleTag = false;

    private boolean iscdata = false;

    protected Jnode(String tagName) {
        this.tagName = tagName;
        this.parentNode = null;
    }

    public Jnode(String tagName, HashMap<String, Object> attributes, Jnode parentNode) {
        this.tagName = tagName;
        this.attributes = attributes;
        this.parentNode = parentNode;
    }

    public Jnode(String text, Jnode parent) {
        this.text = text;
        this.parentNode = parent;
        this.nodeType = Jnode.NODE_TYPE_TEXT;
    }

    public Jnode addNameSpace(String key, String value) {
        this.nameSpace.put(key, value);
        return this;
    }

    public HashMap<String, String> getNameSpace() {
        return this.nameSpace;
    }

    public String getNameSpaceByName(String key) {
        return this.nameSpace.get(key);
    }

    public boolean containNameSpace(String key) {
        return this.nameSpace.containsKey(key);
    }

    public Jnode addChild(Jnode node) {
        if (node != null) {
            this.children.add(node);
            node.parentNode = this;
            node.root = this.root;
        }
        return this;
    }

    public Jnode addChild(String xmlstr) {
        try {
            Jnode node;
            if (xmlstr.charAt(0) == '<') {
                node = Jxml.parse(xmlstr);
            } else {
                node = new Jnode(xmlstr, this);
            }
            this.children.add(node);
            node.parentNode = this;
            node.root = this.root;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Jnode appendCdata(String content) {
        Jnode node = new Jnode(content, this);
        node.iscdata = true;
        this.children.add(node);
        node.root = this.root;
        return this;
    }

    public Jnode setAttributes(HashMap<String, Object> attributes) {
        this.attributes.putAll(attributes);
        return this;
    }

    public Jnode setAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public Jnode setAttributes(Object bean) {
        try {
            HashMap<String, Object> map = ObjectSnooper.snoop(bean).toHashMap();
            this.attributes.putAll(map);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Jnode getFirstChild() {
        if (this.children.size() > 0) {
            return this.children.get(0);
        } else {
            return null;
        }
    }

    public Jnode getLastChild() {
        if (this.children.size() > 0) {
            return this.children.get(this.children.size() - 1);
        } else {
            return null;
        }
    }

    public Jnode getNextSibling() {
        if (this.parentNode.children.size() > 0) {
            int index = 0;
            int nx = 0;
            for (Jnode node : this.parentNode.children) {
                if (node.equals(this)) {
                    nx = index;
                    break;
                }
                index++;
            }
            if (nx + 1 <= this.parentNode.children.size() - 1) {
                return this.parentNode.children.get(nx + 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getTagName() {
        return tagName;
    }

    public String getLiteTagName() {
        if (this.liteTagName != null) {
            return this.tagName;
        } else {
            return this.liteTagName;
        }
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public String getAttributeByName(String key) {
        return this.attributes.get(key).toString();
    }

    public Jnode getRoot() {
        return this.root;
    }

    public Jnode getParentNode() {
        return parentNode;
    }

    public List<Jnode> getChildren() {
        return children;
    }

    public List<Jnode> getElementsByTagName(String tagName) {
        List<Jnode> ns = new ArrayList<Jnode>();
        for (Jnode node : this.allNodes) {
            if (node.parentNode != null && node.parentNode.equals(this)) {
                if (node.tagName.equals(tagName)) {
                    ns.add(node);
                }
            }
        }
        return ns;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (Jnode node : this.children) {
            if (node.nodeType.equals(Jnode.NODE_TYPE_TEXT)) {
                sb.append(node.text);
            } else {
                sb.append(node.getText());
            }
        }
        return sb.toString();
    }

    public String getSubText() {
        StringBuilder sb = new StringBuilder();
        for (Jnode node : this.children) {
            if (node.nodeType.equals(Jnode.NODE_TYPE_TEXT)) {
                sb.append(node.text);
            }
        }
        return sb.toString();
    }

    public void setText(String text) {
        Jnode node = new Jnode(text, this);
        List<Jnode> list = new ArrayList<Jnode>();
        list.add(node);
        node.parentNode = this;
        this.children = list;
    }

    public void setCdata(String text) {
        Jnode node = new Jnode(text, this);
        node.iscdata = true;
        List<Jnode> list = new ArrayList<Jnode>();
        list.add(node);
        node.parentNode = this;
        node.root = this.root;
        this.children = list;
    }

    public void setInnerXML(String xmlstr) throws Exception {
        if (!this.nodeType.equals(Jnode.NODE_TYPE_TEXT)) {
            Jnode newnode;
            if (xmlstr.charAt(0) == '<') {
                newnode = Jxml.parse(xmlstr);
            } else {
                newnode = new Jnode(xmlstr, this);
            }
            List<Jnode> list = new ArrayList<Jnode>();
            list.add(newnode);
            newnode.parentNode = this;
            this.children = list;
        }
    }

    public String getInnerXML() {
        if (this.children.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Jnode node : this.children) {
                if (node.nodeType.equals(Jnode.NODE_TYPE_TEXT)) {
                    sb.append(node.getAvalidText());
                } else {
                    if (node.isSingleTag) {
                        sb.append("<");
                        sb.append(node.tagName);
                        sb.append(node.getAttributeString());
                        sb.append(node.getNameSpaceString());
                        sb.append("/>");
                    } else {
                        sb.append("<");
                        sb.append(node.tagName);
                        sb.append(node.getAttributeString());
                        sb.append(node.getNameSpaceString());
                        sb.append(">");
                        sb.append(node.getInnerXML());
                        sb.append("</");
                        sb.append(node.tagName);
                        sb.append(">");
                    }
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    public Jnode removeChild(Jnode node) {
        this.children.remove(node);
        return this;
    }

    public Jnode removeChild(int index) {
        if (index >= 0 && index <= this.children.size() - 1) {
            this.children.remove(index);
        }
        return this;
    }

    public Jnode removeChildren() {
        this.children = new ArrayList<Jnode>();
        return this;
    }

    public Jnode insertBefore(Jnode oldnode, Jnode newnode) {
        int index = this.children.lastIndexOf(oldnode);
        if (index - 1 >= 0) {
            this.children.add(index - 1, newnode);
        } else {
            this.children.add(0, newnode);
        }
        newnode.root = this.root;
        return this;
    }

    public Jnode insertAfter(Jnode oldnode, Jnode newnode) {
        int index = this.children.lastIndexOf(oldnode);
        if (index == this.children.size() - 1) {
            this.children.add(newnode);
        } else {
            this.children.add(index + 1, newnode);
        }
        newnode.root = this.root;
        return this;
    }

    public Jnode replaceChild(Jnode node, Jnode newnode) {
        int index = this.children.lastIndexOf(node);
        if (index != -1) {
            this.children.add(index, newnode);
            newnode.parentNode = this;
            this.children.remove(node);
            newnode.root = this.root;
        }
        return this;
    }

    public Jnode replaceChild(int index, Jnode node) {
        if (index >= 0 && index <= this.children.size() - 1) {
            Jnode x = this.children.get(index);
            this.children.add(index, node);
            node.parentNode = this;
            node.root = this.root;
            this.children.remove(x);
        }
        return this;
    }

    public Jnode replaceChild(Jnode node, String xmlstr) {
        try {
            Jnode x = Jxml.parse(xmlstr);
            x.root = this.root;
            this.replaceChild(node, x);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public Jnode replaceChild(int index, String xmlstr) {
        try {
            Jnode x = Jxml.parse(xmlstr);
            x.root = this.root;
            this.replaceChild(index, x);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public String getXML() {
        StringBuilder sb = new StringBuilder();
        if (this.isSingleTag) {
            sb.append("<");
            sb.append(this.tagName);
            sb.append(this.getAttributeString());
            sb.append(this.getNameSpaceString());
            sb.append("/>");
        } else {
            sb.append("<");
            sb.append(this.tagName);
            sb.append(this.getAttributeString());
            sb.append(this.getNameSpaceString());
            sb.append(">");
            sb.append(this.getInnerXML());
            sb.append("</");
            sb.append(this.tagName);
            sb.append(">");
        }
        return sb.toString();
    }

    public String getXML(String version, String encoding) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"");
        sb.append(version);
        sb.append("\" encoding=\"");
        sb.append(encoding);
        sb.append("\"?>");
        if (this.isSingleTag) {
            sb.append("<");
            sb.append(this.tagName);
            sb.append(this.getAttributeString());
            sb.append(this.getNameSpaceString());
            sb.append("/>");
        } else {
            sb.append("<");
            sb.append(this.tagName);
            sb.append(this.getAttributeString());
            sb.append(this.getNameSpaceString());
            sb.append(">");
            sb.append(this.getInnerXML());
            sb.append("</");
            sb.append(this.tagName);
            sb.append(">");
        }
        return sb.toString();
    }

    public void print() {
        String xp = this.getAttributeString();
        String xpt = this.getNameSpaceString();
        String k = "";
        int t = this.level();
        for (int i = 0; i < t; i++) {
            k += "  ";
        }
        if (!this.isSingleTag) {
            System.out.println(k + "<" + this.tagName + (xp.length() > 0 ? " " + xp : "") + (xpt.length() > 0 ? " " + xpt : "") + ">");
            for (Jnode node : this.children) {
                if (node.nodeType.equals(Jnode.NODE_TYPE_TEXT)) {
                    System.out.println(k + "  " + node.getAvalidText());
                } else {
                    node.print();
                }
            }
            System.out.println(k + "</" + this.tagName + ">");
        } else {
            System.out.println(k + "<" + this.tagName + (xp.length() > 0 ? " " + xp : "") + (xpt.length() > 0 ? " " + xpt : "") + "/>");
        }
    }

    private int level() {
        int i = 0;
        Jnode node = this;
        while (node.parentNode != null) {
            node = node.parentNode;
            i++;
        }
        return i;
    }

    public Jnode each(JnodeEach each) {
        int i = 0;
        for (Jnode node : this.children) {
            boolean isbreak = each.each(node, i);
            i++;
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        if (this.nodeType.equals(Jnode.NODE_TYPE_TEXT)) {
            return this.text;
        } else {
            return this.getXML();
        }
    }

    private String getAttributeString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, Object> map : this.attributes.entrySet()) {
            sb.append(" ");
            sb.append(map.getKey());
            sb.append("=\"");
            sb.append(map.getValue());
            sb.append("\"");
        }
        return sb.toString();
    }

    private String getNameSpaceString() {
        StringBuilder sb = new StringBuilder();
        if (this.nameSpace.size() > 0) {
            for (Entry<String, String> x : this.nameSpace.entrySet()) {
                sb.append(" xmlns");
                if (!x.getKey().equals("")) {
                    sb.append(":");
                }
                sb.append(x.getKey());
                sb.append("=\"");
                sb.append(x.getValue());
                sb.append("\"");
            }
        }
        return sb.toString();
    }

    private boolean isAvalid(String text) {
        if (text != null && text.length() > 0) {
            String k = "[<>&'\"\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]+";
            return Pattern.compile(k).matcher(text).find();
        } else {
            return false;
        }
    }

    private String wrapCdata(String content) {
        StringBuffer sb = new StringBuffer();
        sb.append("<![CDATA[");
        sb.append(content);
        sb.append("]]>");
        return sb.toString();
    }

    private String getAvalidText() {
        String r = this.text;
        if (r != null && r.length() > 0) {
            if (this.iscdata) {
                r = this.wrapCdata(this.text);
            } else {
                if (this.isAvalid(this.text)) {
                    r = this.wrapCdata(this.text);
                }
            }
        } else {
            r = "";
        }
        return r;
    }

    public void toFile(String filename) throws Exception {
        String x = this.getXML();
        Jile.with(filename).write(x);
    }

    public void toFile(String filename, String version, String encode) throws Exception {
        String x = this.getXML(version, encode);
        Jile.with(filename).write(x);
    }

    public void toOut(OutputStream out) throws IOException {
        String x = this.getXML();
        out.write(x.getBytes());
    }

    public void toOut(OutputStream out, String version, String encode) throws IOException {
        String x = this.getXML(version, encode);
        out.write(x.getBytes());
    }
}
