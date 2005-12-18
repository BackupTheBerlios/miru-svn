/*
  org.iterx.miru.support.spring.context.SpringApplicationContext

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
package org.iterx.miru.support.spring.context;

import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.context.factory.ProcessingContextFactory;
import org.iterx.miru.io.factory.ResourceFactory;

import org.iterx.miru.bean.factory.BeanFactory;
import org.iterx.miru.bean.BeanException;
import org.iterx.miru.support.spring.bean.factory.SpringBeanFactory;

public class SpringApplicationContext extends SpringBeanFactory
    implements ApplicationContext {

    private ProcessingContextFactory processingContextFactory;
    private ResourceFactory resourceFactory;

    private ApplicationContext parent;

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

    public ProcessingContextFactory getProcessingContextFactory() {

        if(processingContextFactory == null) {
            if((processingContextFactory =
                (ProcessingContextFactory) getBeanOfType(ProcessingContextFactory.class)) == null)
                throw new BeanException();

        }
        return processingContextFactory;
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
