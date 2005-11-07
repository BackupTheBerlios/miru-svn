/*
  org.iterx.sax.OutputTarget

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
package org.iterx.sax;

import java.io.Writer;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.ListIterator;

public class OutputTarget implements Iterable {

    private String systemId, publicId, encoding;
    private OutputStream byteStream;
    private Writer characterStream;
    private Iterator iterator;

    public OutputTarget() {}

    public OutputTarget(Iterator iterator) {

        if(iterator == null)
            throw new IllegalArgumentException("iterator == null");
        this.iterator = iterator;
    }

    public OutputTarget(OutputStream byteStream) {

        this.byteStream = byteStream;
    }

    public OutputTarget(OutputStream byteStream, String encoding) {

        this.byteStream = byteStream;
        this.encoding = encoding;
    }

    public OutputTarget(Writer characterStream) {

        this.characterStream = characterStream;
    }

    public OutputTarget(String systemId) {

        this.systemId = systemId;
    }


    public Iterator iterator() {

        if(iterator == null) throw new UnsupportedOperationException();
        return  iterator;
    }

    public OutputStream getByteStream() {

        return byteStream;
    }

    public void setByteStream(OutputStream byteStream) {

        this.byteStream = byteStream;
    }

    public String getEncoding() {

        return encoding;
    }

    public void setEncoding(String encoding) {

        this.encoding = encoding;
    }

    public Writer getCharacterStream() {

    return characterStream;
    }

    public void setCharacterStream(Writer characterStream) {

        this.characterStream = characterStream;
    }

    public String getPublicId() {

        return publicId;
    }

    public void setPublicId(String publicId) {

        this.publicId = publicId;
    }


    public String getSystemId() {

        return systemId;
    }

    public void setSystemId(String systemId) {

        this.systemId = systemId;
    }

}

