/*
  org.iterx.miru.support.servlet.HttpDispatcherServlet

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
package org.iterx.miru.support.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ProcessingContextFactory;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.dispatcher.context.DispatcherApplicationContext;
import org.iterx.miru.dispatcher.handler.HandlerChainFactory;
import org.iterx.miru.support.servlet.context.http.HttpServletRequestContext;
import org.iterx.miru.support.servlet.context.http.HttpServletResponseContext;
import org.iterx.miru.support.servlet.dispatcher.context.ServletDispatcherApplicationContext;
import org.iterx.miru.io.Loadable;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.resource.UriResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpDispatcherServlet extends HttpServlet {

    private static final Log LOGGER = LogFactory.getLog(HttpDispatcherServlet.class);

    private ProcessingContextFactory processingContextFactory;
    private Dispatcher dispatcher;

    public HttpDispatcherServlet() {}

    public HttpDispatcherServlet(Dispatcher dispatcher) {

        if(dispatcher == null)
            throw new IllegalArgumentException("dispatcher == null");
        this.dispatcher = dispatcher;
    }

    public Dispatcher getDispatcher() {

        return dispatcher;
    }

    public void setDispatcher(Dispatcher dispatcher) {

        if(dispatcher == null)
            throw new IllegalArgumentException("dispatcher == null");
        this.dispatcher = dispatcher;
    }

    public ProcessingContextFactory getProcessingContextFactory() {

        return processingContextFactory;
    }

    public void setProcessingContextFactory(ProcessingContextFactory processingContextFactory) {

        if(processingContextFactory != null)
            throw new IllegalArgumentException("processingContextFactory == null");
        this.processingContextFactory = processingContextFactory;
    }

    public void init(ServletConfig servletConfig) throws ServletException {

        try {
            DispatcherApplicationContext applicationContext;
            HandlerChainFactory handlerChainFactory;
            ServletContext servletContext;
            String parameter;

            servletContext = servletConfig.getServletContext();

            applicationContext = new ServletDispatcherApplicationContext(servletContext);
            if((parameter = servletConfig.getInitParameter(ServletDispatcherApplicationContext.BEANS)) != null &&
               applicationContext instanceof Loadable) {
                Resource resource;

                resource = new UriResource((servletContext.getResource(parameter)).toURI());
                ((Loadable) applicationContext).load(resource);
            }

            handlerChainFactory = applicationContext.getHandlerChainFactory();
            if((parameter = servletConfig.getInitParameter(ServletDispatcherApplicationContext.CHAINS)) != null &&
               handlerChainFactory instanceof Loadable) {
                    Resource resource;

                resource = new UriResource((servletContext.getResource(parameter)).toURI());
                ((Loadable) handlerChainFactory).load(resource);
            }

            dispatcher = (Dispatcher) applicationContext.getBeanOfType(Dispatcher.class);
            dispatcher.setHandlerChainMap(handlerChainFactory.getHandlerChains());
            processingContextFactory = applicationContext.getProcessingContextFactory();
        }
        catch(Exception e) {
            LOGGER.error("Initialisation failed.", e);
            throw new ServletException("Initialisation failed.", e);
        }
    }


    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws ServletException, IOException {
        assert (dispatcher != null) : "dispatcher == null";
        assert (processingContextFactory != null) : "processingContextFactory == null";

        try {
            HttpServletResponseContext responseContext;
            HttpServletRequestContext requestContext;
            ProcessingContext processingContext;

            processingContext = processingContextFactory.getProcessingContext
                (requestContext = new HttpServletRequestContext(request),
                 responseContext = new HttpServletResponseContext(response));

            int status = dispatcher.dispatch(processingContext);
            switch(status) {
                case Dispatcher.DONE:
                    response.flushBuffer();
                case Dispatcher.OK:
                    break;
                case Dispatcher.DECLINE:
                    if(responseContext.getStatus() == HttpServletResponseContext.REDIRECT) {
                        ServletContext servletContext;
                        String uri;

                        if((uri = responseContext.getHeader("location")) != null)
                            uri = (requestContext.getURI()).getPath();

                        servletContext = getServletConfig().getServletContext();
                        (servletContext.getRequestDispatcher(uri)).forward(request, response);
                        break;
                    }
                    break;
                case Dispatcher.ERROR:
                    break;
                default:
                    break;
            };
        }
        catch(Exception e) {
            String url;

            url = (request.getRequestURL()).toString();
            LOGGER.error("Failed to process [" + url + "]", e);
            throw new ServletException("Request [" + url + "] failed.", e);
        }
    }
}
