/*
  org.iterx.sax.helpers.XMLWriterImpl

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
package org.iterx.sax.helpers;

import java.io.IOException;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.SAXException;

import org.iterx.sax.XMLWriter;
import org.iterx.sax.OutputTarget;

public class XMLWriterImpl extends XMLFilterImpl implements XMLWriter {
    

    public XMLWriterImpl() {

        super();
    }

    public XMLWriterImpl(XMLReader parent) {

        super(parent);
    }

    public void parse(InputSource input, OutputTarget output) 
        throws IOException, SAXException {
        
        super.parse(input);
    }

}
