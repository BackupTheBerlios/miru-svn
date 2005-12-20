/*
  org.iterx.miru.support.servlet.dispatcher.handler.content.DelegatingContentHandler

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.dispatcher.event.http.HttpErrorEvent;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.support.servlet.context.http.HttpServletRequestContext;
import org.iterx.miru.support.servlet.context.http.HttpServletResponseContext;
import org.iterx.miru.support.servlet.context.ServletApplicationContext;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.util.MiruUtils;



//TODO: Rename to include servlet in name...
public class DelegatingContentHandler implements ContentHandler, ApplicationContextAware {

    private static final Log LOGGER = LogFactory.getLog(DelegatingContentHandler.class);
 
    private ServletContext servletContext;
    private String uri = "{null:0}";

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
        this.uri = uri;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {

        if(applicationContext == null)
            throw new IllegalArgumentException("applicationContext == null");
        servletContext = ((ServletApplicationContext) applicationContext).getServletContext();
    }

    public int execute(ProcessingContext processingContext) {
        assert (servletContext != null) : "servletContext == null";
        HttpServletRequestContext requestContext;
        HttpServletResponseContext responseContext;
        RequestDispatcher dispatcher;
        URI uri;

        requestContext = (HttpServletRequestContext) processingContext.getRequestContext();
        responseContext = (HttpServletResponseContext) processingContext.getResponseContext();

        uri = MiruUtils.resolve(this.uri,
                               null,
                               (Matches) processingContext.getAttribute(ProcessingContext.MATCHES_ATTRIBUTE));


        dispatcher = (("servlet".equals(uri.getScheme()))?
                      servletContext.getNamedDispatcher(uri.getSchemeSpecificPart()) :
                      servletContext.getRequestDispatcher(uri.toString()));

        if(dispatcher != null) {
            try {
                dispatcher.forward(requestContext.getHttpServletRequest(),
                                   responseContext.getHttpResponse());
                return Dispatcher.OK;
            }
            catch(Exception e) {
                LOGGER.error("Processing failure for '"+uri+"'.", e);
                return Dispatcher.ERROR;
            }
        }
        throw new HttpErrorEvent(HttpErrorEvent.NOT_FOUND);
    }


}
