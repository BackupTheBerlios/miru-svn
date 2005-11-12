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

import org.iterx.miru.bean.BeanException;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.factory.BeanFactory;
import org.iterx.miru.bean.BeanProviderListener;
import org.iterx.miru.bean.BeanProviderListenerAware;

import org.iterx.miru.io.ResourceFactory;

public class ApplicationContextImpl implements ApplicationContext, BeanProviderListener {

    private ProcessingContextFactory processingContextFactory;
    private ResourceFactory resourceFactory;

    private ApplicationContext parent;

    protected BeanProvider beanProvider;

    protected ApplicationContextImpl() {


        this(BeanFactory.getBeanFactory());
    }

    public ApplicationContextImpl(BeanProvider beanProvider) {

        if(beanProvider == null)
            throw new IllegalArgumentException("beanFactory == null");
        if(beanProvider instanceof BeanProviderListenerAware)
            ((BeanProviderListenerAware) beanProvider).addListener(this);

        this.beanProvider = beanProvider;
    }

    public ApplicationContextImpl(ApplicationContext parent) {

        this(BeanFactory.getBeanFactory(), parent);
    }


    public ApplicationContextImpl(BeanProvider beanProvider,
                                  ApplicationContext parent) {

        if(beanProvider == null)
            throw new IllegalArgumentException("beanFactory == null");
        if(beanProvider instanceof BeanProviderListenerAware)
            ((BeanProviderListenerAware) beanProvider).addListener(this);

        this.beanProvider = beanProvider;
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
                resourceFactory = ResourceFactory.getResourceFactory();
        }
        return resourceFactory;
    }

    public void setResourceFactory(ResourceFactory resourceFactory) {

        if(resourceFactory == null)
            throw new IllegalArgumentException("resourceFactory == null");
        this.resourceFactory = resourceFactory;
    }

    public ProcessingContextFactory getProcessingContextFactory() {

        if(processingContextFactory == null) {
            if((processingContextFactory =
                (ProcessingContextFactory) getBeanOfType(ProcessingContextFactory.class)) == null)
                processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
        }
        return processingContextFactory;
    }

    public void setProcessingContextFactory(ProcessingContextFactory processingContextFactory) {

        if(processingContextFactory == null)
            throw new IllegalArgumentException("processingContextFactory == null");

        this.processingContextFactory = processingContextFactory;
    }

    public Object getBean(String name) {
        Object object;

        if(((object = beanProvider.getBean(name)) == null) &&
            parent != null) object = parent.getBean(name);
        return object;
    }

    public Object getBeanOfType(Class type) {
        Object object;

        if(((object = beanProvider.getBeanOfType(type)) == null) &&
           parent != null) object = parent.getBeanOfType(type);

        if(object instanceof ApplicationContextAware)
            ((ApplicationContextAware) object).setApplicationContext(this);
        return object;
    }

    public Object getBeanOfType(Class[] types) {
        Object object;

        if(((object = beanProvider.getBeanOfType(types)) == null) &&
           parent != null) object = parent.getBeanOfType(types);

        return object;
    }

    public boolean containsBean(String name) {

        return (beanProvider.containsBean(name) ||
                (parent != null) && parent.containsBean(name));
    }

    public boolean isSingleton(String name) {

        if(beanProvider.containsBean(name))
            return beanProvider.isSingleton(name);
        else if(parent != null && parent.containsBean(name))
            return parent.isSingleton(name);
        throw new BeanException("Invalid bean name '" + name + "'");
    }

    public void beanProviderEvent(BeanProvider source, Event event, Object data) {

        switch(event) {
            case BEAN_CREATED:
                if(data instanceof ApplicationContextAware)
                    ((ApplicationContextAware) data).setApplicationContext(this);
                break;
        }
    }
}
