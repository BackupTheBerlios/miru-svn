/*
  org.iterx.miru.dispatcher.handler.content.ResourceContentHandler;

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

package org.iterx.miru.dispatcher.handler.content;

import java.net.URI;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.stream.StreamRequestContext;
import org.iterx.miru.context.stream.StreamResponseContext;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.StreamTarget;
import org.iterx.miru.io.StreamSource;
import org.iterx.miru.io.factory.ResourceFactory;
import org.iterx.miru.util.MiruUtils;

public class ResourceContentHandler<S extends StreamRequestContext, T extends StreamResponseContext> implements ContentHandler<S, T> {

    private static final Log LOGGER = LogFactory.getLog(ResourceContentHandler.class);
    private static final int BUFFER_SIZE = 8192;

    private ResourceFactory resourceFactory;
    private URI baseUri;

    private String uri;

    public ResourceContentHandler() {}

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        this.uri = uri;
    }

    public URI getBaseUri() {

        return baseUri;
    }

    public void setBaseUri(URI base) {

        this.baseUri = base;
    }

    public ResourceFactory getResourceFactory() {

        return resourceFactory;
    }

    public void setResourceFactory(ResourceFactory resourceFactory) {

        if(resourceFactory == null)
            throw new IllegalArgumentException("resourceFactory == null");
        this.resourceFactory = resourceFactory;
    }


    public int execute(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (resourceFactory != null) : "resourceFactory == null";

        StreamResponseContext responseContext;
        StreamRequestContext requestContext;
        Resource resource;
        URI uri;

        requestContext = processingContext.getRequestContext();
        responseContext = processingContext.getResponseContext();
        uri = MiruUtils.resolve((this.uri != null)? this.uri : (requestContext.getURI()).getPath(),
                                baseUri,
                                (Matches) processingContext.getAttribute(ProcessingContext.MATCHES_ATTRIBUTE));

        if((resource = resourceFactory.getResource(uri)) != null && resource instanceof StreamSource) {
            InputStream in;
            OutputStream out;

            in = null;
            out = null;
            try {
                byte[] buffer;
                int count;

                in = ((StreamSource) resource).getInputStream();
                out = responseContext.getOutputStream();
                buffer = new byte[BUFFER_SIZE];

                while((count = in.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.flush();
                return Dispatcher.OK;
            }
            catch(IOException e) {

            }
            finally {
                if(in != null)
                    try { in.close(); } catch(IOException e) {}
                if(out != null)
                    try { out.close(); } catch(IOException e) {}
            }
        }
        //throw resource not found exception?
        //or set error on responseContext
        LOGGER.warn("Resource '" + uri + "' not found.");
        return Dispatcher.ERROR;
    }



}
