/*
  org.iterx.miru.spring.beans.SpringBeanFactory

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

package org.iterx.miru.spring.beans;

import java.util.Map;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;


import org.iterx.miru.beans.BeanFactory;
import org.iterx.miru.beans.BeanWrapper;
import org.iterx.miru.beans.BeanWrapperSupport;
import org.iterx.miru.spring.beans.SpringBeanWrapper;


public class SpringBeanFactory implements BeanFactory, BeanWrapperSupport {

    protected static final Log logger = 
	LogFactory.getLog(SpringBeanFactory.class);

    protected org.springframework.beans.factory.BeanFactory factory;
    protected BeanFactory parent;

    public SpringBeanFactory()  {

	factory = new SpringDefaultListableBeanFactoryProxy();
    }

    public SpringBeanFactory(BeanFactory parent)  {
	
	if(parent == null) 
	    throw new IllegalArgumentException("parent == null");
	
	factory = new SpringDefaultListableBeanFactoryProxy();
	this.parent = parent;
    }

    public SpringBeanFactory
	(org.springframework.beans.factory.BeanFactory beanFactory)  {
	
	if(beanFactory == null) 
	    throw new IllegalArgumentException("beanFactory == null");

	factory = new SpringDefaultListableBeanFactoryProxy(beanFactory);
    }

    public Object getBean(String name) {
	Object bean;
	
	bean = null;
	try {
	    bean = factory.getBean(name);
	}
	catch(NoSuchBeanDefinitionException e) {
	    if(parent != null) parent.getBean(name);
	    else if(logger.isDebugEnabled())
		logger.debug("Failed to get bean '" + 
			     name + "'", e);
	}
	return bean;
    }

    public Object getBeanOfType(Class type){ 
	assert (type != null) : "type == null";
	try {
	    Map map;
	    
	    if((factory instanceof ListableBeanFactory) &&	       
	       (map = BeanFactoryUtils.beansOfTypeIncludingAncestors
		((ListableBeanFactory) factory, type, true, false)).size() > 0)
		return ((map.values()).iterator()).next();
	    
	}
	catch(BeansException e) {
	    if(logger.isDebugEnabled())
		logger.debug("Failed to create bean implementation [" + 
			     type.toString() + "]", e);
	}

	return (parent != null)? parent.getBeanOfType(type) : null;
    }


    public Object getBeanOfType(Class[] types) {
	assert (types != null && types.length > 0) : "types == null";
	try {
	    Map map;
	    
		    if((factory instanceof ListableBeanFactory) &&	       
	       (map = BeanFactoryUtils.beansOfTypeIncludingAncestors
		((ListableBeanFactory) factory, 
		 types[0], true, false)).size() > 0) {
		

		for(Iterator beans = (map.values()).iterator(); 
		    beans.hasNext();) {
		    Object bean;
		    Class cls;
		    int i;

		    cls = (bean = beans.next()).getClass();

		    for(i = types.length; i-- > 1; ) {
			if(!types[i].isAssignableFrom(cls)) break;
		    }
		    
		    if(i < 1) return bean;
		}
	    }
	}
	catch(BeansException e) {
	    if(logger.isDebugEnabled()) {
		StringBuffer buffer;

		buffer = new StringBuffer();
		for(int i = 0; i < types.length; i++) {
		    buffer.append(',');
		    buffer.append(types[i]);
		}
		logger.debug("Failed to create bean implementation [" + 
			     buffer.substring(1) + "]", e);
	    }
	}

	return (parent != null)? parent.getBeanOfType(types) : null;
    }

    public boolean containsBean(String name) {

	return (factory.containsBean(name) ||
		(parent != null && parent.containsBean(name)));
	
    }

	
    public boolean isSingleton(String name) {

	return (factory.isSingleton(name) ||
		(parent != null && parent.isSingleton(name)));
    }

    public BeanWrapper assignBeanWrapper(Object object) {
	assert (object != null) : "object == null";
	
	return ((SpringDefaultListableBeanFactoryProxy) factory).assignBeanWrapper(object);
    }

    public void recycleBeanWrapper(BeanWrapper wrapper) {}

    
    private class SpringDefaultListableBeanFactoryProxy 
	extends DefaultListableBeanFactory {

	private SpringDefaultListableBeanFactoryProxy() {}

	private SpringDefaultListableBeanFactoryProxy
	    (org.springframework.beans.factory.BeanFactory parent) {

	    super(parent);
	}

	private SpringBeanWrapper assignBeanWrapper(Object object) {
            BeanWrapperImpl beanWrapper;

            initBeanWrapper(beanWrapper = new BeanWrapperImpl(object));
	    return new SpringBeanWrapper(beanWrapper);
	}

    }


}
