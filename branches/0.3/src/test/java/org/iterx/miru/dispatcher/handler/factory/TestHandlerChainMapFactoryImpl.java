/*
  org.iterx.miru.dispatcher.handler.factory.TestHandlerChainMapFactoryImpl

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

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;


public class TestHandlerChainMapFactoryImpl extends TestCase {

    public void testConstructors() {
        HandlerChainFactory<RequestContext, ResponseContext> handlerChainFactory;

        handlerChainFactory = new HandlerChainFactoryImpl<RequestContext, ResponseContext>();
        assertNotNull(handlerChainFactory);
    }

    public void testHandlerChainAccessors() {
        HandlerChainFactory<RequestContext, ResponseContext> handlerChainFactory;
        HandlerChainMap<RequestContext, ResponseContext> handlerChainMap;

        handlerChainFactory = new HandlerChainFactoryImpl<RequestContext, ResponseContext>();
        assertNotNull(handlerChainMap = handlerChainFactory.getHandlerChains());
        assertFalse((handlerChainMap.iterator()).hasNext());
    }


    public void testHandlerAdapterAccessors() {
        HandlerChainFactory<RequestContext, ResponseContext> handlerChainFactory;
        List<HandlerAdapter<? extends RequestContext, ? extends ResponseContext>> handlerAdapterList;
        HandlerAdapter<RequestContext, ResponseContext>[] handlerAdapters;
        HandlerAdapter<RequestContext, ResponseContext> handlerAdapterA, handlerAdapterB;

        handlerChainFactory = new HandlerChainFactoryImpl<RequestContext, ResponseContext>();

        assertNotNull(handlerAdapters = handlerChainFactory.getHandlerAdapters());
        assertEquals(0, handlerAdapters.length);

        handlerChainFactory.addHandlerAdapter(handlerAdapterA = new MockHandlerAdapter<RequestContext, ResponseContext>());

        assertNotNull(handlerAdapters = handlerChainFactory.getHandlerAdapters());
        assertEquals(1, handlerAdapters.length);
        assertEquals(handlerAdapterA, handlerAdapters[0]);

        handlerChainFactory.addHandlerAdapter(handlerAdapterB = new MockHandlerAdapter<RequestContext, ResponseContext>());

        assertNotNull(handlerAdapters = handlerChainFactory.getHandlerAdapters());
        assertEquals(2, handlerAdapters.length);
        assertEquals(handlerAdapterA, handlerAdapters[0]);
        assertEquals(handlerAdapterB, handlerAdapters[1]);


        handlerChainFactory.removeHandlerAdapter(handlerAdapterA);
        assertNotNull(handlerAdapters = handlerChainFactory.getHandlerAdapters());
        assertEquals(1, handlerAdapters.length);
        assertEquals(handlerAdapterB, handlerAdapters[0]);

        handlerChainFactory.removeHandlerAdapter(handlerAdapterB);
        assertNotNull(handlerAdapters = handlerChainFactory.getHandlerAdapters());
        assertEquals(0, handlerAdapters.length);

        handlerAdapterList = new ArrayList<HandlerAdapter<? extends RequestContext, ? extends ResponseContext>>();
        handlerAdapterList.add(handlerAdapterA);
        handlerAdapterList.add(handlerAdapterB);

        handlerChainFactory.setHandlerAdapters(handlerAdapterList);

        assertTrue(Arrays.equals(handlerAdapterList.toArray(handlerAdapters), 
                                 handlerChainFactory.getHandlerAdapters()));
    }


    public class MockHandler<S extends RequestContext, T extends ResponseContext> implements Handler<S, T>  {

        public int execute(ProcessingContext<? extends S, ? extends T> processingContext) {
            return 0;
        }

    }

    public class MockHandlerAdapter<S extends RequestContext, T extends ResponseContext> implements HandlerAdapter<S, T> {

        public boolean supports(Object handler) {

            return (handler instanceof MockHandler) ;
        }

        public int execute(ProcessingContext<? extends S, ? extends T> processingContext, Object handler) {

            return ((MockHandler<S, T>) handler).execute(processingContext);
        }

    }
}


