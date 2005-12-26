/*
  org.iterx.miru.dispatcher.handler.flow.IfFlowHandler

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


import org.iterx.miru.dispatcher.handler.FlowHandler;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.matcher.Matcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;

public class IfFlowHandler<S extends RequestContext, T extends ResponseContext> implements FlowHandler<S, T> {

    private Matcher<S, T> matcher;
    private Handler<S, T> handler;

    public Matcher<S, T> getMatcher() {

        return matcher;
    }

    public void setMatcher(Matcher<? extends S, ? extends T> matcher) {

        this.matcher = (Matcher<S, T>) matcher;
    }

    public Handler<S, T> getHandler()  {

        return handler;
    }

    public void setHandler(Handler<? extends S, ? extends T> handler)  {

        if(handler == null)
            throw new IllegalArgumentException("handler == null");

        this.handler = (Handler<S, T>) handler;
    }

    public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {

        return ((matcher == null) || matcher.hasMatches(processingContext));
    }


    public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {

        return (matcher == null)? new Matches() : matcher.getMatches(processingContext);
    }


    public int execute(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (handler != null) : "handler == null";
        Matches matches;

        if(!(handler instanceof Matcher) ||
           (matches = ((Matcher<S, T>) handler).getMatches(processingContext)) != null) {



            //TODO: Create new ProcessingContext and set matches
            return handler.execute(processingContext);
        }

        return Dispatcher.DECLINE;
    }
}
