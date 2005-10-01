/*
  org.iterx.miru.dispatcher.handler.factory.TestXmlHandlerChainParser

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

import junit.framework.TestCase;
import org.iterx.miru.bean.MockBeanFactory;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.dispatcher.matcher.MockMatcher;


public class TestXmlHandlerChainParser extends TestCase {

    private XmlHandlerChainFactory handlerChainFactory;
     private XmlHandlerChainParser handlerChainParser;
     private MockBeanFactory beanFactory;

     protected void setUp() {

         beanFactory = new MockBeanFactory();

         handlerChainFactory = new XmlHandlerChainFactory(beanFactory);
         handlerChainParser = new XmlHandlerChainParser(handlerChainFactory);
     }

     protected void tearDown() {

         beanFactory = null;
         handlerChainFactory = null;
         handlerChainParser = null;
     }


     public void testParse() throws IOException {
         MockResource resource;

         beanFactory.addBeanDefinition
             (beanFactory.createBeanDefinition("matcher", MockMatcher.class, false));

         resource = new MockResource();
         resource.setData(("<chains xmlns=\"http://iterx.org/miru/1.0\">" +
                           "<chain id=\"test\">" +
                           "<matcher/>" +
                           // "<handlers>" +
                           // "<handler/>" +
                           // "</handlers>" +
                           "</chain>" +
                           "</chains>").getBytes());

         handlerChainParser.parse(resource);

     }
 

}
