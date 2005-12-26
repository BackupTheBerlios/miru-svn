/*
  org.iterx.miru.dispatcher.handler.content.RedirectContentHandler

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

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.event.RedirectEvent;
import org.iterx.miru.matcher.Matcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.util.MiruUtils;

public class RedirectContentHandler<S extends RequestContext, T extends ResponseContext> implements ContentHandler<S, T> {

    private URI baseUri;
    private String uri;

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


    public int execute(ProcessingContext<? extends S, ? extends T> processingContext) {
        URI uri;

        uri = MiruUtils.resolve((this.uri != null)? this.uri : ((processingContext.getRequestContext()).getURI()).getPath(),
                                baseUri,
                                (Matches) processingContext.getAttribute(ProcessingContext.MATCHES_ATTRIBUTE));
        throw new RedirectEvent(uri);
    }
}
