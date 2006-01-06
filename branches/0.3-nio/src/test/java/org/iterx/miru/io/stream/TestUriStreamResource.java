/*
  org.iterx.miru.io.TestUriStreamResource

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

package org.iterx.miru.io.stream;

import java.io.InputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;


public class TestUriStreamResource extends TestCase {

    private static final String CLASS_URI =
        "org/iterx/miru/io/stream/TestUriStreamResource.class";

    private ReadableStreamResource resource, bogus;
    private URI resourceUri, bogusUri;

    protected void setUp() throws URISyntaxException {
        ClassLoader loader;

        loader = (TestUriStreamResource.class).getClassLoader();
        resourceUri = new URI((loader.getResource(CLASS_URI)).toString());
        resource = new UriStreamResource(resourceUri);
        bogusUri = new URI("file:///.bogus");
        bogus = new UriStreamResource(bogusUri);
    }

    protected void tearDown() {

        resourceUri = null;
        resource = null;
        bogusUri = null;
        bogus = null;
    }

    public void testConstructors() throws URISyntaxException {
        UriStreamResource resource;

        resource = new UriStreamResource(new URI("file:///"));
        assertNotNull(resource);

        try {
            resource = new UriStreamResource((String) null);
            fail("UriStreamResource initialised with null arguments");
        }
        catch(IllegalArgumentException e) {}

        try {
            resource = new UriStreamResource((URI) null);
            fail("UriStreamResource initialised with null arguments");
        }
        catch(IllegalArgumentException e) {}
    }

    public void testPropertyAccessors() {

        assertNotNull(resource.getProperty("Content-Length"));
        assertNotNull(resource.getProperty("content-length"));
        assertNull(resource.getProperty("Bogus-Property"));
    }

    public void testURIAccessors() {

        assertEquals(resourceUri.toString(), (resource.getURI()).toString());
        assertEquals(bogusUri.toString(), (bogus.getURI()).toString());
    }

    public void testInputStreamAccessors() throws IOException {

        assertNotNull(resource.getInputStream());
        assertNull(resource.getReader());

        try {
            bogus.getInputStream();
            fail("Failed to detect invalid stream");
        }
        catch(Exception e){}

        try {
            ReadableStreamResource resource;

            resource = new UriStreamResource(new URI(".bogus"));
            resource.getInputStream();
            fail("Failed to detect relative stream URI");
        }
        catch(Exception e) {}
    }

    public void testReaderAccessors() throws IOException {

        assertNotNull(resource.getReader());
        assertNull(resource.getInputStream());

        try {
            bogus.getReader();
            fail("Failed to detect invalid stream");
        }
        catch(Exception e){}

        try {
            ReadableStreamResource resource;

            resource = new UriStreamResource(new URI(".bogus"));
            resource.getReader();
            fail("Failed to detect relative stream URI");
        }
        catch(Exception e) {}

    }

    public void testReset() throws IOException {
    InputStream a, b;

    assertNotNull((a = resource.getInputStream()));
    resource.reset();
    assertNotNull((b = resource.getInputStream()));
    assertFalse(a == b);
    }

    public void testExists() {

    assertTrue(resource.exists());
    assertFalse(bogus.exists());
    }




}
