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
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import org.iterx.util.ArrayUtils;

public class BeanWrapperImpl implements BeanWrapper {

    protected BeanFactory beanFactory;

    private Object instance;
    private HashMap getters;
    private HashMap setters;

    public BeanWrapperImpl() {}

    public BeanWrapperImpl(Object object) {

        setWrappedInstance(object);
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

    public Object getPropertyValue(String property) {

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


    public void setPropertyValue(String property, Object value) {

        System.out.println("SET PROPERTY="+property + " " + value);
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
                            next = beanFactory.getBean(((BeanRef) current).getId());
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
                        //TODO: Add array converter!
                        else if(parameterType.isAssignableFrom(current.getClass())) {
                            if(current instanceof List) {
                                ArrayList list;

                                list = new ArrayList();
                                for(Iterator iterator = ((List) current).iterator();
                                    iterator.hasNext(); ) {
                                    Object entry;

                                    entry = iterator.next();
                                    if(entry instanceof BeanRef) {
                                        list.add(beanFactory.getBean(((BeanRef) entry).getId()));
                                    }
                                    else if(entry instanceof BeanImpl) {
                                        BeanImpl bean;

                                        bean = (BeanImpl) entry;
                                        list.add(bean.newInstance());
                                    }
                                    else list.add(entry);

                                }
                                current = list;
                            }
                            else if(current instanceof Map) {
                                //TODO: Add implementation
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
