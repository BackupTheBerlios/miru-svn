package org.iterx.miru.dispatcher.handler.flow;


import java.util.List;

import org.iterx.miru.dispatcher.handler.FlowHandler;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.Status;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.factory.ProcessingContextFactory;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.matcher.Matcher;

import org.iterx.util.ArrayUtils;

public class AnyFlowHandler<S extends RequestContext, T extends ResponseContext>
    implements FlowHandler<S, T>, ApplicationContextAware<S, T> {

    private Handler<S, T>[] handlers = (Handler<S, T>[]) new Object[0];
    private ProcessingContextFactory<S, T> processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();

    public void setApplicationContext(ApplicationContext<? extends S, ? extends T> applicationContext) {

        processingContextFactory = (ProcessingContextFactory<S, T>) applicationContext.getProcessingContextFactory();
    }


    public Handler<S, T>[] getHandlers() {

        return handlers;
    }

    public void setHandlers(List<Handler<? extends S, ? extends T>> handlers) {

        if(handlers == null)
            throw new IllegalArgumentException("handlers == null");

        this.handlers = handlers.toArray(this.handlers);
    }

    public void addHandler(Handler<? extends S, ? extends T> handler) {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");

        handlers = (Handler<S, T>[]) ArrayUtils.add(handlers, handler);
    }


    public void removeHandler(Handler<? extends S, ? extends T> handler) {

        handlers = (Handler<S, T>[]) ArrayUtils.remove(handlers, handler);
    }


    public boolean hasMatches(ProcessingContext<? extends S, ? extends T>  processingContext) {

        for(Handler<S, T> handler : handlers) {
            if(!(handler instanceof Matcher) ||
               ((Matcher<S, T>) handler).hasMatches(processingContext)) return true;
        }
        return false;
    }



    public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        Matches matches;

        matches = null;
        for(Handler<S, T> handler : handlers) {
            if(handler instanceof Matcher) {
                Matches result;

                if((result = ((Matcher<S, T>) handler).getMatches(processingContext)) != null) {
                    if(matches == null) matches = result;
                    else matches.put(result);
                }
            }
        }
        return matches;
    }


    public Status execute(ProcessingContext<? extends S, ? extends T> processingContext) {
        ProcessingContext<S, T> childProcessingContext;
        Status status;

        status = Status.DECLINE;
        childProcessingContext = processingContextFactory.getProcessingContext(processingContext.getRequestContext(),
                                                                               processingContext.getResponseContext());

        for(Handler<S, T> handler : handlers) {
            Matches matches;

            matches = null;
            if(!(handler instanceof Matcher) ||
               (matches = ((Matcher<S , T>) handler).getMatches(processingContext)) != null) {

                childProcessingContext.setAttribute(ProcessingContext.MATCHES_ATTRIBUTE, matches);
                status = handler.execute(processingContext);

                if(status == Status.ERROR ||
                   status == Status.DONE) break;
            }
        }

        return status;
    }
}
