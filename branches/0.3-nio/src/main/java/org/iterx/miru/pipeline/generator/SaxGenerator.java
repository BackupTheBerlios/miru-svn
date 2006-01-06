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

import java.io.IOException;

import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.stream.StreamRequestContext;
import org.iterx.miru.pipeline.PipelineChainException;
import org.iterx.miru.pipeline.util.SaxUtils;

public class SaxGenerator<S extends StreamRequestContext, T extends ResponseContext> extends GeneratorImpl<S, T> {

    private static final String LEXICAL_HANDLER =
          "context://xml.org/sax/properties/lexical-handler";

    protected XMLReader xmlReader;

    public SaxGenerator() {}

    public SaxGenerator(XMLReader xmlReader) {

        if(xmlReader == null)
            throw new IllegalArgumentException("xmlReader == null");
        this.xmlReader = xmlReader;
    }

    public XMLReader getXMLReader() {

        return xmlReader;
    }

    public void setXMLReader(XMLReader xmlReader) {

        this.xmlReader = xmlReader;
    }

    public void init() {
        assert (xmlReader != null) : "xmlReader == null";

        if(contentHandler != null)
            xmlReader.setContentHandler(contentHandler);
        if(lexicalHandler != null) {
            try {
                xmlReader.setProperty(LEXICAL_HANDLER, lexicalHandler);
            }
            catch(SAXException e) {}
        }
        super.init();
    }

    public void execute(ProcessingContext<? extends S, ? extends T> processingContext) throws IOException {
        assert (xmlReader != null) : "xmlReader == null";

        try {
            StreamRequestContext requestContext;

            requestContext = processingContext.getRequestContext();
            xmlReader.parse(SaxUtils.newInputSource(requestContext));
        }
        catch(SAXException e) {
            throw new PipelineChainException("Pipeline execution failure", e);
        }
    }

    public void destroy() {
        assert (xmlReader != null) : "xmlReader == null";

        if(contentHandler != null)
            xmlReader.setContentHandler(null);
        if(lexicalHandler != null) {
            try {
                xmlReader.setProperty(LEXICAL_HANDLER, null);
            }
            catch(SAXException e) {}
        }
        super.destroy();
    }


}
