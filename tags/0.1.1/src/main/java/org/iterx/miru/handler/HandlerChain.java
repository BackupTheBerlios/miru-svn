/*
  org.iterx.miru.handler.HandlerChain;

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

package org.iterx.miru.handler;

import org.iterx.miru.context.ProcessingContext;

import org.iterx.miru.interceptor.HandlerInterceptor;

public class HandlerChain {

    private HandlerInterceptor[] handlerInterceptors;
    private Object handler;    

    public HandlerChain(Object handler) {

	this(handler, null);
    }

    public HandlerChain(Object handler, 
			HandlerInterceptor[] handlerInterceptors) {
	if(handler == null)
	    throw new IllegalArgumentException("handler == null");

	this.handler = handler;
	this.handlerInterceptors = handlerInterceptors;
    }

    public Object getHandler() {

	return handler;
    }

    public HandlerInterceptor[] getHandlerInterceptors() {

	return handlerInterceptors;
    }

    public String toString() {
        StringBuffer buffer;
        String cls;

        buffer = new StringBuffer();
	cls = (getClass().getName());
        buffer.append(cls.substring(1 + cls.lastIndexOf('.')));
	buffer.append('[');
        buffer.append(handler.toString());
        buffer.append(",HandlerInterceptors[");
        if(handlerInterceptors != null && 
           handlerInterceptors.length > 0) {
            for(int i = 0; i < handlerInterceptors.length; i++) {
                buffer.append(handlerInterceptors[i].toString());
                buffer.append(',');
            }
            buffer.setLength(buffer.length() - 1);
        }
        buffer.append("]]");
        return buffer.toString();
    }


}
