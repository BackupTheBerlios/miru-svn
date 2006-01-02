/*
  org.iterx.miru.context.context.HttpResponseContextImpl

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

import java.util.Map;
import java.util.HashMap;

import org.iterx.util.CaseInsensitiveKey;
import org.iterx.miru.context.stream.StreamResponseContextImpl;
import org.iterx.miru.io.stream.StreamTarget;

public class HttpResponseContextImpl extends StreamResponseContextImpl
    implements HttpResponseContext {

    protected Map<CaseInsensitiveKey, String> headers;

    protected HttpResponseContextImpl() {}

    public HttpResponseContextImpl(StreamTarget streamTarget) {

        super(streamTarget);
        headers = new HashMap<CaseInsensitiveKey, String>(2);
        status = OK;
    }

    public String getHeader(String name) {

        return headers.get(new CaseInsensitiveKey(name));
    }

    public void setHeader(String name, String value) {

        if (value == null) headers.remove(new CaseInsensitiveKey(name));
        else headers.put(new CaseInsensitiveKey(name), value);
    }

}
