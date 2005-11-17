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

import java.io.IOException;

import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

import org.iterx.sax.XMLWriter;
import org.iterx.sax.InputSource;

import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.ProcessingContext;

import org.iterx.miru.pipeline.Stage;
import org.iterx.miru.pipeline.PipelineChainException;
import org.iterx.miru.pipeline.XmlProducer;
import org.iterx.miru.pipeline.XmlConsumer;
import org.iterx.miru.pipeline.generator.SaxGenerator;
import org.iterx.miru.pipeline.helper.SaxHelper;

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


        if(parent instanceof Stage) {
            xmlWriter.setParent(new StageXmlFilterAdapter((Stage) parent));
            ((Stage) parent).init();
        }

    }

    public void execute(ProcessingContext processingContext)
        throws IOException {
        assert (xmlWriter != null) : "xmlWriter == null";

        try {
            ResponseContext responseContext;

            responseContext = processingContext.getResponseContext();
            xmlWriter.parse(new ProcessingContextInputSource(processingContext),
                            SaxHelper.newOutputTarget(responseContext));
        }
        catch(Exception e) {
            throw new PipelineChainException
                ("Pipeline execution failure.", e);
        }
    }

    private static class ProcessingContextInputSource extends InputSource {

        private ProcessingContext processingContext;

        private ProcessingContextInputSource(ProcessingContext processingContext) {

            this.processingContext = processingContext;
        }
    }

    //TODO: Need to add getParent wrapper for all Sax type stages.    
    private static class StageXmlFilterAdapter extends XMLFilterImpl {

        private Stage stage;

        private StageXmlFilterAdapter(Stage stage) {

            this.stage = stage;
        }

        public XMLReader getParent() {

            XmlProducer parent = (XmlProducer) stage;
            while(true) {
                if(parent instanceof SaxGenerator)
                    return ((SaxGenerator) parent).getXMLReader();
                else if (!(parent instanceof XmlConsumer) ||
                         (parent = ((XmlConsumer) parent).getParent()) == null) break;
            }

            return null;
        }

        public void parse(org.xml.sax.InputSource inputSource)
            throws IOException, SAXException {

            stage.execute
                (((ProcessingContextInputSource) inputSource).processingContext);
        }

    }


}
