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
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.resolver.ResourceResolver;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.StreamTarget;
import org.iterx.miru.io.StreamSource;
import org.iterx.miru.io.factory.ResourceFactory;
import org.iterx.util.URIUtils;

public class ResourceContentHandler implements ContentHandler {

    private static final Log LOGGER = LogFactory.getLog(ResourceContentHandler.class);

    private ResourceFactory resourceFactory;
    private URI baseUri;

    private String uri = "{0}";

    public ResourceContentHandler() {}

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
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


    public int execute(ProcessingContext processingContext) {
        assert (resourceFactory != null) : "resourceFactory == null";

        ResponseContext response;
        RequestContext request;
        Resource resource;
        URI uri;

        response = processingContext.getResponseContext();
        request = processingContext.getRequestContext();
        assert (response instanceof StreamTarget) : "ResponseContext not instanceof StreamSource";

        uri = URIUtils.resolve(this.uri,
                               baseUri,
                               new String[] { (request.getURI()).getPath() });

        if((resource = resourceFactory.getResource(uri)) != null &&
           resource instanceof StreamSource) {

            InputStream in;
            OutputStream out;

            in = null;
            out = null;
            try {
                byte[] buffer;
                int count;

                in = ((StreamSource) resource).getInputStream();
                out = ((StreamTarget) response).getOutputStream();
                buffer = new byte[8192];

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
        //or set error on response
        LOGGER.warn("Resource '" + uri + "' not found.");
        return Dispatcher.ERROR;
    }



}
