/*
  org.iterx.miru.support.servlet.dispatcher.handler.content.ServletDispatcherContentHandler

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
package org.iterx.miru.support.servlet.dispatcher.handler.content;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.Status;
import org.iterx.miru.dispatcher.event.http.HttpErrorEvent;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.support.servlet.context.http.HttpServletRequestContext;
import org.iterx.miru.support.servlet.context.http.HttpServletResponseContext;
import org.iterx.miru.support.servlet.context.ServletApplicationContext;
import org.iterx.miru.support.servlet.util.ServletDispatcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.util.MiruUtils;


public class ServletDispatcherContentHandler<S extends HttpServletRequestContext, T extends HttpServletResponseContext> implements ContentHandler<S, T> {

    private static final Log LOGGER = LogFactory.getLog(ServletDispatcherContentHandler.class);

    private ServletDispatcher servletDispatcher;
    private String uri;

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
        this.uri = uri;
    }

    public ServletDispatcher getServletDispatcher() {

        return servletDispatcher;
    }
    public void setServletDispatcher(ServletDispatcher servletDispatcher) {

        this.servletDispatcher = servletDispatcher;
    }

    public Status execute(ProcessingContext<? extends S, ? extends T>  processingContext) {
        assert (servletDispatcher != null) : "servletDispatcher == null";
        HttpServletRequestContext requestContext;
        URI uri;

        requestContext =  processingContext.getRequestContext();
        uri = MiruUtils.resolve((this.uri != null)? this.uri : (requestContext.getURI()).getPath(),
                                null,
                                (Matches) processingContext.getAttribute(ProcessingContext.MATCHES_ATTRIBUTE));

        return servletDispatcher.include(uri, processingContext);
    }

}
