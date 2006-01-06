/*
  org.iterx.miru.resolver.stream.TestUriStreamResourceResolver

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

package org.iterx.miru.resolver.resource;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.iterx.miru.io.Resource;

public class TestUriStreamResourceResolver extends TestCase {

    private static final String PATH =
        "org/iterx/miru/resolver/resource/TestUriStreamResourceResolver.class";

    private URI absoluteUri, relativeUri, baseUri;


    protected void setUp() throws URISyntaxException {
        ClassLoader loader;
        String absolute;

        loader = (TestUriStreamResourceResolver.class).getClassLoader();
        absolute = (loader.getResource(PATH)).toString();
        absoluteUri = new URI(absolute);
        relativeUri = new URI(PATH);
        baseUri = new URI(absolute.substring
            (0, absolute.length() - PATH.length()));
    }

    protected void tearDown() {

        absoluteUri = relativeUri = baseUri = null;
    }

    public void testConstructors() {
        UriStreamResourceResolver resourceResolver;

        resourceResolver = new UriStreamResourceResolver();
        assertNotNull(resourceResolver);

        resourceResolver = new UriStreamResourceResolver(baseUri);
        assertNotNull(resourceResolver);
        assertEquals(baseUri, resourceResolver.getBaseUri());

        try {
            resourceResolver = new UriStreamResourceResolver(null);
            fail("UriStreamResourceResolver initialised with null arguments");
        }
        catch(IllegalArgumentException e) {}

        try {
            resourceResolver = new UriStreamResourceResolver(relativeUri);
            fail("UriStreamResourceResolver initialised with relative uri");
        }
        catch(IllegalArgumentException e) {}
    }

    public void testBaseAccessors() {
        UriStreamResourceResolver resourceResolver;

        resourceResolver = new UriStreamResourceResolver();

        assertEquals(UriStreamResourceResolver.BASE_URI,
                     resourceResolver.getBaseUri());

        resourceResolver.setBaseUri(baseUri);
        assertEquals(baseUri,
                     resourceResolver.getBaseUri());
        try {
            resourceResolver.setBaseUri(null);
            fail("Base uri set to null");
        }
        catch(IllegalArgumentException e) {}

        try {
            resourceResolver.setBaseUri(relativeUri);
            fail("Base uri set to a relative uri");
        }
        catch(IllegalArgumentException e) {}
    }

    public void testResolve() {
        UriStreamResourceResolver resourceResolver;
        Resource resourceA, resourceB;

        resourceResolver = new UriStreamResourceResolver();

        assertNotNull(resourceResolver.resolve(absoluteUri));
        assertNull(resourceResolver.resolve(relativeUri));
        resourceResolver.setBaseUri(baseUri);
        assertNotNull((resourceA = resourceResolver.resolve(absoluteUri)));
        assertNotNull((resourceB = resourceResolver.resolve(relativeUri)));
        assertEquals(resourceA, resourceB);
    }

}
