/*
  org.iterx.miru.context.ProcessingContext

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

package org.iterx.miru.context;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;

public class ProcessingContext {
    protected RequestContext request;
    protected ResponseContext response;

    private Map attributes;

    private ProcessingContext() {
    }

    public ProcessingContext(RequestContext request,
                             ResponseContext response) {
        if (request == null)
            throw new IllegalArgumentException("request == null");
        if (response == null)
            throw new IllegalArgumentException("response == null");

        this.request = request;
        this.response = response;

        attributes = new HashMap();
    }

    public ProcessingContext(ProcessingContext processingContext) {

        if (processingContext == null)
            throw new IllegalArgumentException("processingContext == null");
        this.request = processingContext.request;
        this.response = processingContext.response;

        attributes = new HashMap();
    }


    public RequestContext getRequestContext() {

        return request;
    }

    public void setRequestContext(RequestContext request) {

        if (request == null)
            throw new IllegalArgumentException("request == null");
        this.request = request;
    }

    public ResponseContext getResponseContext() {

        return response;
    }

    public void setResponseContext(ResponseContext response) {

        if (response == null)
            throw new IllegalArgumentException("response == null");
        this.response = response;
    }

    public Object getAttribute(String name) {

        return attributes.get(name);
    }

    public String[] getAttributeNames() {
        Set names;

        return (String[]) ((names = attributes.keySet()).toArray
            ((Object[]) new String[names.size()]));
    }

    public void setAttribute(String name, Object object) {

        if (object == null) attributes.remove(name);
        else attributes.put(name, object);
    }

}
