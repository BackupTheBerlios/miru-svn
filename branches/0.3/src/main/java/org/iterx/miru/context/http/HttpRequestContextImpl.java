/*
  org.iterx.miru.context.context.HttpRequestContextImpl

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

import java.net.URI;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.iterx.util.CaseInsensitiveKey;
import org.iterx.miru.context.stream.StreamRequestContextImpl;
import org.iterx.miru.io.stream.StreamSource;


public class HttpRequestContextImpl extends StreamRequestContextImpl
    implements HttpRequestContext {

    protected Map<CaseInsensitiveKey, String> headers;
    protected Map<String, Object> parameters;

    protected HttpRequestContextImpl() {}

    public HttpRequestContextImpl(URI uri, StreamSource streamSource) {

        super(uri, streamSource);
        headers = new HashMap<CaseInsensitiveKey, String>(8);
        parameters = new HashMap<String, Object>(2);
    }

    public String getHeader(String name) {

        return headers.get(new CaseInsensitiveKey(name));
    }

    public void setHeader(String name, String value) {

        if (value == null) headers.remove(new CaseInsensitiveKey(name));
        else headers.put(new CaseInsensitiveKey(name), value);
    }

    public String getParameter(String name) {
        String[] values;

        return (((values = getParameterValues(name)) != null) ? values[0] : null);
    }

    public void setParameter(String name, String value) {

        if(value == null) parameters.remove(name);
        else parameters.put(name, new String[]{ value });
    }

    public String[] getParameterValues(String name) {
        Object object;
        String[] values;

        object = parameters.get(name);
        if(object instanceof String[]) values = (String[]) object;
        else if(object == null) values = null;
        else {
            List<String> list;

            values = (list = (List<String>) object).toArray(new String[list.size()]);
            parameters.put(name, values);
        }
        return values;
    }


    public void setParameterValues(String name, List<String> values) {

        if(values == null) parameters.remove(name);
        else parameters.put(name, values);
    }

    public void addParameterValue(String name, String value) {
        Object object;
        List<String> list;

        if((object = parameters.get(name)) != null)
            list = (object instanceof List)? (List<String>) object : Arrays.asList((String[]) object);
        else parameters.put(name, list = new ArrayList<String>(2));
        list.add(value);
    }


    public void removeParameterValue(String name, String value) {
        Object object;

        if((object = parameters.get(name)) != null) {
            ArrayList<String> list;

            list = (object instanceof List)? (ArrayList<String>) object : new ArrayList<String>(Arrays.asList((String[]) object));
            list.remove(value);
            if(list.size() == 0) parameters.remove(name);
        }
    }

    public String[] getParameterNames() {
        Set<String> names;

        return  ((names = parameters.keySet()).toArray(new String[names.size()]));
    }


}

    
