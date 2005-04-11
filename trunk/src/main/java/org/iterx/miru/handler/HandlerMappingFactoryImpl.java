/*
  org.iterx.miru.handler.HandlerMappingFactoryImpl

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

import org.iterx.miru.handler.HandlerMapping;
import org.iterx.miru.handler.HandlerMappingImpl;
import org.iterx.miru.handler.HandlerMappingFactory;


public class HandlerMappingFactoryImpl implements HandlerMappingFactory {
    
    protected HandlerMapping handlerMapping;

    public HandlerMappingFactoryImpl() {

        this(new HandlerMappingImpl());
    }

    public HandlerMappingFactoryImpl(HandlerMapping handlerMapping) {

	if(handlerMapping == null) 
	    throw new IllegalArgumentException("handlerMapping == null");

	this.handlerMapping = handlerMapping;
    }
    
    public HandlerMapping getHandlerMapping() {

	return handlerMapping;
    }

    public void setHandlerMapping(HandlerMapping handlerMapping) {

	if(handlerMapping == null) 
	    throw new IllegalArgumentException("handlerMapping == null");

	this.handlerMapping = handlerMapping;
    }
   
}

