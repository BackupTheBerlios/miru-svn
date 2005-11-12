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

import junit.framework.TestCase;
import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.dispatcher.handler.factory.HandlerChainFactoryImpl;
import org.iterx.miru.dispatcher.handler.factory.HandlerChainFactory;
import org.iterx.miru.dispatcher.handler.HandlerChainMap;
import org.iterx.miru.context.ProcessingContext;


public class TestHandlerChainMapFactoryImpl extends TestCase {

    public void testConstructors() {
        HandlerChainFactory handlerChainFactory;

        handlerChainFactory = new HandlerChainFactoryImpl();
        assertNotNull(handlerChainFactory);
    }

    public void testHandlerChainAccessors() {
        HandlerChainFactory handlerChainFactory;
        HandlerChainMap handlerChainMap;

        handlerChainFactory = new HandlerChainFactoryImpl();
        assertNotNull(handlerChainMap = handlerChainFactory.getHandlerChains());
        assertFalse((handlerChainMap.iterator()).hasNext());
    }


    public void testHandlerAdapterAccessors() {
        HandlerChainFactory handlerChainFactory;
        HandlerAdapter[] handlerAdapters;
        HandlerAdapter handlerAdapterA, handlerAdapterB;

        handlerChainFactory = new HandlerChainFactoryImpl();

        assertNotNull(handlerAdapters = handlerChainFactory.getHandlerAdapters());
        assertEquals(0, handlerAdapters.length);

        handlerChainFactory.addHandlerAdapter(handlerAdapterA = new MockHandlerAdapter());

        assertNotNull(handlerAdapters = handlerChainFactory.getHandlerAdapters());
        assertEquals(1, handlerAdapters.length);
        assertEquals(handlerAdapterA, handlerAdapters[0]);

        handlerChainFactory.addHandlerAdapter(handlerAdapterB = new MockHandlerAdapter());

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

        handlerChainFactory.setHandlerAdapters
            (handlerAdapters = new HandlerAdapter[] { handlerAdapterA, handlerAdapterB });
        assertEquals(handlerAdapters, handlerChainFactory.getHandlerAdapters());
    }


    public class MockHandler {

        public int process(ProcessingContext processingContext) {

            return Dispatcher.OK;
        }

    }

    public class MockHandlerAdapter implements HandlerAdapter {

        public boolean supports(Object handler) {

            return (handler instanceof MockHandler) ;
        }
        
        public int execute(ProcessingContext processingContext, Object handler) {

            return ((MockHandler) handler).process(processingContext);
        }

    }
}


