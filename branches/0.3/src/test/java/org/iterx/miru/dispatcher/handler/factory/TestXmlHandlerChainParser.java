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

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.iterx.miru.bean.factory.MockBeanFactory;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.matcher.Matcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;


public class TestXmlHandlerChainParser extends TestCase {

    private static final String NS = "http://iterx.org/miru/1.0/chains";


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
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<matcher>" +
          "<my-matcher/>" +
          "</matcher>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler<RequestContext, ResponseContext>(new MyMatcher<RequestContext, ResponseContext>(), null) }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<matcher>" +
          "<my-matcher/>" +
          "</matcher>" +
          "<handler>" +
          "<my-handler/>" +
          "</handler>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler<RequestContext, ResponseContext>(new MyMatcher<RequestContext, ResponseContext>(), new MyHandler<RequestContext, ResponseContext>()) }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<matcher>" +
          "<my-matcher>" +
          "<matcher>" +
          "<my-matcher/>" +
          "</matcher>" +
          "</my-matcher>" +
          "</matcher>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler<RequestContext, ResponseContext>(new MyMatcher<RequestContext, ResponseContext>(new MyMatcher<RequestContext, ResponseContext>()), null) }
        },
        {
          "<chains xmlns=\"" + NS + "\">" +
          "<chain id=\"a\">" +
          "<my-handler>" +
          "<matcher>" +
          "<my-matcher>" +
          "<matcher>" +
          "<list>" +
          "<my-matcher/>" +
          "<my-matcher/>" +
          "</list>" +
          "</matcher>" +
          "</my-matcher>" +
          "</matcher>" +
          "</my-handler>" +
          "</chain>" +
          "</chains>",
          new Integer(1),
          new String[] { "a" },
          new Handler[] { new MyHandler<RequestContext, ResponseContext>(new MyMatcher<RequestContext, ResponseContext>(new MyList(new MyMatcher[]{ new MyMatcher<RequestContext, ResponseContext>(), new MyMatcher<RequestContext, ResponseContext>()})), null) }
        },
    };

    private XmlHandlerChainFactory<RequestContext, ResponseContext> handlerChainFactory;
    private XmlHandlerChainParser<RequestContext, ResponseContext> handlerChainParser;


    private Object[] data;

    public static Test suite() {
        TestSuite suite;

        suite = new TestSuite();

        for(Object[] chain : CHAINS) {
            suite.addTest(new TestXmlHandlerChainParser("testParse", chain));
        }
        return suite;
    }


    public TestXmlHandlerChainParser(String name, Object[] data) {

        super(name);
        this.data = data;
    }

    protected void setUp() {
        MockBeanFactory beanFactory;

        beanFactory = new MockBeanFactory();
        beanFactory.addBeanDefinition
            (beanFactory.createBeanDefinition("my-matcher", MyMatcher.class, false));
        beanFactory.addBeanDefinition
            (beanFactory.createBeanDefinition("my-handler", MyHandler.class, false));

        handlerChainFactory = new XmlHandlerChainFactory<RequestContext, ResponseContext>(beanFactory);
        handlerChainParser = new XmlHandlerChainParser<RequestContext, ResponseContext>(handlerChainFactory);
    }

    protected void tearDown() {

        handlerChainFactory = null;
        handlerChainParser = null;
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
            if((id  = ((String[]) data[2])[i]) != null) assertEquals(id, chain.getId());

            assertEquals(((handler  = ((Handler[]) data[3])[i]) != null)? handler : null,
                         chain.getHandler());
        }

    }

    public static class MyHandler<S extends RequestContext, T extends ResponseContext> implements Handler<S, T>, Matcher<S, T> {

        private Matcher<S, T> matcher;
        private Object object;

        public MyHandler() {}

        public MyHandler(Object object) {

            this.object = object;
        }

        public MyHandler(Matcher<? extends S, ? extends T> matcher, Object object) {

            this.matcher = (Matcher<S, T>) matcher;
            this.object = object;
        }

        public void setMatcher(Matcher<? extends S, ? extends T> matcher) {

            this.matcher = (Matcher<S, T>) matcher;
        }

        public void setHandler(Handler<? extends S, ? extends T> handler) {

            object = handler;
        }

        public void setHandlers(List<Handler<? extends S, ? extends T>> handlers) {

            object = handlers;
        }

        public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {

            return matcher.getMatches(processingContext);
        }

        public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {

            return matcher.hasMatches(processingContext);
        }


        public int execute(ProcessingContext<? extends S, ? extends T> processingContext) {

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


    public static class MyMatcher<S extends RequestContext, T extends ResponseContext> implements Matcher<S, T> {

        private Object object;


        public MyMatcher() {}

        public MyMatcher(Object object) {

            this.object = object;
        }

        public void setString(String string) {

            object = string;
        }

        public void setMatcher(Matcher<? extends S, ? extends T> matcher) {

            object = matcher;
        }

        public void setMatchers(List<Matcher<? extends S, ? extends T>> matchers) {

            object = matchers;
        }

        public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {

            return false;
        }


        public Matches getMatches(ProcessingContext<? extends S, ? extends T> prcoessingContext) {

            return new Matches();
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

    public static class MyList extends ArrayList<Object> {

        public MyList() {}

        public MyList(Object[] objects) {

            for(Object object : objects) {
                add(object);
            }
        }
    }

}
