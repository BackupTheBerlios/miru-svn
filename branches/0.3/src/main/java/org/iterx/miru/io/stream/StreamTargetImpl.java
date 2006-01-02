/*
  org.iterx.miru.io.StreamTargetImpl

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

import java.io.OutputStream;
import java.io.Writer;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class StreamTargetImpl implements StreamTarget {

    private OutputStream rawOutputStream, outputStream;
    private Writer rawWriter, writer;

    private String encoding;
    private String type;

    private int length = -1;

       public StreamTargetImpl(OutputStream outputStream) {

        this(outputStream, null);
    }

    public StreamTargetImpl(OutputStream outputStream, String encoding) {

        if(outputStream == null)
            throw new IllegalArgumentException("outputStream == null");
        this.rawOutputStream = outputStream;
        this.encoding = encoding;
    }

    public StreamTargetImpl(Writer writer) {

        this(writer, null);
    }

    public StreamTargetImpl(Writer writer, String encoding) {

        if(writer == null)
            throw new IllegalArgumentException("writer == null");

        if(encoding == null && writer instanceof OutputStreamWriter)
            encoding = ((OutputStreamWriter) writer).getEncoding();
        this.rawWriter = writer;
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

    public OutputStream getOutputStream() throws IOException {

          if(outputStream == null) {
              if(writer != null || rawOutputStream == null) return null;
              outputStream = rawOutputStream;
          }
          return outputStream;
      }

      public Writer getWriter() throws IOException {

          if(writer == null) {
              if(outputStream != null || (rawOutputStream == null && rawWriter == null)) return null;
              if(rawWriter == null) {
                  writer = ((encoding != null) ?
                            new OutputStreamWriter(rawOutputStream, encoding) :
                            new OutputStreamWriter(rawOutputStream));
              }
              else writer = rawWriter;
          }
          return writer;
      }


    public void close() throws IOException {

        try {
            Writer writer;
            OutputStream outputStream;

            if((writer = getWriter()) != null) writer.close();
            if((outputStream = getOutputStream()) != null) outputStream.close();
        }
        finally {
            writer = rawWriter = null;
            outputStream = rawOutputStream = null;
        }
    }
}
