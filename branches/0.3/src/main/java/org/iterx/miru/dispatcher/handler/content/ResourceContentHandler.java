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

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.resolver.ResourceResolver;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.StreamTarget;
import org.iterx.util.URIUtils;


public class ResourceContentHandler implements ContentHandler {

    private ResourceResolver resourceResolver;
    private String uri;
    private URI base;


    {
        uri = "{0}";
    }

    public ResourceContentHandler() {};

    public ResourceContentHandler(ResourceResolver resourceResolver) {

        if(resourceResolver == null)
            throw new IllegalArgumentException("resourceResolver == null");
        this.resourceResolver = resourceResolver;
    }

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
        this.uri = uri;
    }

    public URI getBase() {

        return base;
    }

    public void setBase(URI base) {

        this.base = base;
    }

    public ResourceResolver getResourceResolver() {

        return resourceResolver;
    }

    public void setResourceResolver(ResourceResolver resourceResolver) {

        if(resourceResolver == null)
            throw new IllegalArgumentException("resourceResolver == null");
        this.resourceResolver = resourceResolver;
    }


    public int execute(ProcessingContext processingContext) {
        assert (resourceResolver != null) : "resourceResolver == null";

        ResponseContext response;
        RequestContext request;
        Resource resource;
        URI uri;

        response = processingContext.getResponseContext();
        request = processingContext.getRequestContext();
        assert (response instanceof StreamTarget) : "ResponseContext not instanceof StreamSource";

        uri = URIUtils.resolve(this.uri,
                               base,
                               new String[] { (request.getURI()).getPath() });

        if((resource = resourceResolver.resolve(uri)) != null) {
            ReadableByteChannel reader;
            WritableByteChannel writer;

            reader = null;
            writer = null;

            //resource.getContentType();
            //add support for character transcoding...
            try {
                ByteBuffer buffer;
                buffer = ByteBuffer.allocate(8096);

                reader = Channels.newChannel(resource.getInputStream());
                writer = Channels.newChannel(((StreamTarget) response).getOutputStream());
                while(reader.read(buffer) != -1 || buffer.position() > 0) {
                    buffer.flip();
                    writer.write(buffer);
                    buffer.compact();
                }
                return Dispatcher.OK;
            }
            catch(IOException e) {
                //LOG ERROR
                //re-throw error?
            }
            finally {
                if(reader != null)
                    try { reader.close(); } catch(IOException e) {};
                if(writer != null)
                    try { writer.close(); } catch(IOException e) {};
            }
        }

        //throw resource not found exception?
        //or set error on response
        return Dispatcher.ERROR;
    }

}
