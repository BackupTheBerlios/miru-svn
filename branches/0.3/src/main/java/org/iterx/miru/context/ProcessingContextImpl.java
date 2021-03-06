/*
  org.iterx.miru.context.ProcessingContextImpl

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

import org.iterx.miru.resolver.ContextResolver;

public class ProcessingContextImpl<S extends RequestContext, T extends ResponseContext> implements ProcessingContext<S, T> {

    protected S request;
    protected T response;

    private Map<String, Object> attributes;

    private ProcessingContextImpl() {}

    public ProcessingContextImpl(S request,
                                 T response) {
        if (request == null)
            throw new IllegalArgumentException("request == null");
        if (response == null)
            throw new IllegalArgumentException("response == null");

        this.request = request;
        this.response = response;

        attributes = new HashMap<String, Object>();
    }
    public ProcessingContextImpl(ProcessingContext<? extends S, ? extends T> processingContext) {

        if (processingContext == null)
            throw new IllegalArgumentException("processingContext == null");
        this.request = processingContext.getRequestContext();
        this.response = processingContext.getResponseContext();

        attributes = new HashMap<String, Object>();
    }

    public S getRequestContext() {

        return request;
    }

    public void setRequestContext(S request) {

        if (request == null)
            throw new IllegalArgumentException("request == null");
        this.request = request;
    }

    public T getResponseContext() {

        return response;
    }

    public void setResponseContext(T response) {

        if (response == null)
            throw new IllegalArgumentException("response == null");
        this.response = response;
    }

    public Object getAttribute(String name) {
        Object value;

        if((value = attributes.get(name)) instanceof ContextResolver)
            value = ((ContextResolver<?, S, T>) value).resolve(this);

        return value;
    }

    public String[] getAttributeNames() {
        Set<String> names;

        names = attributes.keySet();
        return names.toArray(new String[names.size()]);
    }

    public void setAttribute(String name, Object value) {

        if (value == null) attributes.remove(name);
        else attributes.put(name, value);
    }

}
