/*
  org.iterx.miru.dispatcher.handler.factory.HandlerChainImpl

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

package org.iterx.miru.dispatcher.handler.factory;


import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.Status;
import org.iterx.miru.matcher.Matcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.factory.ProcessingContextFactory;

public class HandlerChainImpl<S extends RequestContext, T extends ResponseContext>
    implements HandlerChain<S, T>, ApplicationContextAware<S, T> {

    private ProcessingContextFactory<S, T> processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();

    private Handler<S, T> handler;
    private String id;

    public void setApplicationContext(ApplicationContext<? extends S, ? extends T> applicationContext) {

        processingContextFactory = (ProcessingContextFactory<S, T>) applicationContext.getProcessingContextFactory();
    }


    public String getId() {

        return (id != null)? id : "\0" + System.identityHashCode(this);
    }

    public void setId(String id) {

        if(id != null && id.startsWith("\0"))
            throw new IllegalArgumentException("Malformed id '" + id + "'");
        this.id = id;
    }

    public Handler<S, T> getHandler() {

        return handler;
    }

    public void setHandler(Handler<? extends S, ? extends T> handler) {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");
        this.handler = (Handler<S, T>) handler;
    }

    public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (handler != null) : "handler == null";

        return ((handler instanceof Matcher)?
                ((Matcher<S, T>) handler).getMatches(processingContext) : new Matches());
    }

    public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (handler != null) : "handler == null";

        return (!(handler instanceof Matcher) ||
                ((Matcher<S, T>) handler).hasMatches(processingContext));
    }

    public Status execute(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (handler != null) : "handler == null";
        Matches matches;

        if((matches = getMatches(processingContext)) != null) {
            ProcessingContext<S, T> childProcessingContext;
            childProcessingContext = processingContextFactory.getProcessingContext(processingContext.getRequestContext(),                                                                                   processingContext.getResponseContext());

            childProcessingContext.setAttribute(ProcessingContext.MATCHES_ATTRIBUTE, matches);
            return handler.execute(childProcessingContext);
        }

        return Status.DECLINE;
    }
}
