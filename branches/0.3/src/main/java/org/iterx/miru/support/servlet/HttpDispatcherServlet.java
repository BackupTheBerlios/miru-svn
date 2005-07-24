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
import org.iterx.miru.support.servlet.context.HttpServletRequestContext;
import org.iterx.miru.support.servlet.context.HttpServletResponseContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpDispatcherServlet extends HttpServlet {

    protected Log logger = LogFactory.getLog(HttpDispatcherServlet.class);

    private ProcessingContextFactory processingContextFactory;
    private Dispatcher dispatcher;

    {
        processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
    }

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

    public void init(ServletConfig servletConfig) throws ServletException {}


    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws ServletException, IOException {
        assert (dispatcher != null) : "dispatcher == null";
        assert (processingContextFactory != null) : "processingContextFactory == null";

        try {
            ProcessingContext processingContext;
            HttpServletResponseContext responseContext;
            HttpServletRequestContext requestContext;

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
            logger.error("Failed to process [" + request.getRequestURL() + "]", e);
            throw new ServletException
                ("Request " + request.getRequestURL() + "failed.", e);
        }
    }
}
