/*
  org.iterx.miru.pipeline.generator.SaxGenerator

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
package org.iterx.miru.pipeline.generator;

import java.io.InputStream;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.iterx.miru.io.StreamSource;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.pipeline.GeneratorImpl;

public class SaxGenerator extends GeneratorImpl {

    private static final String LEXICAL_HANDLER =
        "http://xml.org/sax/properties/lexical-handler";

    protected static SAXParserFactory saxParserFactory;
    protected XMLReader xmlReader;
    
    private InputSource inputSource;

    static {
        saxParserFactory = SAXParserFactory.newInstance();
    }
    
    public SaxGenerator() {}

    public SaxGenerator(XMLReader xmlReader) {
        
        if(xmlReader == null) 
            throw new IllegalArgumentException("xmlReader == null");
        this.xmlReader = xmlReader;
    }

    public XMLReader getXMLReader() {

        return xmlReader;
    }

    public void setXMLReader() {

        this.xmlReader = xmlReader;
    }

    public void init(ProcessingContext processingContext) {
        if(xmlReader == null) {
            try {
                SAXParser saxParser;
                
                saxParser = saxParserFactory.newSAXParser();
                xmlReader = saxParser.getXMLReader();                
            }
            catch(Exception e) {
                throw new RuntimeException
                    ("Failed to initialise Generator.", e);
            }
        }
        try {
            StreamSource streamSource;
            InputStream in;
        
            streamSource = (StreamSource) 
                processingContext.getRequestContext();
            if((in = streamSource.getInputStream()) != null) {
                inputSource = new InputSource(in);
            inputSource.setEncoding(streamSource.getCharacterEncoding());
            }
            else inputSource = new InputSource(streamSource.getReader());
        }
        catch(Exception e) {
            throw new RuntimeException
                ("Failed to initialise stream source.", e);
        }
        if(contentHandler != null)
            xmlReader.setContentHandler(contentHandler);
        if(lexicalHandler != null) {
            try {
                xmlReader.setProperty(LEXICAL_HANDLER, lexicalHandler);
            }
            catch(SAXException e) {}
        }

        super.init(processingContext);
    }

    public void execute() throws IOException {
        assert (xmlReader != null) : "xmlReader == null";
        assert (inputSource != null) : "Invalid source.";

        try {
	    xmlReader.parse(inputSource);
	}
	catch(SAXException e) {	    
	    throw new RuntimeException
                ("Pipeline execution failure.", e);
	}
    }

    public void reset() {
        
        if(contentHandler != null)            
            xmlReader.setContentHandler(null);
        if(lexicalHandler != null) {
            try {
                xmlReader.setProperty(LEXICAL_HANDLER, null);
            }
            catch(SAXException e) {}
        }
        super.reset();
    }

    public void destroy() {

        reset();
    }
    
}
