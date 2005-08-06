/*
  org.iterx.miru.pipeline.generator.SaxTransformer

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
package org.iterx.miru.pipeline.transformer;

import org.xml.sax.XMLFilter;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.SAXException;

import org.iterx.miru.pipeline.Stage;
import org.iterx.miru.pipeline.TransformerImpl;

public class SaxTransformer extends TransformerImpl {

    private static final String LEXICAL_HANDLER =
        "http://xml.org/sax/properties/lexical-handler";

    protected XMLFilter xmlFilter;

    public SaxTransformer() {}

    public SaxTransformer(XMLFilter xmlFilter) {
        
        if(xmlFilter == null) 
            throw new IllegalArgumentException("xmlFilter == null");
        this.xmlFilter = xmlFilter;
    }

    public XMLFilter getXMLFilter() {

        return xmlFilter;
    }

    public void setXMLFilter() {

        this.xmlFilter = xmlFilter;
    }   
    
    public void init() {
        assert (parent != null) : "parent == null";
        assert (xmlFilter != null) : "xmlFilter == null";        

        if(xmlFilter instanceof ContentHandler) {
            parent.setContentHandler((ContentHandler) xmlFilter);
            if(contentHandler != null) 
                xmlFilter.setContentHandler(contentHandler);
        }
        if(xmlFilter instanceof LexicalHandler) {
            parent.setLexicalHandler((LexicalHandler) xmlFilter);
            if(lexicalHandler != null) {
                try {
                    xmlFilter.setProperty(LEXICAL_HANDLER, lexicalHandler);
                }
                catch(SAXException e) {}
            }                
        }
        if(parent instanceof Stage)((Stage) parent).init();
    }


    public void destory() {

        if(contentHandler != null)
            xmlFilter.setContentHandler(null);
        if(lexicalHandler != null) {
            try {
                xmlFilter.setProperty(LEXICAL_HANDLER, null);
            }
            catch(SAXException e) {}
        }
        super.destroy();               
    }

    


}
