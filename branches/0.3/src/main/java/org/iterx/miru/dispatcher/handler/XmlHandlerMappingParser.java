/*
  org.iterx.miru.dispatcher.handler.XmlHandlerMappingParser

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

package org.iterx.miru.dispatcher.handler;

import java.io.IOException;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.io.Resource;
import org.iterx.miru.beans.BeanWrapper;
import org.iterx.miru.beans.BeanWrapperSupport;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.dispatcher.interceptor.HandlerInterceptor;
import org.iterx.miru.dispatcher.handler.HandlerMapping;

public class XmlHandlerMappingParser extends DefaultHandler {

    protected final Log logger = LogFactory.getLog
        (XmlHandlerMappingParser.class);

    protected static final String SITEMAP_NS =
    "http://iterx.org/miru/map/1.0";

    private static final String TAG_MAP             = "map";
    private static final String TAG_CHAIN           = "chain";

    private static final int STATE_DEFAULT          = 0;
    private static final int STATE_MAP              = 1;
    private static final int STATE_CHAIN            = 2;
    private static final int STATE_HANDLER          = 3;

    private ApplicationContext applicationContext;
    private HandlerMapping handlerMapping;

    private ArrayList interceptors;
    private Object handler;

    private String chainId, handlerId;

    private int state;


    public XmlHandlerMappingParser(ApplicationContext applicationContext,
                                   HandlerMapping handlerMapping) {
        if(applicationContext == null)
            throw new IllegalArgumentException("applicationContext == null");
        if(handlerMapping == null)
            throw new IllegalArgumentException("handlerMapping == null");

        this.applicationContext = applicationContext;
    this.handlerMapping = handlerMapping;
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
        throw new IOException("Invalid xml stream: " +
                              e.getMessage());
    }
    }

    public void startDocument() throws SAXException {

    handler = null;
    interceptors = new ArrayList();
    state = STATE_DEFAULT;
    }

    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attributes)
    throws SAXException {

        System.out.println("START State:"+state+" Tag:"+localName+" ("+uri+")");
    switch(state) {
    case STATE_DEFAULT:
        if(TAG_MAP.equals(localName) &&
           SITEMAP_NS.equals(uri))
        state = STATE_MAP;
        break;
    case STATE_MAP:
        if(TAG_CHAIN.equals(localName) &&
           SITEMAP_NS.equals(uri)) {
        chainId = attributes.getValue("id");

        if(logger.isInfoEnabled())
            logger.info("Starting HandlerChain '" + chainId + "'");
        state = STATE_CHAIN;
        break;
        }
        throw new RuntimeException("Invalid HandlerMapping.");
    case STATE_CHAIN:
        if(SITEMAP_NS.equals(uri)) {
        Object object;

        if((object = applicationContext.getBean(localName)) != null) {
            if(object instanceof HandlerInterceptor) {
            int length;

            if((length = attributes.getLength()) > 0) {
                if(applicationContext.isSingleton(localName) ||
                   !(applicationContext instanceof BeanWrapperSupport))
                throw new RuntimeException
                    ("Immutable bean '" + localName + "'.");
                BeanWrapper wrapper;

                wrapper =
                ((BeanWrapperSupport) applicationContext).assignBeanWrapper(object);


                for(int i = length; i-- > 0;) {
                //BUG: Filter on namespace?
                try {
                    wrapper.setPropertyValue(attributes.getLocalName(i),
                                             attributes.getValue(i));
                }
                catch(Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }
                }
            }

            if(logger.isInfoEnabled())
                logger.info("Adding HandlerInterceptor [" + object.toString() + "]");
            interceptors.add(object);
            }
            else if(object != null) {

            //if(logger.isInfoEnabled())
            //  logger.info("Adding Handler [" + handler.toString() + "]");

            handler = object;
            handlerId = localName;


            //BUG: dynamically load from bean def &
            //should inherit from Dispatcher context

            state = STATE_HANDLER;
            }

            if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext
                (applicationContext);
            break;
        }
        }
        throw new RuntimeException("Invalid HandlerMapping.");
    case STATE_HANDLER:
    default:
        break;
    }

    }

    public void endElement(String uri,
                           String localName,
                           String qName)
    throws SAXException {

        System.out.println("END State:"+state+" Tag:"+localName+" ("+uri+")");

    switch(state) {
    case STATE_DEFAULT:
        break;
    case STATE_MAP:
        if(TAG_MAP.equals(localName) &&
           (SITEMAP_NS.equals(uri)))
        state = STATE_DEFAULT;
        break;
    case STATE_HANDLER:
    case STATE_CHAIN:
        if(TAG_CHAIN.equals(localName) &&
           (SITEMAP_NS.equals(uri))) {
        int length;

        if((length = interceptors.size()) > 0) {

            handlerMapping.addHandler(chainId,
                                      handler,
                                      ((HandlerInterceptor[]) interceptors.toArray
                                       (new HandlerInterceptor[length])));
            interceptors.clear();
        }
        else handlerMapping.addHandler(chainId, handler);

        chainId = null;
        handlerId = null;
        handler = null;
        state = STATE_MAP;
        }
        break;
    default:
        break;
    }
    }


    public void endDocument() throws SAXException {

    interceptors = null;
    handler = null;
    }
}
