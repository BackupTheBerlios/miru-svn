/*
  org.iterx.miru.bean.factory.XmlBeanParser

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  Copyright (C)2004-2005 Darren Graves <darren@iterx.org>
  All Rights Reserved.
*/

package org.iterx.miru.bean.factory;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.io.Resource;

import org.iterx.miru.bean.Bean;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanRef;
import org.iterx.util.KeyValue;


public class XmlBeanParser extends DefaultHandler {

    public static final String MIRU_NS              = "http://iterx.org/miru/1.0";

    private static final String TAG_BEANS           = "beans";
    private static final String TAG_BEAN            = "bean";
    private static final String TAG_LIST            = "list";
    private static final String TAG_MAP             = "map";
    private static final String TAG_ENTRY           = "entry";


    private static final int STATE_UNKNOWN          = 0;
    private static final int STATE_BEANS            = 1;
    private static final int STATE_BEAN             = 2;
    private static final int STATE_PROPERTY         = 3;
    private static final int STATE_COLLECTION       = 4;


    protected static final Log LOGGER = LogFactory.getLog(XmlBeanParser.class);

    private XmlBeanFactory beanFactory;
    private int state;

    private List stack;

    private Object bean;
    private Object key, value;

    public XmlBeanParser(XmlBeanFactory beanFactory) {

        if(beanFactory == null)
            throw new IllegalArgumentException("beanFactory == null");

        this.beanFactory = beanFactory;
    }

    public void parse(Resource resource) throws IOException {
        try {
            SAXParserFactory factory;
            SAXParser parser;

            factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);

            parser = factory.newSAXParser();
            parser.parse(resource.getInputStream(), this);
        }
        catch(ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        catch(SAXException e) {
            if(LOGGER.isErrorEnabled()) LOGGER.error(e, e);
            throw new IOException("Invalid xml stream [" + resource + "]. " + e.getMessage());
        }
    }


    public void startDocument() throws SAXException {

        state = STATE_UNKNOWN;
        stack = new ArrayList();
    }


    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        System.out.println("start " + qName + " " + state);
        switch(state) {
            case STATE_UNKNOWN:
                if(TAG_BEANS.equals(localName) &&
                   MIRU_NS.equals(uri)) state = STATE_BEANS;
                break;
            case STATE_COLLECTION:
                if(TAG_ENTRY.equals(localName) &&
                   MIRU_NS.equals(uri)) {
                    Object value;

                    if((value = attributes.getValue("value")) != null)
                        this.value = value;
                    else if((value = attributes.getValue("ref")) != null)
                        this.value = new BeanRef((String) value);
                    key = attributes.getValue("key");
                    state = STATE_PROPERTY;
                    break;
                }
                throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_PROPERTY:
                if(MIRU_NS.equals(uri)) {
                    if(TAG_BEAN.equals(localName)) {
                        stack.add(bean);
                        bean = null;
                        value = null;
                        state = STATE_BEANS;
                    }
                    else if(TAG_LIST.equals(localName)) {
                        stack.add(bean);

                        bean = new ArrayList();
                        value = null;
                        state = STATE_COLLECTION;
                        break;
                    }
                    else if(TAG_MAP.equals(localName)) {
                        stack.add(bean);

                        bean = new HashMap();
                        value = null;
                        state = STATE_COLLECTION;
                        break;
                    }
                }
                else throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_BEANS:
                if(TAG_BEAN.equals(localName) &&
                   MIRU_NS.equals(uri)) {
                    String cls;

                    cls = attributes.getValue("class");
                    try {
                        Bean bean;

                        bean = beanFactory.createBeanDefinition
                            (attributes.getValue("id"),
                             ((cls == null)? null : Class.forName(cls)),
                             (!"false".equals(attributes.getValue("singleton"))));

                        this.bean = beanFactory.assignBeanWrapper(bean);
                    }
                    catch(ClassNotFoundException e) {
                        throw new RuntimeException("Bean class '" + cls + "' not found.");
                    }
                    state = STATE_BEAN;
                    break;
                }
                throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_BEAN:
                if(MIRU_NS.equals(uri)) {
                    Object value;

                    if((value = attributes.getValue("value")) != null)
                        this.value = value;
                    else if((value = attributes.getValue("ref")) != null)
                        this.value = new BeanRef((String) value);
                    state = STATE_PROPERTY;
                    break;
                }
                throw new SAXException("Invalid element '" + qName + "'.");

        }
    }

    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        switch(state) {
            case STATE_PROPERTY:
                value = ((value == null)?
                          new String(ch, start, length) :
                          value + new String(ch, start, length));
                break;
        }

    }

    public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException {
        System.out.println("end " + qName + " " + state);

         switch(state) {
             case STATE_UNKNOWN:
                 break;
             case STATE_BEANS:
                 if(TAG_BEANS.equals(localName) &&
                    MIRU_NS.equals(uri)) {
                     state = STATE_UNKNOWN;
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
             case STATE_BEAN:
                 if(TAG_BEAN.equals(localName) &&
                    MIRU_NS.equals(uri)) {

                     if(stack.isEmpty()) {
                         BeanWrapper bean;

                         bean = (BeanWrapper) this.bean;
                         beanFactory.addBeanDefinition((Bean) bean.getWrappedInstance());
                         beanFactory.recycleBeanWrapper(bean);
                         bean = null;
                         state = STATE_BEANS;
                     }
                     else {
                         BeanWrapper bean;

                         bean = (BeanWrapper) this.bean;
                         value = bean.getWrappedInstance();
                         beanFactory.recycleBeanWrapper(bean);

                         this.bean = stack.remove(stack.size() - 1);
                         state = STATE_PROPERTY;
                     }
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
             case STATE_PROPERTY:
                 if(MIRU_NS.equals(uri)) {
                     if(bean instanceof BeanWrapper) {
                         BeanWrapper bean;

                         bean = (BeanWrapper) this.bean;
                         bean.setPropertyValue
                             ("propertyValue", new KeyValue(localName, value));
                         value = null;

                         state = STATE_BEAN;
                         break;
                     }
                     state = STATE_COLLECTION;
                 }
                 else throw new SAXException("Invalid element '" + qName + "'.");
             case STATE_COLLECTION:
                 if(MIRU_NS.equals(uri)) {
                     if(TAG_ENTRY.equals(localName)) {
                         if(bean instanceof List)
                             ((List) bean).add(value);
                         else if(bean instanceof Map)
                             ((Map) bean).put(key,value);

                         value = null;
                         key = null;
                     }
                     else if(TAG_LIST.equals(localName) ||
                             TAG_MAP.equals(localName)) {

                         key = null;
                         value = bean;
                         bean = stack.remove(stack.size() - 1);
                         state = STATE_PROPERTY;
                     }
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
         }
    }


    public void endDocument() throws SAXException {

        stack = null;
        bean = null;
        value = null;
    }




}
