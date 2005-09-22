package org.iterx.miru.dispatcher.handler;

import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.dispatcher.matcher.Matcher;
import org.iterx.miru.context.ProcessingContext;

public class HandlerChainImpl implements HandlerChain {

    private String name;
    private Matcher matcher;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        if(name == null)
            throw new IllegalArgumentException("name == null");
        this.name = name;
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
            //iterate over handler chains, executing.


            return Dispatcher.OK;
        }

        return Dispatcher.DECLINE;
    }




}
