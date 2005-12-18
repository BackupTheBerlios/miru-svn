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
import java.net.URL;
import java.net.URI;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;

import org.iterx.miru.io.Loadable;
import org.iterx.miru.io.resource.UriResource;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.factory.ProcessingContextFactory;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.dispatcher.event.RedirectEvent;
import org.iterx.miru.dispatcher.event.ErrorEvent;
import org.iterx.miru.dispatcher.context.DispatcherApplicationContext;
import org.iterx.miru.dispatcher.handler.factory.HandlerChainFactory;
import org.iterx.miru.support.servlet.context.http.HttpServletRequestContext;
import org.iterx.miru.support.servlet.context.http.HttpServletResponseContext;
import org.iterx.miru.support.servlet.dispatcher.context.ServletDispatcherApplicationContext;

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
            ApplicationContext parentApplicationContext;
            HandlerChainFactory handlerChainFactory;
            ServletContext servletContext;
            String parameter;

            servletContext = servletConfig.getServletContext();

            parentApplicationContext =
                (ApplicationContext) servletContext.getAttribute((DispatcherApplicationContext.class).getName());
            applicationContext = ((parentApplicationContext != null)?
                                  new ServletDispatcherApplicationContext(parentApplicationContext, servletContext) :
                                  new ServletDispatcherApplicationContext(servletContext));

            if((parameter = servletConfig.getInitParameter(ServletDispatcherApplicationContext.BEANS)) != null &&
               applicationContext instanceof Loadable) {
                URL url;

                if((url = (servletContext.getResource(parameter))) != null)
                    ((Loadable) applicationContext).load(new UriResource(url.toURI()));
                else throw new IOException("Invalid resource ["+  parameter + "]");
            }

            handlerChainFactory = applicationContext.getHandlerChainFactory();
            if((parameter = servletConfig.getInitParameter(ServletDispatcherApplicationContext.CHAINS)) != null &&
               handlerChainFactory instanceof Loadable) {
                URL url;

                if((url = (servletContext.getResource(parameter))) != null)
                    ((Loadable) handlerChainFactory).load(new UriResource(url.toURI()));
                else throw new IOException("Invalid resource ["+  parameter + "]");
            }

            if(dispatcher == null &&
               (dispatcher = (Dispatcher) applicationContext.getBeanOfType(Dispatcher.class)) == null)
                dispatcher = new Dispatcher();

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
                    requestContext.close();
                    responseContext.close();
                    break;
                case Dispatcher.OK:
                case Dispatcher.DECLINE:
                    break;
                case Dispatcher.ERROR:
                    responseContext.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    break;
                default:
                    break;
            };
        }
        catch(RedirectEvent redirect)  {
            URI uri;

            if((uri = redirect.getURI()).getScheme() == null) {
                ServletContext servletContext;
                servletContext = (getServletConfig().getServletContext());

                (servletContext.getRequestDispatcher(uri.getPath())).forward(request, response);
            }
            else response.sendRedirect(uri.toString());
        }
        catch(ErrorEvent error)  {
            String url;

            url = (request.getRequestURL()).toString();
            LOGGER.info("Error processing [" + url + "]", error);
            response.setStatus(error.getStatus());
        }
        catch(Exception e) {
            String url;

            url = (request.getRequestURL()).toString();
            LOGGER.error("Failed to process [" + url + "]", e);
            throw new ServletException("Request [" + url + "] failed.", e);
        }
    }
}
