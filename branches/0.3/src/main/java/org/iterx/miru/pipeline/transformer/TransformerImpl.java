/*
  org.iterx.miru.pipeline.transformer.TransformerImpl

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

import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.pipeline.Stage;
import org.iterx.miru.pipeline.XmlProducer;
import org.iterx.miru.pipeline.Transformer;

public class TransformerImpl implements Transformer {

    protected ContentHandler contentHandler;
    protected LexicalHandler lexicalHandler;
    protected XmlProducer parent;
    
    public TransformerImpl() {}

    public TransformerImpl(XmlProducer parent) {

        if(parent == null) 
            throw new IllegalArgumentException("parent == null");

        this.parent = parent;
    }

    public XmlProducer getParent() {
        
        return parent;
    }

    public void setParent(XmlProducer parent) {

        this.parent = parent;
    }

    public void setContentHandler(ContentHandler contentHandler) {

        this.contentHandler = contentHandler;
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler) {

        this.lexicalHandler = lexicalHandler;
    }

    public void init() {
        assert (parent != null) : "parent == null";

        if(parent instanceof Stage) ((Stage) parent).init();
        
        if(contentHandler != null)
            parent.setContentHandler(contentHandler);
        if(lexicalHandler != null)
            parent.setLexicalHandler(lexicalHandler);

    }

    public void execute(ProcessingContext processingContext) throws IOException {
        assert (processingContext != null) : "processingContext == null";

        if(parent instanceof Stage) ((Stage) parent).execute(processingContext);
    }

    public void destroy() {

        if(parent != null && 
           parent instanceof Stage) ((Stage) parent).destroy();

        lexicalHandler = null;
        contentHandler = null;
        parent = null;
    }
    
}
