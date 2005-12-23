/*
  org.iterx.miru.dispatcher.handler.flow.AndFlowHandler

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

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.matcher.Matcher;
import org.iterx.miru.matcher.Matches;
import org.iterx.miru.dispatcher.handler.FlowHandler;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.util.ArrayUtils;

public class AndFlowHandler implements FlowHandler {

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


    public boolean hasMatches(ProcessingContext processingContext) {

        for(int i = handlers.length; i-- > 0; ) {
            Handler handler;

            handler = handlers[i];
            if(handler instanceof Matcher &&
               !((Matcher) handler).hasMatches(processingContext)) return false;
        }
        return true;
    }

    public Matches getMatches(ProcessingContext processingContext) {
        Matches matches;

        matches = new Matches();
        for(int i = handlers.length; i-- > 0; ) {
            Handler handler;

            handler = handlers[i];
            if(handler instanceof Matcher) {
                Matches result;

                result = ((Matcher) handler).getMatches(processingContext);
                if(result == null) return null;
                else matches.put(result);
            }
        }
        return matches;
    }
   
    public int execute(ProcessingContext processingContext) {

        for(int i = 0; i < handlers.length; i++ ) {
            int status;

            status = handlers[i].execute(processingContext);
            if(status == Dispatcher.ERROR ||
               status == Dispatcher.DONE) return status;
        }
        return Dispatcher.OK;
    }

}
