/*
  org.iterx.miru.context.HttpRequestContextImpl

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

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URI;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import org.iterx.miru.context.HttpRequestContext;

public class HttpRequestContextImpl implements HttpRequestContext {

    protected Map parameters, properties;
    protected URI uri;

    protected boolean _mutable;

    private InputStream in;
    private Reader reader;

    private String encoding, type;

    {
        _mutable = true;
    }

    protected HttpRequestContextImpl() {
    }

    public HttpRequestContextImpl(InputStream in) {

        this(in, null);
    }

    public HttpRequestContextImpl(Reader reader) {

        this(reader, null);
    }

    public HttpRequestContextImpl(InputStream in, String encoding) {

        parameters = new HashMap();
        properties = new HashMap();
        this.encoding = encoding;
        this.in = in;

    }

    public HttpRequestContextImpl(Reader reader , String encoding) {

        if (encoding == null &&
            reader instanceof InputStreamReader)
            encoding = ((InputStreamReader) reader).getEncoding();

        parameters = new HashMap();
        properties = new HashMap();
        this.encoding = encoding;
        this.reader = reader;
    }

    public URI getURI() {

        return uri;
    }

    public void setURI(URI uri) {
        assert (_mutable) : "Immutable object.";

        this.uri = uri;
    }

    public String getHeader(String name) {

        return (String) properties.get(new CaseInsensitiveKey(name));
    }

    public void setProperty(String name, String value) {

        if (value == null) properties.remove(new CaseInsensitiveKey(name));
        else properties.put(new CaseInsensitiveKey(name), value);
    }

    public String getParameter(String name) {
        String[] values;

        return (((values = (String[]) parameters.get(name)) != null) ?
                values[0] : null);
    }

    public void setParameter(String name, String value) {

        if (value == null) parameters.remove(name);
        else parameters.put(name, new String[]{value});
    }

    public String[] getParameterValues(String name) {

        return (String[]) parameters.get(name);
    }

    public void setParameterValues(String name, String[] value) {
        assert (_mutable) : "Immutable object.";

        if (value == null) parameters.remove(name);
        else parameters.put(name, value);
    }


    public String[] getParameterNames() {
        Set names;

        return (String[]) ((names = parameters.keySet()).toArray
            ((Object[]) new String[names.size()]));
    }

    public int getContentLength() {

        return -1;
    }

    public String getContentType() {

        return type;
    }

    public void setContentType(String type) {
        assert (_mutable) : "Immutable object.";

        this.type = type;
    }

    public String getCharacterEncoding() {

        return encoding;
    }

    public InputStream getInputStream() throws IOException {

        if (_mutable && in != null) {
            _mutable = false;
            reader = null;
        }
        return in;
    }

    public Reader getReader() throws IOException {

        if (_mutable) {
            if (in != null)
                reader = ((encoding != null) ?
                          new InputStreamReader(in, encoding) :
                          new InputStreamReader(in));
            _mutable = false;
            in = null;
        }
        return reader;
    }

    private final class CaseInsensitiveKey {
        private String value;

        private CaseInsensitiveKey(String key) {

            value = key.toLowerCase();
        }

        public int hashCode() {

            return value.hashCode();
        }

        public boolean equals(Object object) {
            String string;

            return (this == object ||
                    (object instanceof CaseInsensitiveKey &&
                     (((string = ((CaseInsensitiveKey) object).value) == value) ||
                      value.equals(value))));
        }
    }
}
    
