/*
  org.iterx.miru.io.stream.UriStreamResource

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

import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class UriStreamResource implements ReadableStreamResource {

    protected URLConnection connection;

    private InputStream in;
    private Reader reader;

    private URI uri;

    public UriStreamResource(String uri) {
        if(uri == null)
            throw new IllegalArgumentException("uri == null");

        this.uri = URI.create(uri);
    }

    public UriStreamResource(URI uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");

        this.uri = uri;
    }


    public URI getURI() {

        return uri;
    }

    public String getProperty(String key) {

        if(connection == null) init();

        return connection.getHeaderField(key);
    }

    public int getContentLength() {

        if(connection == null) init();

        return connection.getContentLength();
    }

    public String getContentType() {

        if(connection == null) init();

        return connection.getContentType();
    }

    public String getCharacterEncoding() {

        if(connection == null) init();

        return connection.getContentEncoding();
    }

    public InputStream getInputStream() throws IOException {

        if(in != null) return in;
        else if(reader != null) return null;
        else if(connection == null) init();

        return (in = connection.getInputStream());
    }

    public Reader getReader() throws IOException {
        String encoding;

        if(reader != null) return reader;
        else if(in != null) return null;
        else if(connection == null) init();

        encoding = connection.getContentEncoding();
        reader = ((encoding != null)?
                  new InputStreamReader(connection.getInputStream(), encoding) :
                  new InputStreamReader(connection.getInputStream()));

        return reader;
    }


    public boolean exists() {

        if(connection != null) return true;
        else {
            try {
                init();
            }
            catch(Exception e) {}
        }

        return (connection != null);
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
        connection = null;
    }

    public void close() throws IOException {

        if(reader != null) reader.close();
        if(in != null) in.close();
    }

    public int hashCode() {

        return uri.hashCode();
    }

    public boolean equals(Object object) {

        return ((this == object) ||
                ((object instanceof UriStreamResource) &&
                 uri.equals(((UriStreamResource) object).uri)));
    }

    public String toString() {
        StringBuilder buffer;
        String cls;

        buffer = new StringBuilder();
        cls = (getClass().getName());
        buffer.append(cls.substring(1 + cls.lastIndexOf('.')));
        buffer.append('[');
        buffer.append("uri=[");
        buffer.append(uri);
        buffer.append("]]");
        return buffer.toString();
    }

    protected void init() {

        if(connection == null) {
            try {
                URL url;

                url = uri.toURL();
                connection = url.openConnection();
                connection.connect();
            }
            catch(Exception e){
                connection = null;
                throw new RuntimeException("Invalid stream [" + uri + "]");
            }
        }
    }

    protected void finalize() throws Throwable {

        reset();
        super.finalize();
    }
}
