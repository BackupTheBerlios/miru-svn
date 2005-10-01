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
import java.util.LinkedList;

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
import org.iterx.miru.dispatcher.handler.HandlerChainWrapper;
import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.matcher.Matcher;


public class XmlHandlerChainParser extends DefaultHandler {

    public static final String MIRU_NS              = "http://iterx.org/miru/1.0";

    private static final String TAG_CHAINS          = "chains";
    private static final String TAG_CHAIN           = "chain";
    private static final String TAG_HANDLERS        = "handlers";

    private static final int STATE_UNKNOWN          = 0;
    private static final int STATE_CHAINS           = 1;
    private static final int STATE_CHAIN            = 2;
    private static final int STATE_MATCHER          = 3;
    private static final int STATE_HANDLERS         = 4;
    private static final int STATE_HANDLER          = 5;


    protected static final Log LOGGER = LogFactory.getLog(XmlHandlerChainParser.class);


    private int state;

    private XmlHandlerChainFactory handlerChainFactory;
    private BeanWrapperAware beanWrapper;
    private BeanProvider beanProvider;


    private HandlerChainWrapper handlerChain;
    private LinkedList beans;

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
            if(LOGGER.isErrorEnabled()) LOGGER.error(e);
            throw new IOException("Invalid xml stream [" + resource + "]. " + e.getMessage());
        }
    }

    public void startDocument() throws SAXException {

        state = STATE_UNKNOWN;

        beans = new LinkedList();
    }


    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        System.out.println("start " + qName + " " + state);
        switch(state) {
            case STATE_UNKNOWN:
                if(TAG_CHAINS.equals(localName) &&
                   MIRU_NS.equals(uri)) state = STATE_CHAINS;
                break;
            case STATE_CHAINS:
                if(TAG_CHAIN.equals(localName) &&
                   MIRU_NS.equals(uri)) {

                    handlerChain = handlerChainFactory.assignHandlerChainWrapper
                        (handlerChainFactory.createHandlerChain());
                    handlerChain.setId(attributes.getValue("id"));

                    state = STATE_CHAIN;
                    break;
                }
                throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_CHAIN:
                if(MIRU_NS.equals(uri)) {
                    Object object;

                    if(TAG_HANDLERS.equals(localName)) {
                        state = STATE_HANDLERS;
                        break;
                    }
                    else if((object = beanProvider.getBean(localName)) != null) {
                        BeanWrapper bean;

                        bean = beanWrapper.assignBeanWrapper(object);
                        //applyAttributes(bean, attributes);

                        beans.add(bean);
                        state = STATE_MATCHER;
                        break;
                    }
                }
                throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_MATCHER:
                if(MIRU_NS.equals(uri)) {
                    Object object;

                    if((object = beanProvider.getBean(localName)) != null) {
                        BeanWrapper bean;

                        bean = beanWrapper.assignBeanWrapper(object);
                        beans.add(bean);
                        break;
                    }
                }
                throw new SAXException("Invalid element '" + qName + "'.");
            case STATE_HANDLERS:
                break;
        }

    }

    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {

    }

    public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException {
        System.out.println("end " + qName + " " + state);

         switch(state) {
             case STATE_UNKNOWN:
                 break;
             case STATE_CHAINS:
                 if(TAG_CHAINS.equals(localName) &&
                    MIRU_NS.equals(uri)) {

                     state = STATE_UNKNOWN;
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
             case STATE_HANDLERS:
                 if(TAG_HANDLERS.equals(localName) &&
                    MIRU_NS.equals(uri)) {

                     state = STATE_CHAIN;
                     break;
                 }
             case STATE_CHAIN:
                 if(TAG_CHAIN.equals(localName) &&
                    MIRU_NS.equals(uri)) {
                     HandlerChainWrapper handlerChain;

                     handlerChain = this.handlerChain;
                     this.handlerChain = null;

                     handlerChainFactory.addHandlerChain
                         ((HandlerChain) handlerChain.getWrappedInstance());
                     handlerChainFactory.recycleHandlerChainWrapper(handlerChain);

                     state = STATE_CHAINS;
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
             case STATE_MATCHER:
                 if(MIRU_NS.equals(uri)) {
                     BeanWrapper bean;

                     bean = (BeanWrapper) beans.removeLast();
                     if(beans.isEmpty()) {
                         handlerChain.setMatcher((Matcher) bean.getWrappedInstance());

                     }
                     else {
                         ((BeanWrapper) beans.getLast()).setPropertyValue
                             (localName, bean.getWrappedInstance());
                     }

                     beanWrapper.recycleBeanWrapper(bean);
                     state = STATE_HANDLERS;
                     break;
                 }
                 throw new SAXException("Invalid element '" + qName + "'.");
         }
    }


    public void endDocument() throws SAXException {

        handlerChain = null;
        beans = null;
    }


    private static final void applyAttributes(BeanWrapper bean,
                                              Attributes attributes) {



    }


}
