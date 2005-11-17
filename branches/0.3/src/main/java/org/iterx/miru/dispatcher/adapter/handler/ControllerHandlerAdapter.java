/*
  org.iterx.miru.dispatcher.adapter.handler.ControllerHandlerAdapter

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
package org.iterx.miru.dispatcher.adapter.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.dispatcher.controller.Controller;
import org.iterx.miru.dispatcher.Dispatcher;


public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final Log LOGGER = LogFactory.getLog(ControllerHandlerAdapter.class);

    public boolean supports(Object handler) {

        return (handler instanceof Controller);
    }

    public int execute(ProcessingContext processingContext, Object handler) {
        try {
            return ((Controller) handler).execute(processingContext);
        }
        catch(Exception e) {
            LOGGER.warn("Controller [" + handler + "]failure.", e);
        }
        return Dispatcher.ERROR;
    }
}
