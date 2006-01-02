/*
  org.iterx.miru.pipeline.serializer.HtmlSerializer

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

import com.sun.org.apache.xml.internal.serializer.ToHTMLStream;
import org.iterx.miru.pipeline.Stage;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.stream.StreamResponseContext;


public class HtmlSerializer<S extends RequestContext, T extends StreamResponseContext> extends SerializerImpl<S, T> {


    private ToHTMLStream htmlStream;

    public HtmlSerializer() {

        htmlStream = new ToHTMLStream();
    }

    public String getDoctypePublic() {

        return htmlStream.getDoctypePublic();
    }

    public void setDoctypePublic(String publicId) {

        htmlStream.setDoctypePublic(publicId);
    }

    public String getDoctypeSystem() {

        return htmlStream.getDoctypeSystem();
    }

    public void setDoctypeSystem(String doctypeSystem) {

        htmlStream.setDoctypeSystem(doctypeSystem);
    }


    public String getMediaType() {

        return htmlStream.getMediaType();
    }

    public void setMediaType(String mediaType) {

        htmlStream.setMediaType(mediaType);
    }

    public boolean getIndent() {

        return htmlStream.getIndent();
    }

    public void setIndent(boolean doIndent) {

        htmlStream.setIndent(doIndent);
    }

    public void init() {
        assert (parent != null) : "parent == null";

        parent.setContentHandler(htmlStream);
        parent.setLexicalHandler(htmlStream);

        if(parent instanceof Stage) ((Stage) parent).init();
    }

    public void execute(ProcessingContext<? extends S, ? extends T> processingContext) throws IOException {
        StreamResponseContext responseContext;
        Writer writer;

        responseContext = processingContext.getResponseContext();
        writer = responseContext.getWriter();

        htmlStream.setWriter(writer);
        super.execute(processingContext);
        writer.flush();

        htmlStream.setWriter(null);
    }

}
