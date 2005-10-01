package org.iterx.miru.dispatcher.handler;

import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.dispatcher.matcher.Matcher;
import org.iterx.miru.context.ProcessingContext;

public class HandlerChainImpl implements HandlerChain {

    private String id;
    private Matcher matcher;

    private Handler[] handlers;

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Matcher getMatcher() {

        return matcher;
    }

    public void setMatcher(Matcher matcher) {

        this.matcher = matcher;
    }

    public void addHandler(Object handler) {



    }

    public Object[] getHandlers() {

        return null;
    }

    public void setHandlers(Object[] handlers) {

    }

    public void removeHandler(Object handler) {


    }

    public int execute(ProcessingContext processingContext) {

        if((matcher == null || matcher.hasMatches(processingContext))) {

            //phases ->
            //   security
            //   pre-fixup
            //   content
            //   post-fixup
            //   log

            return Dispatcher.OK;
        }

        return Dispatcher.DECLINE;
    }




}
