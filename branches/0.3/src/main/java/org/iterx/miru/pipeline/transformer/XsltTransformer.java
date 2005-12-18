/*
  org.iterx.miru.pipeline.generator.XsltTransformer

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

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.HashMap;

import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;


import org.iterx.miru.pipeline.Stage;
import org.iterx.miru.pipeline.PipelineChainException;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.io.factory.ResourceFactory;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.ReadableResource;
import org.iterx.util.URIUtils;

public class XsltTransformer extends TransformerImpl {

    private static SAXTransformerFactory transformerFactory;
    private static Map templates;

    private TransformerHandler transformerHandler;

    private SaxHandlerProxy saxHandlerProxy;
    private SAXResult saxResult;

    private ResourceFactory resourceFactory;
    private String uri;

    static {
        transformerFactory = (SAXTransformerFactory) TransformerFactory.newInstance();
        templates = new HashMap();
    }

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
        this.uri = uri;
    }

    public ResourceFactory getResourceFactory() {

        return resourceFactory;
    }

    public void setResourceFactory(ResourceFactory resourceFactory) {

        if(resourceFactory == null)
            throw new IllegalArgumentException("resourceFactory == null");
        this.resourceFactory = resourceFactory;
    }

    public void init() {
        assert (parent != null) : "parent == null";

        saxHandlerProxy = new SaxHandlerProxy();
        parent.setContentHandler(saxHandlerProxy);
        parent.setLexicalHandler(saxHandlerProxy);

        saxResult = new SAXResult();
        if(contentHandler != null) saxResult.setHandler(contentHandler);
        if(lexicalHandler != null) saxResult.setLexicalHandler(lexicalHandler);

        if(parent instanceof Stage) ((Stage) parent).init();
    }


    public void execute(ProcessingContext processingContext) throws IOException {
        assert (processingContext != null) : "processingContext == null";
        assert (resourceFactory != null) : "resourceFactory == null";
        assert (uri != null) : "uri == null";

        try {
            Templates template;
            URI uri;

            uri = URIUtils.resolve(this.uri,
                                   null,
                                   new String[] {});

            if((template = (Templates) templates.get(uri)) == null) {
                Resource resource;

                if((resource = resourceFactory.getResource(uri)) == null ||
                   !(resource instanceof ReadableResource))
                    throw new PipelineChainException("Invalid template [" + uri + "].");
                template = transformerFactory.newTemplates
                    (new StreamSource(((ReadableResource) resource).getReader()));

                synchronized(templates) {
                    templates.put(uri, template);
                }
            }
            transformerHandler = transformerFactory.newTransformerHandler(template);
            transformerHandler.setResult(saxResult);

            super.execute(processingContext);
        }
        catch(TransformerConfigurationException e) {
            throw new PipelineChainException("Transformer failure.", e);
        }
    }

     public void destroy() {

         transformerHandler = null;
         saxHandlerProxy = null;
         saxResult = null;
         super.destroy();
    }



    private class SaxHandlerProxy implements ContentHandler, LexicalHandler {

        public void characters(char[] ch, int start, int length) throws SAXException {

            transformerHandler.characters(ch, start, length);
        }

        public void endDocument() throws SAXException {

            transformerHandler.endDocument();
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {

            transformerHandler.endElement(uri, localName, qName);
        }

        public void endPrefixMapping(String prefix) throws SAXException {

            transformerHandler.endPrefixMapping(prefix);
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

            transformerHandler.ignorableWhitespace(ch, start, length);
        }

        public void processingInstruction(String target, String data) throws SAXException {

            transformerHandler.processingInstruction(target, data);
        }

        public void setDocumentLocator(Locator locator) {

            transformerHandler.setDocumentLocator(locator);
        }

        public void skippedEntity(String name) throws SAXException {

            transformerHandler.skippedEntity(name);
        }

        public void startDocument() throws SAXException {

            transformerHandler.startDocument();
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

            transformerHandler.startElement(uri, localName, qName, atts);
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {

            transformerHandler.startPrefixMapping(prefix, uri);
        }

        public void comment(char[] ch, int start, int length) throws SAXException {

            transformerHandler.comment(ch, start, length);
        }

        public void endCDATA() throws SAXException {

            transformerHandler.endCDATA();
        }

        public void endDTD() throws SAXException {

            transformerHandler.endDTD();
        }

        public void endEntity(String name) throws SAXException {

            transformerHandler.endEntity(name);
        }

        public void startCDATA() throws SAXException {

            transformerHandler.startCDATA();
        }

        public void startDTD(String name, String publicId, String systemId) throws SAXException {

            transformerHandler.startDTD(name, publicId, systemId);
        }

        public void startEntity(String name) throws SAXException {

            transformerHandler.startEntity(name);
        }

    }
}
