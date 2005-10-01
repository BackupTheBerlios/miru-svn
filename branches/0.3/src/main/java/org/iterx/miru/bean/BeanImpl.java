/*
  org.iterx.miru.bean.BeanImpl

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
package org.iterx.miru.bean;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.iterx.util.KeyValue;

public class BeanImpl implements Bean {

    protected BeanFactory beanFactory;
    protected String id;
    protected Class cls;

    private Map propertyValues;
    private Object object;
    private boolean singleton;

    {
        propertyValues = new HashMap();
    }

    public BeanImpl(String id, Class cls, boolean singleton) {

        if(id == null)
            throw new IllegalArgumentException("id == null");
        if(cls == null)
            throw new IllegalArgumentException("cls == null");

        this.id = id;
        this.cls = cls;
        this.singleton = singleton;
    }

    protected Object newInstance() {

        try {
            Object instance;

            if(singleton) {
                if(object == null) object = cls.newInstance();
                instance = object;
            }
            else instance = cls.newInstance();

            if(!propertyValues.isEmpty()) {
                BeanWrapper bean;

                bean = ((BeanWrapperAware) beanFactory).assignBeanWrapper(instance);
                for(Iterator iterator = (propertyValues.values()).iterator();
                    iterator.hasNext();) {
                    KeyValue keyValue;

                    keyValue = (KeyValue) iterator.next();

                    bean.setPropertyValue((String) keyValue.getKey(),
                                          keyValue.getValue());
                }
                ((BeanWrapperAware) beanFactory).recycleBeanWrapper(bean);
            }
            return instance;
        }
        catch(Exception e) {
            throw new BeanException("Invalid Bean instance.", e);
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

        return (KeyValue) propertyValues.get(name);
    }

    public void setPropertyValue(KeyValue keyValue) {

        if(keyValue == null)
            throw new IllegalArgumentException("keyValue == null");
        propertyValues.put(keyValue.getKey(), keyValue);
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
        buffer.append(cls.getName());
        buffer.append("',singleton='");
        buffer.append(singleton);
        buffer.append("',properties=[");

        iterator = (propertyValues.values()).iterator();
        next = iterator.hasNext();
        while(next) {
            buffer.append(iterator.next());
            if(next = iterator.hasNext()) buffer.append(',');
        }

        buffer.append("]]");
        return buffer.toString();
    }

}
