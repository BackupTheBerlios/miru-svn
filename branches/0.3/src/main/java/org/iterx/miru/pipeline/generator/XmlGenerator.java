/*
  org.iterx.miru.pipeline.generator.XmlGenerator

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

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.stream.StreamRequestContext;
import org.iterx.miru.pipeline.util.SaxUtils;
import org.iterx.miru.pipeline.PipelineChainException;

public class XmlGenerator<S extends StreamRequestContext, T extends ResponseContext> extends GeneratorImpl<S, T> {

    private static final String LEXICAL_HANDLER =
          "http://xml.org/sax/properties/lexical-handler";

    private static final String VALIDATION =
        "http://xml.org/sax/features/validation";

    private static final String NAMESPACES =
        "http://xml.org/sax/features/namespaces";

    private static final String NAMESPACE_PREFIXES =
        "http://xml.org/sax/features/namespace-prefixes";


    private static SAXParserFactory saxParserFactory;

    private XMLReader xmlReader;

    static {
        saxParserFactory = SAXParserFactory.newInstance();
    }

    public XmlGenerator() {
        try {
            SAXParser saxParser;

            saxParser = saxParserFactory.newSAXParser();
            xmlReader = saxParser.getXMLReader();
            xmlReader.setFeature(NAMESPACES, true);
            xmlReader.setFeature(NAMESPACE_PREFIXES, false);
        }
        catch(Exception e) {
            throw new RuntimeException
                ("Failed to initialise Generator.", e);
        }
    }

    public boolean getValidation() {

        try {
            return xmlReader.getFeature(VALIDATION);
        }
        catch(SAXException e) {
            throw new IllegalArgumentException("Validation not supported");
        }
    }

    public void setValidation(boolean validation) {

        try {
            xmlReader.setFeature(VALIDATION, validation);
        }
        catch(SAXException e) {
            throw new IllegalArgumentException("Validation not supported");
        }
    }

    public boolean getNamespaces() {

        try {
            return xmlReader.getFeature(NAMESPACES);
        }
        catch(SAXException e) {
            throw new RuntimeException("Non-compliant XMLReader implementation");
        }
    }

    public void setNamespaces(boolean namespaces) {
        try {
            xmlReader.setFeature(NAMESPACES, namespaces);
        }
        catch(SAXException e) {
            throw new RuntimeException("Non-compliant XMLReader implementation");
        }
    }
    public boolean getNamespacePrefixes() {

        try {
            return xmlReader.getFeature(NAMESPACE_PREFIXES);
        }
        catch(SAXException e) {
            throw new RuntimeException("Non-compliant XMLReader implementation");
        }
    }

    public void setNamespacePrefixes(boolean namespacePrefixes) {
        try {
            xmlReader.setFeature(NAMESPACE_PREFIXES, namespacePrefixes);
        }
        catch(SAXException e) {
            throw new RuntimeException("Non-compliant XMLReader implementation");
        }
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
