/*
  org.iterx.miru.io.stream.ClassPathStreamResource

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

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class ClassPathStreamResource implements ReadableStreamResource {

    private static final String SCHEME = "classpath";

    protected ClassLoader classLoader;
    private InputStream in;
    private Reader reader;

    private URI uri;

    public ClassPathStreamResource(String uri) {

        this(URI.create(uri));
    }

    public ClassPathStreamResource(String uri, ClassLoader classLoader) {

        this(URI.create(uri), classLoader);
    }

    public ClassPathStreamResource(URI uri) {

        this(uri, null);
    }

    public ClassPathStreamResource(URI uri, ClassLoader classLoader) {
        String scheme;

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
        if((scheme = uri.getScheme()) != null &&
           !(SCHEME.equals(scheme)))
            throw new IllegalArgumentException("Invalid scheme '" + scheme + "'");

        this.classLoader = ((classLoader == null) ?
                            (Thread.currentThread()).getContextClassLoader() :
                            classLoader);
        this.uri = uri;
    }


    public URI getURI() {

        return uri;
    }

    public String getProperty(String key) {

        return null;
    }

    public int getContentLength() {

        return -1;
    }

    public String getContentType() {


        return null;
    }

    public String getCharacterEncoding() {

        return null;
    }

    public InputStream getInputStream() throws IOException {

        if(in != null) return in;
        else if(reader != null) return null;

        return (in = classLoader.getResourceAsStream(uri.getPath()));
    }

    public Reader getReader() throws IOException {

        if(reader != null) return reader;
        else if(in != null) return null;

        return (reader = new InputStreamReader
            (classLoader.getResourceAsStream(uri.getPath())));
    }


    public boolean exists() {

        return ((classLoader.getResourceAsStream(uri.getPath())) != null);
    }


    public void close() throws IOException {

        if(reader != null) reader.close();
        if(in != null) in.close();
    }

    public void reset() {

        if(in != null) {
            try { in.close(); } catch(Exception e) {}
            in = null;
        }
        if(reader != null) {
            try { reader.close(); } catch(Exception e) {}
            reader = null;
        }
    }

    public int hashCode() {

        return uri.hashCode();
    }

    public boolean equals(Object object) {

        return ((this == object) ||
                ((object instanceof ClassPathStreamResource) &&
                 uri.equals(((ClassPathStreamResource) object).uri) &&
                 classLoader.equals((((ClassPathStreamResource) object).classLoader))));

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

    protected void finalize() throws Throwable {

        reset();
        super.finalize();
    }
}
