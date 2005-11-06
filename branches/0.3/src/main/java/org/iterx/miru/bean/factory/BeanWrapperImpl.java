/*
  org.iterx.miru.bean.factory.BeanWrapperImpl

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

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.Array;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.iterx.util.ArrayUtils;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanFactory;
import org.iterx.miru.bean.BeanRef;
import org.iterx.miru.bean.BeanProvider;

public class BeanWrapperImpl implements BeanWrapper {

    private BeanProvider beanProvider;

    private Object instance;
    private Map getters;
    private Map setters;

    public BeanWrapperImpl(BeanProvider beanProvider) {
        if(beanProvider == null)
            throw new IllegalArgumentException("beanProvider == null");

        this.beanProvider = beanProvider;
    }

    private void initialise() {
        assert (instance != null) : "instance == null";

        Method[] methods;

        getters = new HashMap();
        setters = new HashMap();
        methods = (instance.getClass()).getMethods();

        for(int i = 0; i < methods.length; i++) {
            Method method;
            String key;

            method = methods[i];
            key = (method.getName()).toLowerCase();

            if(key.startsWith("get")) {
                if((method.getParameterTypes()).length  == 0)
                    getters.put(key.substring(3), method);
            }
            else if(key.startsWith("set") ||
                    key.startsWith("add") ||
                    key.startsWith("put"))  {

                if((method.getParameterTypes()).length  == 1) {
                    for(int j = 0; j < 2; j++) {
                        if(setters.containsKey(key))
                            setters.put(key,
                                        ArrayUtils.add((Method[]) setters.get(key), method));
                        else setters.put(key, new Method[] { method });

                        if(key.length() > 3) key = key.substring(3);
                        else break;
                    }
                }
            }
        }
    }

    public Object getWrappedInstance() {

        return instance;
    }


    public void setWrappedInstance(Object object) {

        instance = object;

        if(instance == null) getters = setters = null;
        else initialise();
    }

    public Object getValue(String property) {

        if(property == null)
            throw new IllegalArgumentException("property == null");

        try {
            String key;

            if(getters.containsKey(key = property.toLowerCase()))
                return ((Method) getters.get(key)).invoke(instance);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException
            ("Invalid getter property '" + property + "'");
    }


    public void setValue(String property, Object value) {


        if(property == null)
            throw new IllegalArgumentException("property == null");
        if(value == null)
            throw new IllegalArgumentException("value == null");

        try {
            String name;

            name = (property.toLowerCase());
            if(setters.containsKey(name)) {
                Method[] methods;

                methods = (Method[]) setters.get(name);
                for(int i = methods.length; i-- > 0; ) {
                    PropertyEditor editor;
                    Class parameterType;
                    Method method;
                    Object current, next;

                    current = null;
                    method = methods[i];
                    parameterType = (method.getParameterTypes())[0];
                    next = value;


                    while(current != next) {
                        current = next;


                        if(current instanceof BeanRef) {
                            next = beanProvider.getBean(((BeanRef) current).getId());
                        }
                        else if(current instanceof BeanImpl) {
                            BeanImpl bean;

                            bean = (BeanImpl) current;
                            next = bean.newInstance();
                        }
                        else if(current instanceof String &&
                                (editor = PropertyEditorManager.findEditor(parameterType)) != null) {
                            editor.setAsText((String) current);

                            method.invoke(instance, editor.getValue());
                            return;
                        }
                        else if(parameterType.isAssignableFrom(current.getClass()) ||
                                (parameterType.isArray() &&
                                 current instanceof List &&
                                 (parameterType.getComponentType()).isAssignableFrom(current.getClass()))) {

                            if(current instanceof List ) {
                                ArrayList list;

                                list = new ArrayList();
                                for(Iterator iterator = ((List) current).iterator();
                                    iterator.hasNext(); ) {
                                    Object entry;

                                    entry = iterator.next();
                                    if(entry instanceof BeanRef) {
                                        list.add(beanProvider.getBean(((BeanRef) entry).getId()));
                                    }
                                    else if(entry instanceof BeanImpl) {
                                        BeanImpl bean;

                                        bean = (BeanImpl) entry;
                                        list.add(bean.newInstance());
                                    }
                                    else list.add(entry);
                                }
                                current = ((parameterType.isArray())?
                                           list.toArray((Object[]) Array.newInstance
                                               (parameterType.getComponentType(), list.size())) :
                                                                                                list);
                            }
                            else if(current instanceof Map) {
                                HashMap map;

                                map = new HashMap();
                                for(Iterator iterator = ((Map) current).entrySet().iterator();
                                    iterator.hasNext(); ) {
                                    Map.Entry entry;
                                    Object object;

                                    entry = (Map.Entry) iterator.next();
                                    object = entry.getValue();
                                    if(object instanceof BeanRef) {
                                        map.put(entry.getKey(),
                                                beanProvider.getBean(((BeanRef) object).getId()));
                                    }
                                    else if(object instanceof BeanImpl) {
                                        BeanImpl bean;

                                        bean = (BeanImpl) object;
                                        map.put(entry.getKey(), bean.newInstance());
                                    }
                                    else map.put(entry.getKey(), object);

                                }
                                current = map;
                            }
                            method.invoke(instance, current);
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

    public void setValues(Map map) {

        if(map == null)
            throw new IllegalArgumentException("map == null");

        for(Iterator iterator = (map.entrySet()).iterator();
            iterator.hasNext();) {
            Map.Entry entry;

            entry = (Map.Entry) iterator.next();
            setValue((String) entry.getKey(),
                     entry.getValue());
        }
    }

}
