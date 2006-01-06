package org.iterx.miru.support.servlet.util;

import java.net.URI;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.support.servlet.context.ServletApplicationContext;
import org.iterx.miru.support.servlet.context.http.HttpServletRequestContext;
import org.iterx.miru.support.servlet.context.http.HttpServletResponseContext;
import org.iterx.miru.dispatcher.Status;
import org.iterx.miru.dispatcher.event.http.HttpErrorEvent;


public class ServletDispatcher implements ApplicationContextAware {

    private static final Log LOGGER = LogFactory.getLog(ServletDispatcher.class);
    private static enum TYPE { FORWARD, INCLUDE }

    private ServletContext servletContext;

    public ServletDispatcher() {}

    public void setApplicationContext(ApplicationContext applicationContext) {

        if(applicationContext == null)
            throw new IllegalArgumentException("applicationContext == null");
        servletContext = ((ServletApplicationContext) applicationContext).getServletContext();
    }

    public Status forward(URI uri, ProcessingContext<? extends HttpServletRequestContext, ? extends HttpServletResponseContext> processingContext) {

        return dispatch(uri, processingContext, TYPE.FORWARD);
    }

    public Status include(URI uri, ProcessingContext<? extends HttpServletRequestContext, ? extends HttpServletResponseContext> processingContext) {

        return dispatch(uri, processingContext, TYPE.INCLUDE);
    }

    private Status dispatch(URI uri,
                            ProcessingContext<? extends HttpServletRequestContext, ? extends HttpServletResponseContext> processingContext,
                            TYPE type) {
        HttpServletRequestContext requestContext;
        HttpServletResponseContext responseContext;
        HttpServletRequest request;
        HttpServletResponse response;
        RequestDispatcher dispatcher;

        requestContext =  processingContext.getRequestContext();
        responseContext =  processingContext.getResponseContext();

        request = requestContext.getHttpServletRequest();
        response = responseContext.getHttpServletResponse();

        if("servlet".equals(uri.getScheme()))
            dispatcher = servletContext.getNamedDispatcher(uri.getSchemeSpecificPart());
        else {
            String path, context;

            path = uri.getPath();
            context = request.getContextPath();
            dispatcher = servletContext.getRequestDispatcher((path.startsWith(context))? path.substring(context.length()) : path);
        }

        if(dispatcher != null) {
            try {
                String[] attributes;

                attributes = processingContext.getAttributeNames();
                for(int i = attributes.length; i-- > 0; ) {
                    String attribute;

                    attribute = attributes[i];
                    request.setAttribute(attribute, processingContext.getAttribute(attribute));
                }

                switch(type) {
                    case FORWARD:
                        dispatcher.forward(request, response);
                        break;
                    case INCLUDE:
                        dispatcher.include(request, response);
                        break;
                }
                return Status.OK;
            }
            catch(Exception e) {
                LOGGER.error("Processing failure for '" + uri + "'", e);
                return Status.ERROR;
            }
        }
        throw new HttpErrorEvent(HttpErrorEvent.NOT_FOUND);
    }
}
