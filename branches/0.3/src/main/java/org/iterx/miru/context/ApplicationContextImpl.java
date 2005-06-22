/*
  org.iterx.miru.context.ApplicationContextImpl

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

import org.iterx.miru.beans.BeanFactory;
import org.iterx.miru.beans.BeanException;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.io.ResourceFactory;
import org.iterx.miru.dispatcher.handler.HandlerMappingFactory;

public class ApplicationContextImpl implements ApplicationContext {

    protected HandlerMappingFactory handlerMappingFactory;
    protected ResourceFactory resourceFactory;
    protected BeanFactory beanFactory;

    protected ApplicationContext parent;

    protected ApplicationContextImpl() {}

    public ApplicationContextImpl(BeanFactory beanFactory) {

        if(beanFactory == null)
            throw new IllegalArgumentException("beanFactory == null");
        this.beanFactory = beanFactory;
    }

    public ApplicationContextImpl(BeanFactory beanFactory,
                                  ApplicationContext parent) {

        if(beanFactory == null)
            throw new IllegalArgumentException("beanFactory == null");
        this.beanFactory = beanFactory;
        this.parent = parent;
    }


    public ApplicationContext getParent() {

        return parent;
    }

    public void setParent(ApplicationContext parent) {

        this.parent = parent;
    }

    public ResourceFactory getResourceFactory() {
        if(resourceFactory == null) {
            if((resourceFactory =
                (ResourceFactory) getBeanOfType(ResourceFactory.class)) == null)
                throw new BeanException("Invalid bean type 'ResourceFactory'.");

        }

        return resourceFactory;
    }

    public void setResourceFactory(ResourceFactory resourceFactory) {

        if(resourceFactory== null)
            throw new IllegalArgumentException("resourceFactory == null");
        this.resourceFactory = resourceFactory;
    }

    public HandlerMappingFactory getHandlerMappingFactory() {

        if(handlerMappingFactory == null) {
            if((handlerMappingFactory =
                (HandlerMappingFactory) getBeanOfType(HandlerMappingFactory.class)) == null)
                throw new BeanException("Invalid bean type 'HandlerMappingFactory'");

        }

        return handlerMappingFactory;
    }

    public void setHandlerMappingFactory(HandlerMappingFactory handlerMappingFactory) {

        if(handlerMappingFactory == null)
            throw new IllegalArgumentException("handlerMappingFactory == null");

        this.handlerMappingFactory = handlerMappingFactory;
    }

    public Object getBean(String name) {
        Object object;

        if(((object = beanFactory.getBean(name)) == null) &&
           parent != null) object = parent.getBean(name);

        if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext(this);
        return object;
    }

    public Object getBeanOfType(Class type){
        Object object;

        if(((object = beanFactory.getBeanOfType(type)) == null) &&
           parent != null) object = parent.getBeanOfType(type);

        if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext(this);
        return object;
    }

    public Object getBeanOfType(Class[] types){
        Object object;

        if(((object = beanFactory.getBeanOfType(types)) == null) &&
           parent != null) object = parent.getBeanOfType(types);

        if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext(this);
        return object;
    }

    public boolean containsBean(String name) {

        return (beanFactory.containsBean(name) ||
                (parent != null) && parent.containsBean(name));
    }

    public boolean isSingleton(String name) {

        if(beanFactory.containsBean(name))
            return beanFactory.isSingleton(name);
        else if(parent != null && parent.containsBean(name))
            return parent.isSingleton(name);
        throw new BeanException("Invalid bean name '" + name + "'");
    }

}
