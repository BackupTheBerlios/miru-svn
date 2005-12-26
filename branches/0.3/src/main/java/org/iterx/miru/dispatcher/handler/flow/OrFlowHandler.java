/*
  org.iterx.miru.dispatcher.handler.flow.OrFlowHandler

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

package org.iterx.miru.dispatcher.handler.flow;

import java.util.List;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.matcher.Matcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.dispatcher.handler.FlowHandler;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.util.ArrayUtils;

public class OrFlowHandler<S extends RequestContext, T extends ResponseContext> implements FlowHandler<S, T> {

    private Handler<S, T>[] handlers = (Handler<S, T>[]) new Object[0];


    public void addHandler(Handler<? extends S, ? extends T> handler) {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");

        handlers = (Handler<S, T>[]) ArrayUtils.add(handlers, handler);
    }

    public Handler<S, T>[] getHandlers() {

        return handlers;
    }

    public void setHandlers(List<Handler<? extends S, ? extends T>>  handlers) {

        if(handlers == null)
            throw new IllegalArgumentException("handlers == null");

        this.handlers = handlers.toArray(this.handlers);
    }


    public void removeHandler(Handler<? extends S, ? extends T> handler) {

        handlers = (Handler<S, T>[]) ArrayUtils.remove(handlers, handler);
    }

    public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {

        for(Handler<S, T> handler : handlers) {
            if(!(handler instanceof Matcher) ||
               ((Matcher<S, T>) handler).hasMatches(processingContext)) return true;
        }
        return false;
    }


    public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {

        for(Handler<S, T> handler : handlers) {
            if(handler instanceof Matcher) {
                Matches matches;

                if((matches = ((Matcher<S, T>) handler).getMatches(processingContext)) != null)
                    return matches;
            }
            else return new Matches();
        }
        return null;
    }


    public int execute(ProcessingContext<? extends S, ? extends T> processingContext) {

        for(Handler<S, T> handler : handlers) {            
            if(!(handler instanceof Matcher) ||
               ((Matcher<S, T>) handler).hasMatches(processingContext))
                return handler.execute(processingContext);
        }
        return Dispatcher.OK;
    }

}
