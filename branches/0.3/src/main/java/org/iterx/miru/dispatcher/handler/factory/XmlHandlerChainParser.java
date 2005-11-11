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

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.io.StreamSource;

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
    private static final String TAG_HANDLER         = "handler";
    private static final String TAG_MATCHER         = "matcher";
    private static final String TAG_LIST            = "list";

    private static final int STATE_UNKNOWN          = 0;
    private static final int STATE_CHAIN            = 1;
    private static final int STATE_HANDLER          = 2;
    private static final int STATE_MATCHER          = 3;


    private static final Log LOGGER = LogFactory.getLog(XmlHandlerChainParser.class);

    private XmlHandlerChainFactory handlerChainFactory;
    private BeanProvider beanProvider;

    private List stack;
    private Object object;

    private int state;

    public XmlHandlerChainParser(XmlHandlerChainFactory handlerChainFactory) {

        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");

        this.handlerChainFactory = handlerChainFactory;
        this.beanProvider = handlerChainFactory.getBeanProvider();
    }

    public void parse(StreamSource source) throws IOException {
        try {
            SAXParserFactory factory;
            SAXParser parser;

            factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);

            parser = factory.newSAXParser();
            parser.parse(source.getInputStream(), this);
        }
        catch(ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        catch(SAXException e) {
            if(LOGGER.isErrorEnabled()) LOGGER.error(e);
            throw new IOException("Invalid xml stream [" + source + "]. " + e.getMessage());
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

                        stack.add(handlerChainFactory.assignHandlerWrapper(handlerChain));
                        state = STATE_HANDLER;
                        break;
                    }
                }
                throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_MATCHER:
            case STATE_HANDLER:
                if(MIRU_NS.equals(uri)) {
                    Object object;

                    if(TAG_HANDLER.equals(localName)) {
                        stack.add(this.object);
                        this.object = null;
                        state = STATE_HANDLER;
                        break;
                    }
                    else if(TAG_MATCHER.equals(localName)) {
                        stack.add(this.object);
                        this.object = null;
                        state = STATE_MATCHER;
                        break;
                    }
                    else if(TAG_LIST.equals(localName)) {
                        stack.add(new ArrayList());
                        this.object = null;
                        break;
                    }
                    else if((object = beanProvider.getBean(localName)) != null) {
                        BeanWrapper beanWrapper;
                        int length;


                        beanWrapper = ((state == STATE_MATCHER)?
                                       ((BeanWrapperAware) beanProvider).assignBeanWrapper(object) :
                                       handlerChainFactory.assignHandlerWrapper(object));

                        if((length = attributes.getLength()) > 0) {
                            if(beanProvider.isSingleton(localName))
                                throw new BeanException
                                    ("Singleton beans are not mutable.");

                            for(int i = length; i-- > 0; ) {
                                beanWrapper.setValue(attributes.getLocalName(i),
                                                     attributes.getValue(i));                            }

                        }
                        this.object = beanWrapper;
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
             case STATE_MATCHER:
                 if(MIRU_NS.equals(uri)) {
                     Object parent;

                     if(TAG_MATCHER.equals(localName)) {
                         object = stack.remove(stack.size() - 1);
                         if(object instanceof HandlerWrapper)
                             state = STATE_HANDLER;
                     }
                     else if(TAG_LIST.equals(localName)) {
                         Object object;

                         object = stack.remove(stack.size() - 1);
                         parent = stack.get(stack.size() - 1);
                         ((BeanWrapper) parent).setValue("matchers", object);
                     }
                     else {
                         BeanWrapper object;

                         object = (BeanWrapper) this.object;
                         parent = stack.get(stack.size() - 1);

                         if(parent instanceof List)
                             ((List) parent).add(object.getWrappedInstance());
                         else if(parent instanceof BeanWrapper)
                             ((BeanWrapper) parent).setValue("matcher", object.getWrappedInstance());

                         ((BeanWrapperAware) beanProvider).recycleBeanWrapper(object);
                         this.object = null;
                     }
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
             case STATE_HANDLER:
                 if(MIRU_NS.equals(uri)) {
                     Object parent;

                     if(TAG_CHAIN.equals(localName)) {
                         HandlerWrapper object;

                         object = (HandlerWrapper) stack.remove(stack.size() - 1);
                         handlerChainFactory.addHandlerChain((HandlerChain) object.getWrappedInstance());
                         handlerChainFactory.recycleHandlerWrapper(object);

                         this.object = null;
                         state = STATE_CHAIN;
                     }
                     else if(TAG_HANDLER.equals(localName)) {
                         object = stack.remove(stack.size() - 1);
                         state = STATE_HANDLER;
                     }
                     else if(TAG_LIST.equals(localName)) {
                         Object object;

                         object = stack.remove(stack.size() - 1);
                         parent = stack.get(stack.size() - 1);
                         ((HandlerWrapper) parent).setHandlers(object);
                     }
                     else {
                         HandlerWrapper object;

                         parent = stack.get(stack.size() - 1);
                         object = (HandlerWrapper) this.object;

                         if(parent instanceof List)
                             ((List) parent).add(object.getWrappedInstance());
                         else if(parent instanceof HandlerWrapper)
                             ((HandlerWrapper) parent).setHandler(object.getWrappedInstance());

                         handlerChainFactory.recycleHandlerWrapper(object);
                         this.object = null;
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
