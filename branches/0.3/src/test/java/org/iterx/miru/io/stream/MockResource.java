/*
  org.iterx.miru.io.MockResource

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

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.net.URI;

public class MockResource implements ReadableStreamResource {

    private String type, encoding;
    private InputStream in;
    private Reader reader;
    private URI uri;

    private byte[] data = new byte[0];

    public MockResource() {}

    public MockResource(URI uri) {

        this.uri = uri;
    }

    public URI getURI() {

        return uri;
    }

    public void setURI(URI uri) {

        this.uri = uri;
    }

    public byte[] getData() {

        return data;
    }

    public void setData(byte[] data) {

        if(data == null)
            throw new IllegalArgumentException("data == null");

        this.data = data;
        reset();
    }


    public boolean exists() {

        return true;
    }

    public void reset() {

        reader = null;
        in = null;
    }


    public String getProperty(String property) {

        return null;
    }

    public int getContentLength() {

        return data.length;
    }

    public String getContentType() {

        return type;
    }

    public void setContentType(String type) {

        this.type = type;
    }

    public String getCharacterEncoding() {

        return encoding;
    }

    public void setCharacterEncoding(String encoding) {

        this.encoding = encoding;
    }

    public InputStream getInputStream() throws IOException {

        if(reader != null) return null;
        else if(in == null)
            in = new ByteArrayInputStream(data);

        return in;
    }

    public Reader getReader() throws IOException {

        if(in != null) return null;
        else if(reader == null) {
            reader =
                ((encoding != null)?
                 new InputStreamReader(new ByteArrayInputStream(data), encoding) :
                 new InputStreamReader(new ByteArrayInputStream(data)));
        }
        return reader;
    }


    public void close() throws IOException {
        if(reader != null) reader.close();
        if(in != null) in.close();
    }
}
                                     