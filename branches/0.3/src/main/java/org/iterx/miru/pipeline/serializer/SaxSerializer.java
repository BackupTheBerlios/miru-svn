/*
  org.iterx.miru.pipeline.serializer.SaxSerializer

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
package org.iterx.miru.pipeline.serializer;

import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

import org.iterx.sax.XMLWriter;
import org.iterx.sax.InputSource;
import org.iterx.sax.OutputTarget;

import org.iterx.miru.io.StreamTarget;

import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.ProcessingContext;

import org.iterx.miru.pipeline.Stage;
import org.iterx.miru.pipeline.SerializerImpl;
import org.iterx.miru.pipeline.PipelineException;

public class SaxSerializer extends SerializerImpl {
    
    private static final String LEXICAL_HANDLER =
        "http://xml.org/sax/properties/lexical-handler";

    protected XMLWriter xmlWriter;

    public SaxSerializer() {}

    public SaxSerializer(XMLWriter xmlWriter) {

        if(xmlWriter == null) 
            throw new IllegalArgumentException("xmlWriter == null");
        this.xmlWriter = xmlWriter;        
    }
    
    public XMLWriter getXMLWriter() {

        return xmlWriter;
    }

    public void setXMLWriter(XMLWriter xmlWriter) {

        this.xmlWriter = xmlWriter;
    }   
    
    
    public void init() {
        assert (parent != null) : "parent == null";
        assert (xmlWriter != null) : "xmlWriter == null";        

        if(xmlWriter instanceof ContentHandler)
            parent.setContentHandler((ContentHandler) xmlWriter);
        if(xmlWriter instanceof LexicalHandler)
            parent.setLexicalHandler((LexicalHandler) xmlWriter);

        xmlWriter.setParent(new XmlFilterCallback());
        if(parent instanceof Stage)((Stage) parent).init();

    }  

    public void execute(ProcessingContext processingContext) 
        throws IOException {
        assert (xmlWriter != null) : "xmlWriter == null";

        //write wrapper for parent to pass through details
        //        xmlWriter.setOutputTarget(outputTarget);
        //
        //xmlWriter.setOutputTarget(null);

        try {
            ResponseContext responseContext;
            OutputTarget outputTarget;
            
            responseContext = processingContext.getResponseContext();
            outputTarget = new OutputTarget(responseContext);
            if(responseContext instanceof StreamTarget) {
                StreamTarget streamTarget;
                OutputStream out;

                streamTarget = (StreamTarget) responseContext; 
                if((out = streamTarget.getOutputStream()) != null) {
                    outputTarget.setByteStream(out);
                    outputTarget.setEncoding
                        (streamTarget.getCharacterEncoding());
                }
                else outputTarget.setCharacterStream(streamTarget.getWriter());
            }


            xmlWriter.parse(new InputSource(processingContext), 
                            outputTarget);
        }
        catch(Exception e) {
            throw new PipelineException
                ("Pipeline execution failure.", e);
        }
    }    

    private class XmlFilterCallback extends XMLFilterImpl {

        public void parse(org.xml.sax.InputSource inputSource) 
            throws IOException, SAXException {

            if(parent instanceof Stage) 
                ((Stage) parent).execute
                    ((ProcessingContext)
                     ((InputSource) inputSource).getObject());
        }

    }


}
