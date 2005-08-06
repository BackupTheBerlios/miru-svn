package org.iterx.miru.dispatcher.handler;

import org.iterx.miru.dispatcher.handler.flow.IfFlowHandler;

public class HandlerChainImpl extends IfFlowHandler implements HandlerChain {

    private String name;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        if(name == null)
            throw new IllegalArgumentException("name == null");
        this.name = name;
    }

}
