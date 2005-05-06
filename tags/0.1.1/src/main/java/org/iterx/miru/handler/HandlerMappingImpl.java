/*
  org.iterx.miru.handler.HandlerMappingImpl

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

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.iterx.miru.handler.Handler;
import org.iterx.miru.handler.HandlerChain;
import org.iterx.miru.handler.HandlerMapping;

import org.iterx.miru.interceptor.HandlerInterceptor;

public class HandlerMappingImpl implements HandlerMapping {

    protected LinkedHashMap handlers;
    
    public HandlerMappingImpl() {
	
	handlers = new LinkedHashMap();
    }

    public Object getHandler(String id) {
	
	return  handlers.get(id);
    }

    public Iterator getHandlers() {

	return (handlers.values()).iterator();		
    }
    
    public void addHandler(String id, 
			   Object handler) {

	addHandler(id, handler, null);
    }

    public void addHandler(String id, 
			   Object handler,
			   HandlerInterceptor[] handlerInterceptors) {

	if(id == null)
	    throw new IllegalArgumentException("id == null");

	synchronized(handlers) {
	    handlers.put(id, new HandlerChain(handler, 
					      handlerInterceptors));
	}
    }

    public void removeHandler(String id) {
	
	synchronized(handlers) {
	    handlers.remove(id);
	}
    }

    public String toString() {
        StringBuffer buffer;
        String cls;

        buffer = new StringBuffer();
	cls = (getClass().getName());
        buffer.append(cls.substring(1 + cls.lastIndexOf('.')));
	buffer.append('[');
        buffer.append(handlers.toString());
        buffer.append(']');
        return buffer.toString();
    }

}
