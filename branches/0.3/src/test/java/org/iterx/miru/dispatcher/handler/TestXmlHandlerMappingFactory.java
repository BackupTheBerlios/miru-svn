/*
  org.iterx.miru.dispatcher.handler.TestXmlHandlerMappingFactory

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

import junit.framework.TestCase;

import org.iterx.miru.dispatcher.handler.HandlerMapping;
import org.iterx.miru.dispatcher.handler.HandlerMappingImpl;
import org.iterx.miru.dispatcher.handler.XmlHandlerMappingFactory;
import org.iterx.miru.dispatcher.handler.XmlHandlerMappingParser;

public class TestXmlHandlerMappingFactory extends TestCase {

    private static final String SITEMAP_NS =
    XmlHandlerMappingParser.SITEMAP_NS;


    public void testConstructors() {
    XmlHandlerMappingFactory handlerMappingFactory;
    HandlerMapping handlerMapping;

    handlerMappingFactory = new XmlHandlerMappingFactory();
    assertNotNull(handlerMappingFactory.getHandlerMapping());

        handlerMappingFactory = new XmlHandlerMappingFactory
            (handlerMapping = new HandlerMappingImpl());
        assertEquals(handlerMapping,
                     handlerMappingFactory.getHandlerMapping());

    try {
            handlerMappingFactory = new XmlHandlerMappingFactory(null);
        fail("XmlHandlerMappingFactory initialised with null arguments");
    }
    catch(IllegalArgumentException e) {}

    }

    /*
    public void testHandlerMappingAccessors() {
	XmlHandlerMappingFactory handlerMappingFactory;
	HandlerMapping handlerMapping;

	handlerMappingFactory = new XmlHandlerMappingFactory();
	assertNotNull
	    ((handlerMapping = handlerMappingFactory.getHandlerMapping()));
	assertTrue(handlerMapping instanceof HandlerMappingImpl);

	handlerMapping = new HandlerMappingImpl();
	handlerMappingFactory.setHandlerMapping(handlerMapping);
	assertEquals(handlerMapping, 
		     handlerMappingFactory.getHandlerMapping());
	try {
	    handlerMappingFactory.setHandlerMapping(null);
	    fail("HandlerMapping is null");
	}
	catch(IllegalArgumentException e) {}
    }
    
    public void testLoad() throws IOException {
        XmlHandlerMappingFactory handlerMappingFactory;
        HandlerInterceptor[] handlerInterceptors;
        HandlerMapping handlerMapping;
        MockResource resource;       
        Handler handler;
        String id;
            

        resource = new MockResource();
        handlerMappingFactory = new XmlHandlerMappingFactory
            (handlerMapping = new HandlerMappingImpl() {
                    public void addHandler(String id, 
                                           Handler handler) {
                        throw new CallbackException
                            (new Object[] { id, handler});
                    }

                    public void addHandler(String id, 
                                           Handler handler,
                                           HandlerInterceptor[] handlerInterceptors) {
                        
                        throw new CallbackException
                            (new Object[] { id, 
                                            handler,
                                            handlerInterceptors});
                    }
                });

        resource.setContent("<document/>".getBytes());
        handlerMappingFactory.load(resource);

        resource.setContent(("<document>" +
                             "<map xmlns=\"" + SITEMAP_NS + "\"/>" +
                             "</document>").getBytes());
        handlerMappingFactory.load(resource);


        try {
            resource.setContent(("<document>" +
                                 "<map xmlns=\"" + SITEMAP_NS + "\">" +
                                 "<chain id=\"" + (id = "id" ) + "\">" +
                                 "</chain>" +
                                 "</map>" +
                                 "</document>").getBytes());
            handlerMappingFactory.load(resource);
            fail("Invalid chain definition.");
        }
        catch(Exception e) {}

        

    }


    public void testLoadInvalid() throws IOException {
        XmlHandlerMappingFactory handlerMappingFactory;
        MockResource resource;

        resource = new MockResource();
        handlerMappingFactory = new XmlHandlerMappingFactory();
        
        try {
            handlerMappingFactory.load(null);
            fail("Failed to detect invalid resource.");
        }
        catch(IllegalArgumentException e) {}

        try {
            resource.setContent("".getBytes()); 
            handlerMappingFactory.load(resource);
            fail("Failed to detect invalid xml stream.");
        }
        catch(IOException e) {}        
    }
    
    private class CallbackException extends RuntimeException {
        private Object[] objects;
        
        private CallbackException(Object[] objects) {
            
            this.objects = objects;
        }
        private Object[] getObjects() {

            return objects;
        }

    }
    */
}
