/*
  org.iterx.miru.dispatcher.handler.content.ResourceContentHandler;

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

package org.iterx.miru.dispatcher.handler.content;

import java.util.Random;
import java.util.Arrays;
import java.net.URI;

import junit.framework.TestCase;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.io.factory.ResourceFactory;
import org.iterx.miru.io.factory.ResourceFactoryImpl;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.MockProcessingContext;
import org.iterx.miru.context.http.MockHttpRequestContext;
import org.iterx.miru.context.http.MockHttpResponseContext;
import org.iterx.miru.resolver.MockResourceResolver;
import org.iterx.miru.dispatcher.Dispatcher;

public class TestResourceContentHandler extends TestCase {

    private static final byte[] DATA = new byte[10000];
    private static final URI BASE = URI.create("/");
    private static final String PATH = "/";

    {
        (new Random()).nextBytes(DATA);
    }

    public void testConstructors() {
        ResourceContentHandler contentHandler;

        assertNotNull(contentHandler = new ResourceContentHandler());
        assertNull(contentHandler.getResourceFactory());
    }

    public void testUriAccessors() {
        ResourceContentHandler contentHandler;

        contentHandler = new ResourceContentHandler();
        assertEquals("{0}", contentHandler.getUri());

        contentHandler.setUri(PATH);
        assertEquals(PATH, contentHandler.getUri());

        try {
            contentHandler.setUri(null);
            fail("URI is null.");
        }
        catch(Exception e){}
    }


    public void testBaseAccessors() {
        ResourceContentHandler contentHandler;

        contentHandler = new ResourceContentHandler();
        assertNull(contentHandler.getBaseUri());

        contentHandler.setBaseUri(BASE);
        assertEquals(BASE, contentHandler.getBaseUri());

        contentHandler.setBaseUri(null);
        assertNull(contentHandler.getBaseUri());
    }

    public void testResourceFactoryAccessors() {
        ResourceContentHandler contentHandler;
        ResourceFactory resourceFactory;

        resourceFactory = ResourceFactory.getResourceFactory();

        contentHandler = new ResourceContentHandler();
        assertNull(contentHandler.getResourceFactory());

        contentHandler.setResourceFactory(resourceFactory);
        assertEquals(resourceFactory, contentHandler.getResourceFactory());

        try {
            contentHandler.setResourceFactory(null);
            fail("ResourceFactory is null.");
        }
        catch(Exception e){}

    }


    public void testHandler() throws Exception {
        ProcessingContext processingContext;
        ResourceContentHandler contentHandler;
        ResourceFactoryImpl resourceFactory;
        MockHttpResponseContext response;
        MockResourceResolver resolver;
        MockResource source;

        processingContext = new MockProcessingContext
            (MockHttpRequestContext.newInstance("/"),
             response = MockHttpResponseContext.newInstance());
        resourceFactory = new ResourceFactoryImpl();

        resolver = new MockResourceResolver();
        resolver.setResource(source = new MockResource(URI.create(PATH)));
        resourceFactory.addResourceResolver(resolver);

        contentHandler = new ResourceContentHandler();
        contentHandler.setResourceFactory(resourceFactory);

        source.setData(DATA);
        assertEquals(Dispatcher.OK, contentHandler.execute(processingContext));
        assertTrue(Arrays.equals(DATA, response.getData()));


        source.reset();
        response.reset();
        contentHandler.setUri(PATH);
        assertEquals(Dispatcher.OK, contentHandler.execute(processingContext));
        assertTrue(Arrays.equals(DATA, response.getData()));

        contentHandler.setUri("does-not-exist");
        assertEquals(Dispatcher.ERROR, contentHandler.execute(processingContext));
    }

}
