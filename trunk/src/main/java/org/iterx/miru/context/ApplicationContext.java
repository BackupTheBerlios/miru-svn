/*
  org.iterx.miru.context.ApplicationContext;

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
package org.iterx.miru.context;

import org.iterx.miru.io.ResourceFactory;
import org.iterx.miru.beans.BeanFactory;
import org.iterx.miru.handler.HandlerMappingFactory;

public interface ApplicationContext {

    public static final String BEAN_FACTORY = 
	"org.iterx.miru.beans.BeanFactory";

    public static final String RESOURCE_FACTORY = 
	"org.iterx.miru.io.ResourceFactory";

    public static final String HANDLER_MAPPING_FACTORY = 
	"org.iterx.miru.handler.HandlerMappingFactory";

    public ApplicationContext getParent();

    public BeanFactory getBeanFactory();
    
    public ResourceFactory getResourceFactory();

    public HandlerMappingFactory getHandlerMappingFactory();

}
