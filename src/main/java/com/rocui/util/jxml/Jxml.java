package com.rocui.util.jxml;

import com.rocui.util.jxml.base.Jquery;
import com.rocui.util.file.Jile;
import com.rocui.util.jxml.base.Jnode;
import com.rocui.util.jxml.base.JnodeHandler;
import java.io.StringReader;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Jxml {
    
    public static Jnode parse(InputSource xml) throws Exception {
        JnodeHandler mpr = new JnodeHandler();
        XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        reader.setContentHandler(mpr);
        reader.parse(xml);
        return mpr.getRoot();
    }
    
    public static Jnode parse(String xmlstr) throws Exception {
        StringReader stringReader = new StringReader(xmlstr);
        InputSource xml = new InputSource(stringReader);
        JnodeHandler mpr = new JnodeHandler();
        XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        reader.setContentHandler(mpr);
        reader.parse(xml);
        return mpr.getRoot();
    }
    
    public static Jquery create(InputSource xml) throws Exception {
        JnodeHandler mpr = new JnodeHandler();
        XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        reader.setContentHandler(mpr);
        reader.parse(xml);
        return new Jquery(mpr.getRoot());
    }
    
    public static Jquery create(String xmlstr) throws Exception {
        StringReader stringReader = new StringReader(xmlstr);
        InputSource xml = new InputSource(stringReader);
        JnodeHandler mpr = new JnodeHandler();
        XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        reader.setContentHandler(mpr);
        reader.parse(xml);
        return new Jquery(mpr.getRoot());
    }
    
    public static void main(String[] args) {
        InputSource xmlis;
        try {
//            xmlis = new InputSource(new FileReader("H:\\xx.xml"));
//            Jquery node = Jxml.parse(xmlis);
//            System.out.println(node.root().get(0).getXML());
//            node.find("aa").each(new JqueryEach() {
//                @Override
//                public boolean each(Jquery node, int index) {
//                    System.out.println(node.get(0).getInnerXML());
//                    return false;
//                }
//            });
//            System.out.println(Jxml.create("<e>xxxx</e>").xml("<cc>eeee</cc>").children().eq(0).text("xxxxxx").xml());
//            Jquery query = Jxml.create("<e>xxxx</e>").xml("<cc>eeee</cc>");
//            query.children().eq(0).text("xxxffffffxxx");
//            query.print();
            Jquery query = Jxml.create(Jile.with("E:\\xx.xml").read());
//            System.out.println(query.text());
//            query.find("ToUserName").xml("xxxxxxxx").append("aa$aa<<aaaaa").appendCdata("xxxttt");
//            System.out.println(query.xml());
//            Jile.with("E:\\xxx.xml").write(query.xml());
            query.print();
//            query.toFile("E:\\xxx.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
