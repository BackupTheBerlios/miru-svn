/*
  org.iterx.miru.pipeline.serializer.XmlSerializer

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
import java.io.Writer;


import com.sun.org.apache.xml.internal.serializer.ToXMLStream;


import org.iterx.miru.io.StreamTarget;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.stream.StreamResponseContext;
import org.iterx.miru.pipeline.Stage;

public class XmlSerializer<S extends RequestContext, T extends StreamResponseContext> extends SerializerImpl<S, T> {

    private ToXMLStream xmlStream;

    public XmlSerializer() {

        xmlStream = new ToXMLStream();
    }

    public String getDoctypePublic() {

        return xmlStream.getDoctypePublic();
    }

    public void setDoctypePublic(String publicId) {

        xmlStream.setDoctypePublic(publicId);
    }

    public String getDoctypeSystem() {

        return xmlStream.getDoctypeSystem();
    }

    public void setDoctypeSystem(String doctypeSystem) {

        xmlStream.setDoctypeSystem(doctypeSystem);
    }

    public boolean getOmitXMLDeclaration() {

        return xmlStream.getOmitXMLDeclaration();
    }

    public void setOmitXMLDeclaration(boolean omitXMLDeclaration) {

        xmlStream.setOmitXMLDeclaration(omitXMLDeclaration);
    }

    public String getMediaType() {

        return xmlStream.getMediaType();
    }

    public void setMediaType(String mediaType) {

        xmlStream.setMediaType(mediaType);
    }

    public boolean getIndent() {

        return xmlStream.getIndent();
    }

    public void setIndent(boolean doIndent) {

        xmlStream.setIndent(doIndent);
    }

    public void init() {
        assert (parent != null) : "parent == null";

        parent.setContentHandler(xmlStream);
        parent.setLexicalHandler(xmlStream);

        if(parent instanceof Stage) ((Stage) parent).init();
    }

    public void execute(ProcessingContext<? extends S, ? extends T> processingContext) throws IOException {
        StreamResponseContext responseContext;
        Writer writer;

        responseContext = processingContext.getResponseContext();
        writer = responseContext.getWriter();

        xmlStream.setWriter(writer);
        super.execute(processingContext);
        writer.flush();

        xmlStream.setWriter(null);

    }

}
