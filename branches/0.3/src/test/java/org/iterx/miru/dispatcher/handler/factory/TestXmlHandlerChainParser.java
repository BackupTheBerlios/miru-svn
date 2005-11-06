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
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.iterx.miru.bean.factory.MockBeanFactory;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.dispatcher.matcher.Matcher;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.dispatcher.handler.factory.HandlerChainImpl;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.context.ProcessingContext;


public class TestXmlHandlerChainParser extends TestCase {

    private static final String NS = "http://iterx.org/miru/1.0";


    private static final Object[][] CHAINS = {
        {
            "<chains xmlns=\"" + NS + "\">" +
            "<chain>" +
            "</chain>" +
            "</chains>",
            new Integer(1),
            new String[] { null },
            new Handler[] { null }
        },

        {
            "<chains xmlns=\"" + NS + "\">" +
            "<chain id=\"a\">" +
            "</chain>" +
            "</chains>",
            new Integer(1),
            new String[] { "a" },
            new Handler[] { null }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler/>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler() }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<handler>" +
          "<my-handler/>" +
          "</handler>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler(new MyHandler()) }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<handler>" +
          "<my-handler>" +
          "<handler>" +
          "<my-handler/>" +
          "</handler>" +
          "</my-handler>" +
          "</handler>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler(new MyHandler(new MyHandler())) }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<handler>" +
          "<list>" +
          "<my-handler/>" +
          "<my-handler/>" +
          "</list>" +
          "</handler>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler(new MyList(new MyHandler[] { new MyHandler(), new MyHandler()})) }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<handler>" +
          "<list>" +
          "<my-handler>" +
          "<handler>" +
          "<my-handler/>" +
          "</handler>" +
          "</my-handler>" +
          "<my-handler/>" +
          "</list>" +
          "</handler>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler(new MyList
              (new MyHandler[] { new MyHandler(new MyHandler()), new MyHandler()})) }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<handler>" +
          "<list>" +
          "<my-handler>" +
          "<handler>" +
          "<list>" +
          "<my-handler/>" +
          "<my-handler/>" +
          "</list>" +
          "</handler>" +
          "</my-handler>" +
          "<my-handler/>" +
          "</list>" +
          "</handler>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler(new MyList
              (new MyHandler[] { new MyHandler(new MyList
                  (new MyHandler[] { new MyHandler(), new MyHandler() })), new MyHandler()})) }
        },
    };

    private XmlHandlerChainFactory handlerChainFactory;
    private XmlHandlerChainParser handlerChainParser;
    private MockBeanFactory beanFactory;

    private Object[] data;

    public static Test suite() {
        TestSuite suite;

        suite = new TestSuite();

        for(int i = 0; i < CHAINS.length; i++) {
            suite.addTest(new TestXmlHandlerChainParser("testParse", CHAINS[i]));
        }
        return suite;
    }


    public TestXmlHandlerChainParser(String name, Object[] data) {

        super(name);
        this.data = data;
    }

    protected void setUp() {

        beanFactory = new MockBeanFactory();
        beanFactory.addBeanDefinition
            (beanFactory.createBeanDefinition("my-matcher", MyMatcher.class, false));
        beanFactory.addBeanDefinition
            (beanFactory.createBeanDefinition("my-handler", MyHandler.class, false));

        handlerChainFactory = new XmlHandlerChainFactory(beanFactory);
        handlerChainParser = new XmlHandlerChainParser(handlerChainFactory);
    }

    protected void tearDown() {

        handlerChainFactory = null;
        handlerChainParser = null;
        beanFactory = null;
    }

    public void testParse() throws IOException {
        MockResource resource;
        HandlerChainMap chains;
        Iterator iterator;

        resource = new MockResource();
        resource.setData(((String) data[0]).getBytes());

        handlerChainParser.parse(resource);

        assertNotNull(chains = handlerChainFactory.getHandlerChains());
        iterator = chains.iterator();
        for(int i = ((Integer) data[1]).intValue(); i-- > 0; ) {
            HandlerChainImpl chain;
            Handler handler;
            String id;

            assertTrue(iterator.hasNext());
            assertNotNull((chain = (HandlerChainImpl) iterator.next()));
            assertEquals(((id  = ((String[]) data[2])[i]) != null)? id : null,
                         chain.getId());
            assertEquals(((handler  = ((Handler[]) data[3])[i]) != null)? handler : null,
                         chain.getHandler());
        }

    }

    public static class MyHandler implements Handler {

        private Matcher matcher;
        private Object object;

        public MyHandler() {}

        public MyHandler(Object object) {

            this.object = object;
        }

        public MyHandler(Matcher matcher, Object object) {

            this.matcher = matcher;
            this.object = object;
        }

        public void setMatcher(Matcher matcher) {

            this.matcher = matcher;
        }

        public void setHandler(Handler handler) {

            object = handler;
        }

        public void setHandlers(List handlers) {

            object = handlers;
        }

        public int execute(ProcessingContext processingContext) {

            return Dispatcher.OK;
        }

        public boolean equals(Object object) {

            return ((object instanceof MyHandler) &&
                    (((MyHandler) object).object == this.object ||
                     (((MyHandler) object).object).equals(this.object)) &&
                     (((MyHandler) object).matcher == this.matcher ||
                      (((MyHandler) object).matcher).equals(this.matcher)));
        }

    }


    public static class MyMatcher implements Matcher {

        private Object object;


        public MyMatcher() {}

        public MyMatcher(Object object) {

            this.object = object;
        }

        public void setString(String string) {

            object = string;
        }

        public void setMatchers(List matchers) {

            object = matchers;
        }


        public Object[] getMatches(ProcessingContext context) {

            return new Object[0];
        }

        public boolean hasMatches(ProcessingContext context) {

            return false;
        }

        public int hashCode() {

            return (object != null)? object.hashCode() : 0;
        }

        public boolean equals(Object object) {

            return ((object instanceof MyMatcher) &&
                    (((MyMatcher) object).object == this.object ||
                     (((MyMatcher) object).object).equals(this.object)));

        }

    }


     public static class MyList extends ArrayList {

        public MyList() {}

        public MyList(Object[] objects) {

            for(int i = 0; i < objects.length; i++) {
                add(objects[i]);
            }
        }
    }

    public static class MyMap extends LinkedHashMap {

        public MyMap() {}

        public MyMap(Object[] objects) {

            for(int i = 0; i < objects.length; i += 2) {
                put(objects[i], objects[i + 1]);
            }
        }
    }
}
