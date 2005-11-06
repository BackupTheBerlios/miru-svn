/*
  org.iterx.miru.dispatcher.handler.factory.XmlHandlerChainParser

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

package org.iterx.miru.dispatcher.handler.factory;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.io.Resource;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanException;
import org.iterx.miru.dispatcher.handler.HandlerWrapper;
import org.iterx.miru.dispatcher.handler.HandlerChain;


public class XmlHandlerChainParser extends DefaultHandler {

    public static final String MIRU_NS              = "http://iterx.org/miru/1.0";

    private static final String TAG_CHAINS          = "chains";
    private static final String TAG_CHAIN           = "chain";
    private static final String TAG_MATCHER         = "matcher";
    private static final String TAG_HANDLER         = "handler";
    private static final String TAG_LIST            = "list";
    private static final String TAG_MAP             = "map";

    private static final int STATE_UNKNOWN          = 0;
    private static final int STATE_CHAIN            = 1;
    private static final int STATE_MATCHER          = 2;
    private static final int STATE_HANDLER          = 3;


    private static final Log LOGGER = LogFactory.getLog(XmlHandlerChainParser.class);

    private XmlHandlerChainFactory handlerChainFactory;
    private BeanWrapperAware beanWrapper;
    private BeanProvider beanProvider;

    private List stack;
    private Object object;

    private int state;

    public XmlHandlerChainParser(XmlHandlerChainFactory handlerChainFactory) {

        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");

        this.handlerChainFactory = handlerChainFactory;
        this.beanProvider = handlerChainFactory.getBeanProvider();
        this.beanWrapper = (BeanWrapperAware) beanProvider;
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
            e.printStackTrace();
            if(LOGGER.isErrorEnabled()) LOGGER.error(e);
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
                if(TAG_CHAINS.equals(localName) &&
                   MIRU_NS.equals(uri)) state = STATE_CHAIN;
                break;
            case STATE_CHAIN:
                if(MIRU_NS.equals(uri)) {
                    if(TAG_CHAIN.equals(localName)) {
                        HandlerChain handlerChain;

                        handlerChain = handlerChainFactory.createHandlerChain();
                        handlerChain.setId(attributes.getValue("id"));

                        stack.add
                            (handlerChainFactory.assignHandlerWrapper(handlerChain));
                        object = null;

                        state = STATE_HANDLER;
                        break;
                    }
                }
                throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_HANDLER:
            //case STATE_MATCHER:
                if(MIRU_NS.equals(uri)) {
                    Object bean;

                    if(TAG_HANDLER.equals(localName)) {
                        if(object != null) {
                            //if(object instanceof List ||
                            // object instanceof Map) stack.add(object);
                            //else
                            stack.add
                                (handlerChainFactory.assignHandlerWrapper(object));
                        }
                        object = null;
                        break;
                    }
                    else if(TAG_LIST.equals(localName)) {

                        stack.add(new ArrayList());
                        object = null;
                        break;
                    }
                    else if((bean = beanProvider.getBean(localName)) != null) {
                        int length;

                        if((length = attributes.getLength()) > 0) {
                            BeanWrapper wrapper;

                            if(beanProvider.isSingleton(localName))
                                throw new BeanException
                                    ("Singleton beans are not mutable.");

                            wrapper = beanWrapper.assignBeanWrapper(bean);
                            for(int i = length; i-- > 0; ) {
                                wrapper.setValue(attributes.getLocalName(i),
                                                 attributes.getValue(i));
                            }
                            beanWrapper.recycleBeanWrapper(wrapper);
                        }


                        object = bean;
                        break;
                    }
                }
                throw new SAXException("Invalid element '" + qName + "'.");
        }
    }

    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {}

    public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException {
        System.out.println("end " + qName + " " + state);

         switch(state) {
             case STATE_UNKNOWN:
                 break;
             case STATE_CHAIN:
                 if(MIRU_NS.equals(uri)) {
                     if(TAG_CHAINS.equals(localName)) {
                         state = STATE_UNKNOWN;
                         break;
                     }
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
             //case STATE_MATCHER:
             case STATE_HANDLER:
                 System.out.println(stack);
                 if(MIRU_NS.equals(uri)) {

                     if(TAG_CHAIN.equals(localName)) {
                         HandlerWrapper wrapper;
                         wrapper = (HandlerWrapper) stack.remove(stack.size() - 1);

                         handlerChainFactory.addHandlerChain((HandlerChain) wrapper.getWrappedInstance());
                         handlerChainFactory.recycleHandlerWrapper(wrapper);
                         state = STATE_CHAIN;
                         object = null;
                     }
                     else if(TAG_HANDLER.equals(localName)) {
                         Object parent;

                         parent = stack.remove(stack.size() - 1);
                         if(parent != null) {
                             //always true
                             HandlerWrapper wrapper;
                             wrapper = (HandlerWrapper) parent;

                             object = wrapper.getWrappedInstance();
                             handlerChainFactory.recycleHandlerWrapper(wrapper);
                         }
                     }
                     else if(TAG_LIST.equals(localName)) {

                         Object parent;

                         object = stack.remove(stack.size() - 1);
                         parent = stack.get(stack.size() - 1);
                         if(parent instanceof HandlerWrapper ) {
                             HandlerWrapper wrapper;

                             wrapper = (HandlerWrapper) parent;
                             wrapper.setValue("handler", object);
                         }
                         object = null;

                     }
                     else {
                         Object parent;

                         parent = stack.get(stack.size() - 1);
                         if(parent instanceof List) {
                             ((List) parent).add(object);


                         }
                         else if(parent instanceof HandlerWrapper ) {
                             HandlerWrapper wrapper;

                             wrapper = (HandlerWrapper) parent;
                             wrapper.setValue("handler", object);
                         }
                         object = null;
                     }
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
         }
    }

    public void endDocument() throws SAXException {

        stack = null;
    }



}
