/*
  org.iterx.sax.InputSource

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

import java.io.Reader;
import java.io.InputStream;
import java.util.Iterator;

public class InputSource extends org.xml.sax.InputSource {

    private Iterator iterator;

    public InputSource() {}

    public InputSource(Iterator iterator) {

        if(iterator == null)
            throw new IllegalArgumentException("iterator == null");
        this.iterator = iterator;
    }

    public InputSource(InputStream byteStream) {

        super(byteStream);
    }

    public InputSource(InputStream byteStream, String encoding) {

        super(byteStream);
        setEncoding(encoding);
    }

    public InputSource(Reader characterStream) {

        super(characterStream);
    }

    public InputSource(String systemId) {

        super(systemId);
    }

    public Iterator getIterator() {

        return iterator;
    }

    public void setIterator(Iterator iterator) {

        this.iterator = iterator;
    }

}

