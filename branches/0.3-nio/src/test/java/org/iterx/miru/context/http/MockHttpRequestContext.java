/*
  org.iterx.miru.context.context.MockHttpRequestContext

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

package org.iterx.miru.context.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.net.URI;

import org.iterx.miru.io.stream.StreamSourceImpl;

public class MockHttpRequestContext extends HttpRequestContextImpl {

    private MockHttpRequestContext(String uri) {

        this(uri, new byte[0]);
    }

    private MockHttpRequestContext(String uri, byte[] data) {

        super(URI.create(uri),
              new StreamSourceImpl(new ByteArrayInputStream(data)));
    }

    public static MockHttpRequestContext newInstance(String uri) {

        return new MockHttpRequestContext(uri);
    }

    public static MockHttpRequestContext newInstance(String uri, byte[] data) {

        return new MockHttpRequestContext(uri, data);
    }

    public void setURI(String uri) {

        this.uri = URI.create(uri);
    }

    public void reset() {

        try {
            (getInputStream()).reset();
        }
        catch(IOException e) {}
    }


}
