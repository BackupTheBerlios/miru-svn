/*
  org.iterx.miru.spring.context.SpringApplicationContext

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
package org.iterx.miru.spring.context;

import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.io.ResourceFactory;
import org.iterx.miru.handler.HandlerMappingFactory;

import org.iterx.miru.beans.BeanFactory;
import org.iterx.miru.beans.BeanException;
import org.iterx.miru.spring.beans.SpringBeanFactory;

public class SpringApplicationContext extends SpringBeanFactory
    implements ApplicationContext {

    protected ApplicationContext parent;
    private ResourceFactory resourceFactory;
    private HandlerMappingFactory handlerMappingFactory; 

    public SpringApplicationContext()  {

        super();
    }

    public SpringApplicationContext(ApplicationContext parent)  {
	
        super(parent);
	this.parent = parent;
    }

    public SpringApplicationContext(BeanFactory beanFactory) {
        
        super(beanFactory);
    }


    public SpringApplicationContext
	(org.springframework.beans.factory.BeanFactory beanFactory)  {

        super(beanFactory);
    }

    public ApplicationContext getParent() {
        
        return parent;
    }

    public ResourceFactory getResourceFactory() {

        if(resourceFactory == null) {
            if((resourceFactory = 
                (ResourceFactory) getBeanOfType(ResourceFactory.class)) == null) 
                throw new BeanException();
            
        }

        return resourceFactory;
    }

    public HandlerMappingFactory getHandlerMappingFactory() {

        if(handlerMappingFactory == null) {
            if((handlerMappingFactory = 
                (HandlerMappingFactory) getBeanOfType(HandlerMappingFactory.class)) == null) 
                throw new BeanException();
            
        }

        return handlerMappingFactory;
    }

    public Object getBean(String name) {
        Object object;

        object = super.getBean(name);
        if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext(this);
        return object;
    }

    public Object getBeanOfType(Class type){ 
        Object object;

        object = super.getBeanOfType(type);
        if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext(this);
        return object;
    }

    public Object getBeanOfType(Class[] types){ 
        Object object;

        object = super.getBeanOfType(types);
        if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext(this);
        return object;
    }
}
