/*
  org.iterx.miru.bean.factory.BeanImpl

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

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.iterx.util.KeyValue;
import org.iterx.miru.bean.Bean;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.bean.BeanException;
import org.iterx.miru.bean.BeanProviderListenerAware;
import org.iterx.miru.bean.BeanProviderListener;

public abstract class BeanImpl implements Bean {

    protected BeanFactory beanFactory;

    protected String id;
    protected Class cls;
    protected boolean singleton;

    private Map values;
    private Object object;

    {
        values = new HashMap();
    }

    protected Object newInstance() {

        try {
            Object instance;

            if(singleton) {
                if(object != null) return object;

                instance = object = cls.newInstance();
            }
            else instance = cls.newInstance();

            if(!values.isEmpty()) {
                BeanWrapper bean;

                bean = ((BeanWrapperAware) beanFactory).assignBeanWrapper(instance);
                for(Iterator iterator = (values.values()).iterator();
                    iterator.hasNext();) {
                    KeyValue keyValue;

                    keyValue = (KeyValue) iterator.next();
                    bean.setValue((String) keyValue.getKey(),
                                  keyValue.getValue());
                }
                ((BeanWrapperAware) beanFactory).recycleBeanWrapper(bean);
            }
            if(beanFactory instanceof BeanProviderListenerAware)
                beanFactory.notifyListeners(BeanProviderListener.Event.BEAN_CREATED,
                                            instance);

            return instance;
        }
        catch(Exception e) {
            throw new BeanException("Invalid Bean instance [" + this + "].", e);
        }
    }

    public String getId() {

        return id;
    }

    public boolean isSingleton() {

        return singleton;
    }

    public void setSingleton(boolean singleton) {

        this.singleton = singleton;
    }

    public KeyValue getPropertyValue(String name) {
        return (KeyValue) values.get(name);
    }

    public void setPropertyValue(KeyValue keyValue) {

        if(keyValue == null)
            throw new IllegalArgumentException("keyValue == null");

        values.put(keyValue.getKey(), keyValue);
    }

    public String toString() {
        StringBuffer buffer;
        Iterator iterator;
        boolean next;
        String name;

        buffer = new StringBuffer();
        name = (getClass().getName());
        buffer.append(name.substring(1 + name.lastIndexOf('.')));
        buffer.append("[id='");
        buffer.append(id);
        buffer.append("',class='");
        buffer.append((cls == null)? null : cls.getName());
        buffer.append("',singleton='");
        buffer.append(singleton);
        buffer.append("',properties=[");

        iterator = (values.values()).iterator();
        next = iterator.hasNext();
        while(next) {
            buffer.append(iterator.next());
            if(next = iterator.hasNext()) buffer.append(',');
        }

        buffer.append("]]");
        return buffer.toString();
    }

}
