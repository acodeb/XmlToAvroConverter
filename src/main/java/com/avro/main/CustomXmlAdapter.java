package com.avro.main;

import org.w3c.dom.Element;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by Ankit Jindal on 7/2/2016.
 */
public class CustomXmlAdapter extends XmlAdapter<Object, String> {
    public String unmarshal(Object v) throws Exception {
        Element e = (Element) v;
        return e.getLocalName()+":"+e.getTextContent();
    }

    public Object marshal(String v) throws Exception {
        return v;
    }
}
