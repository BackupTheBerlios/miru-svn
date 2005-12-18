/*
  org.iterx.miru.bean.factory.BeanFactoryImpl

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

package org.iterx.miru.bean.factory;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.Bean;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanAware;

public class BeanFactoryImpl extends BeanFactory
    implements BeanWrapperAware {

    private BeanProvider parent;

    private final Map beans = new HashMap();

    public BeanFactoryImpl() {}

    public BeanFactoryImpl(BeanProvider parent) {

        this.parent = parent;
    }

    public Object getBean(String name) {
        BeanImpl bean;

        if((bean = (BeanImpl) beans.get(name)) != null) return newInstance(bean);
        return ((parent != null)? parent.getBean(name) : null);
    }

    public Bean createBeanDefinition(String id, Class cls) {

        return createBeanDefinition(id, cls, true);
    }

    public Bean createBeanDefinition(String id, Class cls, boolean singleton) {
        BeanImpl bean;

        if(cls == null)
            throw new IllegalArgumentException("cls == null");

        bean = new BeanImpl() {};
        bean.id = id;
        bean.cls = cls;
        bean.singleton = singleton;
        bean.beanFactory = this;

        return bean;
    }

    public void addBeanDefinition(Bean bean) {
        String id;

        if(bean == null)
            throw new IllegalArgumentException("bean == null");
        if(!(bean instanceof BeanImpl) || ((BeanImpl) bean).beanFactory != this)
            throw new IllegalArgumentException
                ("Foreign Bean instance [" + bean + "].");
        if((id = bean.getId()) == null)
            throw new IllegalArgumentException("Anonymous Bean Definition.");

        synchronized(beans) {
            beans.put(id, bean);
        }

    }

    public Bean getBeanDefinition(String id) {

        synchronized(beans) {
            return (Bean) beans.get(id);
        }
    }

    public Bean[] getBeanDefinitions() {

        synchronized(beans) {
            return (Bean[]) (beans.values()).toArray(new Bean[beans.size()]);
        }
    }

    public void setBeanDefinitions(Bean[] beans) {

        synchronized(this.beans) {
            this.beans.clear();
            for(int i = beans.length; i-- > 0;){
                addBeanDefinition(beans[i]);
            }
        }
    }

    public void removeBeanDefinition(String id) {

        synchronized(beans) {
            beans.remove(id);
        }
    }

    public Object getBeanOfType(Class type) {

        for(Iterator iterator = (beans.values()).iterator();
            iterator.hasNext();) {
            BeanImpl bean;

            if(type.isAssignableFrom((bean = (BeanImpl) iterator.next()).cls))
                return newInstance(bean);
        }
        return ((parent != null)? parent.getBeanOfType(type) : null);
    }

    public Object getBeanOfType(Class[] types) {

        for(Iterator iterator = (beans.values()).iterator();
            iterator.hasNext();) {
            BeanImpl bean;
            int i;

            bean = (BeanImpl) iterator.next();
            for(i = types.length; i-- > 0; ) {
                if(!types[i].isAssignableFrom(bean.cls)) break;
            }
            if(i < 0) return newInstance(bean);
        }
        return ((parent != null)? parent.getBeanOfType(types) : null);
    }

    public boolean containsBean(String id) {

        return ((beans.containsKey(id)) ||
                (parent != null && parent.containsBean(id)));
    }

    public boolean isSingleton(String id) {
        BeanImpl bean;

        if((bean = (BeanImpl) beans.get(id)) != null)
            return bean.isSingleton();

        return ((parent != null) && parent.isSingleton(id));
    }


    public BeanWrapper assignBeanWrapper(Object object) {
        BeanWrapperImpl wrapper;

        wrapper = new BeanWrapperImpl(this);
        wrapper.setWrappedInstance(object);
        return wrapper;
    }


    public void recycleBeanWrapper(BeanWrapper wrapper) {
        assert (wrapper == null ||
                wrapper instanceof BeanWrapperImpl) : "Invalid instance.";

        wrapper.setWrappedInstance(null);
    }


    private static final Object newInstance(BeanImpl bean) {
        Object object;

        object = bean.newInstance();
        if(object instanceof BeanAware)
            ((BeanAware) object).setId(bean.getId());

        return object;
    }
}
