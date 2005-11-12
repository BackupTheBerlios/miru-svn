/*
  org.iterx.miru.support.spring.bean.SpringBeanFactory

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

package org.iterx.miru.support.spring.bean.factory;

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


import org.iterx.miru.bean.factory.BeanFactory;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.support.spring.bean.SpringBeanWrapper;

public class SpringBeanFactory extends BeanFactory implements BeanWrapperAware {

    private static final Log LOGGER =
        LogFactory.getLog(SpringBeanFactory.class);

    protected org.springframework.beans.factory.BeanFactory beanFactory;
    protected BeanProvider parent;

    public SpringBeanFactory() {

        beanFactory = new SpringDefaultListableBeanFactoryProxy();
    }

    public SpringBeanFactory(BeanProvider parent) {

        if (parent == null)
            throw new IllegalArgumentException("parent == null");

        beanFactory = new SpringDefaultListableBeanFactoryProxy();
        this.parent = parent;
    }

    public SpringBeanFactory
        (org.springframework.beans.factory.BeanFactory beanFactory) {

        if (beanFactory == null)
            throw new IllegalArgumentException("beanFactory == null");

        this.beanFactory = new SpringDefaultListableBeanFactoryProxy(beanFactory);
    }

    public Object getBean(String name) {
        assert (name != null)  : "name == null";

        try {
            return beanFactory.getBean(name);
        }
        catch(NoSuchBeanDefinitionException e) {
            if(LOGGER.isWarnEnabled())
                LOGGER.warn("Failed to get bean '" + name + "'", e);
        }
        return (parent != null)? parent.getBean(name) : null;
    }

    public Object getBeanOfType(Class type) {
        assert (type != null) : "type == null";

        try {
            Map map;

            if((beanFactory instanceof ListableBeanFactory) &&
               (map = BeanFactoryUtils.beansOfTypeIncludingAncestors
                   ((ListableBeanFactory) beanFactory, type, true, false)).size() > 0)
                return ((map.values()).iterator()).next();

        }
        catch(BeansException e) {
            if(LOGGER.isWarnEnabled())
                LOGGER.warn("Failed to create bean implementation [" +
                            type.toString() + "]", e);
        }

        return (parent != null) ? parent.getBeanOfType(type) : null;
    }


    public Object getBeanOfType(Class[] types) {
        assert (types != null && types.length > 0) : "types == null";
        try {
            Map map;

            if((beanFactory instanceof ListableBeanFactory) &&
               (map = BeanFactoryUtils.beansOfTypeIncludingAncestors
                   ((ListableBeanFactory) beanFactory,
                    types[0], true, false)).size() > 0) {

                for(Iterator beans = (map.values()).iterator();
                    beans.hasNext();) {
                    Object bean;
                    Class cls;
                    int i;

                    cls = (bean = beans.next()).getClass();

                    for (i = types.length; i-- > 1;) {
                        if (!types[i].isAssignableFrom(cls)) break;
                    }

                    if (i < 1) return bean;
                }
            }
        }
        catch(BeansException e) {
            if(LOGGER.isWarnEnabled()) {
                StringBuffer buffer;

                buffer = new StringBuffer();
                for (int i = 0; i < types.length; i++) {
                    buffer.append(',');
                    buffer.append(types[i]);
                }
                LOGGER.warn("Failed to create bean implementation [" +
                            buffer.substring(1) + "]", e);
            }
        }

        return (parent != null) ? parent.getBeanOfType(types) : null;
    }

    public boolean containsBean(String name) {

        return (beanFactory.containsBean(name) ||
                (parent != null && parent.containsBean(name)));

    }

    public boolean isSingleton(String name) {

        return (beanFactory.isSingleton(name) ||
                (parent != null && parent.isSingleton(name)));
    }

    public BeanWrapper assignBeanWrapper(Object object) {
        assert (object != null) : "object == null";

        return ((SpringDefaultListableBeanFactoryProxy) beanFactory).assignBeanWrapper(object);
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
