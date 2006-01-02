/*
  org.iterx.miru.context.stream.StreamResponseContextImpl

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

package org.iterx.miru.context.stream;

import java.io.OutputStream;
import java.io.IOException;
import java.io.Writer;

import org.iterx.miru.io.stream.StreamTarget;

public class StreamResponseContextImpl implements StreamResponseContext {

    protected StreamTarget streamTarget;
    protected int status;

    protected StreamResponseContextImpl() {}


    public StreamResponseContextImpl(StreamTarget streamTarget) {

        if(streamTarget == null)
            throw new IllegalArgumentException("streamTarget == null");
        this.streamTarget = streamTarget;
    }

    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {

        this.status = status;
    }

    public String getCharacterEncoding() {

        return streamTarget.getCharacterEncoding();
    }

    public void setCharacterEncoding(String encoding) {

        streamTarget.setCharacterEncoding(encoding);
    }


    public int getContentLength() {

        return streamTarget.getContentLength();
    }

    public void setContentLength(int length) {

        streamTarget.setContentLength(length);
    }

    public String getContentType() {

        return streamTarget.getContentType();
    }

    public void setContentType(String type) {

        streamTarget.setContentType(type);
    }

    public OutputStream getOutputStream() throws IOException {

        return streamTarget.getOutputStream();
    }

    public Writer getWriter() throws IOException {

        return streamTarget.getWriter();
    }

    public void close() throws IOException {

        streamTarget.close();
    }
}
