/*
  org.iterx.miru.io.StreamSourceImpl

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

import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;

public class StreamSourceImpl implements StreamSource {

    private InputStream rawInputStream, inputStream;
    private Reader rawReader, reader;

    private String encoding;
    private String type;

    private int length = -1;

    public StreamSourceImpl(InputStream inputStream) {

        this(inputStream, null);
    }

    public StreamSourceImpl(InputStream inputStream, String encoding) {

        if(inputStream == null)
            throw new IllegalArgumentException("inputStream == null");
        this.rawInputStream = inputStream;
        this.encoding = encoding;
    }

    public StreamSourceImpl(Reader reader) {

        this(reader, null);
    }

    public StreamSourceImpl(Reader reader, String encoding) {

        if(reader == null)
            throw new IllegalArgumentException("reader == null");

        if(encoding == null && reader instanceof InputStreamReader)
            encoding = ((InputStreamReader) reader).getEncoding();
        this.rawReader = reader;
        this.encoding = encoding;
    }

    public int getContentLength() {

        return length;
    }

    public void setContentLength(int length) {

        if(length < -1)
            throw new IllegalArgumentException("length < -1");
        this.length = length;
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

          if(inputStream == null) {
              if(reader != null || rawInputStream == null) return null;
              inputStream = rawInputStream;
          }
          return inputStream;
    }

    public Reader getReader() throws IOException {

        if(reader == null) {
            if(inputStream != null || (rawInputStream == null && rawReader == null)) return null;
            if(rawReader == null) {
                reader = ((encoding != null) ?
                          new InputStreamReader(rawInputStream, encoding) :
                          new InputStreamReader(rawInputStream));
            }
            else reader = rawReader;
        }
        return reader;
    }

    public void close() throws IOException {

        try {
            InputStream inputStream;
            Reader reader;

            if((reader = getReader()) != null) reader.close();
            if((inputStream = getInputStream()) != null) inputStream.close();
        }
        finally{
            reader = rawReader = null;
            inputStream = rawInputStream = null; 
        }

    }


}
