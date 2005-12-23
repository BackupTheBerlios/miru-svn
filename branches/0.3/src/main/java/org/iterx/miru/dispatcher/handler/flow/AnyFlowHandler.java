package org.iterx.miru.dispatcher.handler.flow;


import org.iterx.miru.dispatcher.handler.FlowHandler;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.matcher.Matcher;

import org.iterx.util.ArrayUtils;

public class AnyFlowHandler implements FlowHandler {

    private Handler[] handlers = new Handler[0];

    public void addHandler(Handler handler) {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");

        handlers = (Handler[]) ArrayUtils.add(handlers, handler);
    }

    public Handler[] getHandlers() {

        return handlers;
    }

    public void setHandlers(Handler[] handlers) {

        if(handlers == null)
            throw new IllegalArgumentException("handlers == null");

        this.handlers = handlers;
    }


    public void removeHandler(Handler handler) {

        handlers = (Handler[]) ArrayUtils.remove(handlers, handler);
    }


    public Matches getMatches(ProcessingContext processingContext) {
        Matches matches;

        matches = null;
        for(int i = handlers.length; i-- > 0; ) {
            Handler handler;

            handler = handlers[i];
            if(handler instanceof Matcher) {
                Matches result;

                if((result = ((Matcher) handler).getMatches(processingContext)) != null) {
                    if(matches == null) matches = result;
                    else matches.put(result);
                }
            }
        }
        return matches;
    }

    public boolean hasMatches(ProcessingContext processingContext) {

        for(int i = 0; i < handlers.length; i++ ) {
            Handler handler;

            handler = handlers[i];
            if(!(handler instanceof Matcher) ||
               ((Matcher) handler).hasMatches(processingContext)) return true;
        }
        return false;
    }


    public int execute(ProcessingContext processingContext) {

        for(int i = 0; i < handlers.length; i++ ) {
            Handler handler;

            handler = handlers[i];
            if(!(handler instanceof Matcher) ||
               ((Matcher) handler).hasMatches(processingContext)) {
                int status;

                status = handler.execute(processingContext);
                if(status == Dispatcher.ERROR ||
                   status == Dispatcher.DONE) return status;
            }

        }
        return Dispatcher.OK;
    }
}
