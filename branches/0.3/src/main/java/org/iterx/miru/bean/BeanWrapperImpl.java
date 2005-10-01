/*
  org.iterx.miru.bean.BeanWrapperImpl

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

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.iterx.util.ArrayUtils;

public class BeanWrapperImpl implements BeanWrapper {

    protected BeanFactory beanFactory;

    private Object instance;
    private HashMap map;

    public BeanWrapperImpl() {}

    public BeanWrapperImpl(Object object) {

        setWrappedInstance(object);
    }

    private void initialise() {
        assert (instance != null) : "instance == null";

        Method[] methods;

        map = new HashMap();
        methods = (instance.getClass()).getMethods();

        for(int i = 0; i < methods.length; i++) {
            Method method;
            String key;

            method = methods[i];
            key = (method.getName()).toLowerCase();

            if(key.startsWith("get")) {
                if((method.getParameterTypes()).length  == 0)
                    map.put(key, method);
            }
            else if(key.startsWith("set"))  {
                if((method.getParameterTypes()).length  == 1) {
                    if(map.containsKey(key))
                        map.put(key,
                                ArrayUtils.add((Method[]) map.get(key), method));
                    else map.put(key, new Method[] {method });
                }
            }
        }
    }

    public Object getWrappedInstance() {

        return instance;
    }


    public void setWrappedInstance(Object object) {

        instance = object;

        if(instance == null) map = null;
        else initialise();
    }

    public Object getPropertyValue(String property) {

        if(property == null)
            throw new IllegalArgumentException("property == null");

        try {
            String key;

            if(map.containsKey(key = ("get" + property).toLowerCase()))
                return ((Method) map.get(key)).invoke(instance, null);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException
            ("Invalid getter property '" + property + "'");
    }


    public void setPropertyValue(String property, Object value) {

        if(property == null)
            throw new IllegalArgumentException("property == null");
        if(value == null)
            throw new IllegalArgumentException("value == null");

        try {
            String key;

            if(map.containsKey(key = ("set" + property).toLowerCase())) {
                Method[] methods;
                methods = (Method[]) map.get(key);
                for(int i = methods.length; i-- > 0; ) {
                    PropertyEditor editor;
                    Class parameterType;
                    Method method;
                    Object current, next;

                    current = null;
                    next = value;
                    method = methods[i];
                    parameterType = (method.getParameterTypes())[0];
                    while(current != next) {
                        current = next;

                        if(current instanceof BeanRef) {
                            next = beanFactory.getBean(((BeanRef) current).getId());
                        }
                        else if(current instanceof BeanImpl) {
                            next = ((BeanImpl) current).newInstance();
                        }
                        else if(current instanceof String &&
                                (editor = PropertyEditorManager.findEditor(parameterType)) != null) {
                            editor.setAsText((String) current);
                            method.invoke(instance, new Object[] { editor.getValue() });
                            return;
                        }
                        else if(parameterType.isAssignableFrom(current.getClass())) {
                            method.invoke(instance, new Object[] { current });
                            return;
                        }

                    }
                }
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException
            ("Invalid setter property '" + property + "'.");

    }

    public void setPropertyValues(Map map) {

        if(map == null)
            throw new IllegalArgumentException("map == null");

        for(Iterator iterator = (map.entrySet()).iterator();
            iterator.hasNext();) {
            Map.Entry entry;

            entry = (Map.Entry) iterator.next();
            setPropertyValue((String) entry.getKey(),
                             entry.getValue());
        }

    }
}
