
/*
  org.iterx.miru.io.TestResourceFactoryImpl

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

package org.iterx.miru.io;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.io.ResourceFactoryImpl;

import org.iterx.miru.dispatcher.resolver.ResourceResolver;
import org.iterx.miru.dispatcher.resolver.MockResourceResolver;

public class TestResourceFactoryImpl extends TestCase {

    private static final String URI =
    "org/iterx/miru/io/TestResourceFactoryImpl.class";

    private ResourceFactoryImpl resourceFactory;
    private URI resourceUri, bogusUri;

    protected void setUp() throws URISyntaxException {
    ClassLoader loader;

    loader = (TestResourceFactoryImpl.class).getClassLoader();
    resourceUri = new URI((loader.getSystemResource(URI)).toString());
    bogusUri = new URI("file:///.bogus");

    resourceFactory = new ResourceFactoryImpl();

    }

    protected void tearDown() {

    resourceUri = null;
    bogusUri = null;
    }


    public void testConstructors() {
    ResourceFactoryImpl resourceFactory;

    resourceFactory = new ResourceFactoryImpl();
    assertNotNull(resourceFactory);
    }

    public void testResourceResolverAccessors() {
    MockResourceResolver resourceResolver;
    ResourceResolver[] resourceResolvers;

    resourceResolver = new MockResourceResolver();

    assertNotNull
        (resourceResolvers = resourceFactory.getResourceResolvers());
    assertEquals(0, resourceResolvers.length);

    resourceFactory.addResourceResolver(resourceResolver);
    assertNotNull
        (resourceResolvers = resourceFactory.getResourceResolvers());
    assertEquals(1, resourceResolvers.length);
    assertEquals(resourceResolver, resourceResolvers[0]);

    resourceFactory.removeResourceResolver(resourceResolver);
    assertNotNull
        (resourceResolvers = resourceFactory.getResourceResolvers());
    assertEquals(0, resourceResolvers.length);
    }


    public void testResource() throws URISyntaxException {
    MockResourceResolver resourceResolver;
    MockResource resource;

    resourceFactory.addResourceResolver
        (resourceResolver = new MockResourceResolver());


    assertNull(resourceFactory.getResource(resourceUri));

    resourceResolver.setResource(resource = new MockResource(resourceUri));
    assertEquals(resource, resourceFactory.getResource(resourceUri));

    assertNull(resourceFactory.getResource(bogusUri));

    }

}
