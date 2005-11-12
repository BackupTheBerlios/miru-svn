/*
  org.iterx.miru.io.resource.UriResource

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

package org.iterx.miru.io.resource;

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import org.iterx.miru.io.ReadableResource;

public class UriResource implements ReadableResource {

    protected URLConnection _connection;

    private InputStream in;
    private Reader reader;

    private URI uri;

    public UriResource(String uri) {
        if(uri == null)
            throw new IllegalArgumentException("uri == null");

        this.uri = URI.create(uri);
    }

    public UriResource(URI uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");

        this.uri = uri;
    }


    public URI getURI() {

        return uri;
    }

    public String getProperty(String key) {

        if(_connection == null) init();

        return _connection.getHeaderField(key);
    }

    public int getContentLength() {

        if(_connection == null) init();

        return _connection.getContentLength();
    }

    public String getContentType() {

        if(_connection == null) init();

        return _connection.getContentType();
    }

    public String getCharacterEncoding() {

        if(_connection == null) init();

        return _connection.getContentEncoding();
    }

    public InputStream getInputStream() throws IOException {

        if(in != null) return in;
        else if(reader != null) return null;
        else if(_connection == null) init();

        return (in = _connection.getInputStream());
    }

    public Reader getReader() throws IOException {
        String encoding;

        if(reader != null) return reader;
        else if(in != null) return null;
        else if(_connection == null) init();



        encoding = _connection.getContentEncoding();
        reader = ((encoding != null)?
                  new InputStreamReader(_connection.getInputStream(), encoding) :
                  new InputStreamReader(_connection.getInputStream()));

        return reader;
    }


    public boolean exists() {

        if(_connection != null) return true;
        else {
            try {
                init();
            }
            catch(Exception e) {}
        }

        return (_connection != null);
    }

    public void reset() {

        if(in != null) {
            try {
                in.close();
        }
            catch(Exception e){}
            in = null;
        }
        if(reader != null) {
            try {
                reader.close();
            }
            catch(Exception e){}
            reader = null;
        }
        _connection = null;
    }

    public int hashCode() {

        return uri.hashCode();
    }

    public boolean equals(Object object) {

        return ((this == object) ||
                ((object instanceof UriResource) &&
                 uri.equals(((UriResource) object).uri)));
    }

    public String toString() {
        StringBuffer buffer;
        String cls;

        buffer = new StringBuffer();
        cls = (getClass().getName());
        buffer.append(cls.substring(1 + cls.lastIndexOf('.')));
        buffer.append('[');
        buffer.append("uri=[");
        buffer.append(uri);
        buffer.append("]]");
        return buffer.toString();
    }

    protected void init() {

        if(_connection == null) {
            try {
                URL url;

                url = uri.toURL();
                _connection = url.openConnection();
                _connection.connect();
            }
            catch(Exception e){
                _connection = null;
                throw new RuntimeException("Invalid resource [" +
                                           uri + "]");
            }
        }
    }

    protected void finalize() throws Throwable {

        reset();
        super.finalize();
    }
}
