/*
  org.iterx.miru.dispatcher.handler.factory.HandlerWrapperImpl

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

package org.iterx.miru.dispatcher.handler.factory;

import java.util.Map;
import java.util.List;

import org.iterx.miru.dispatcher.handler.HandlerWrapper;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.handler.HandlerChainFactory;
import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanWrapperAware;

public class HandlerWrapperImpl implements HandlerWrapper {

    private HandlerChainFactory handlerChainFactory;
    private BeanProvider beanProvider;
    private BeanWrapper beanWrapper;

    public HandlerWrapperImpl(HandlerChainFactory handlerChainFactory,
                              BeanProvider beanProvider) {
        if(handlerChainFactory == null)
            throw new IllegalArgumentException("handlerChainFactory == null");
        if(beanProvider == null)
            throw new IllegalArgumentException("beanProvider == null");
        if(!(beanProvider instanceof BeanWrapperAware))
            throw new IllegalArgumentException("beanProvider is not BeanWrapperAware.");

        this.handlerChainFactory = handlerChainFactory;
        this.beanProvider = beanProvider;
    }

    public Object getWrappedInstance() {

        return ((beanWrapper != null)?
                beanWrapper.getWrappedInstance() : null);
    }

    public void setWrappedInstance(Object object) {
        BeanWrapperAware beanWrapperAware;

        beanWrapperAware =(BeanWrapperAware) beanProvider;

        if(beanWrapper != null) {
            beanWrapperAware.recycleBeanWrapper(beanWrapper);
            beanWrapper = null;
        }
        if(object != null)
            beanWrapper = beanWrapperAware.assignBeanWrapper(object);
    }

    public Object getValue(String property) {

        return beanWrapper.getValue(property);
    }

    public void setValue(String property, Object value) {


        beanWrapper.setValue(property, value);
    }

    public void setValues(Map map) {

        beanWrapper.setValues(map);
    }

    public void setHandler(Object value) {

        //wrap if not handler!
        if(value instanceof Handler) setValue("handler", value);
        if(value instanceof List) {
            //iterate over list, wrapping
        }

    }

    /*
    private Object instance;
    private Map getters;
    private Map setters;

    public HandlerWrapperImpl() {}

    public HandlerWrapperImpl(Object object) {

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
                    key.startsWith("add")) {

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
            String name;

            if(getters.containsKey(name = property.toLowerCase())) {
                Object object;

                object = ((Method) getters.get(name)).invoke(instance);
                if(object instanceof AdapterAdapter)
                    object = ((AdapterAdapter) object).getInstance();
                return object;
            }
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

            System.out.println(">>>>" + instance + " " + value);
            if(setters.containsKey(name = property.toLowerCase())) {
                Method[] methods;

                methods = (Method[]) setters.get(name);
                for(int i = methods.length; i-- > 0; ) {
                    Class parameterType;
                    Method method;
                    Object current, next;

                    current = null;
                    method = methods[i];
                    parameterType = (method.getParameterTypes())[0];
                    next = value;

                    while(current != next) {
                        current = next;
                        System.out.println("TYPE=>" + parameterType);

                        if(parameterType.isAssignableFrom(current.getClass()) ||
                           (parameterType.isArray() &&
                            current instanceof List &&
                            (parameterType.getComponentType()).isAssignableFrom(current.getClass()))) {

                            System.out.println("***************");

                            if(current instanceof List ) {
                                List list;

                                list = (List) current;
                                current = ((parameterType.isArray())?
                                           list.toArray((Object[]) Array.newInstance
                                               (parameterType.getComponentType(), list.size())) :
                                           list);

                            }
                            else if(current instanceof Map) {

                            }


                            method.invoke(instance, current);
                            return;
                        }
                    }
                }
            }


            //object = ((Method) setters.get(name)).invoke(instance);
            // if(object instanceof AdapterAdapter)
            //     object = ((AdapterAdapter) object).getInstance();

        }
        catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException
            ("Invalid setter property '" + property + "'");


    }

    public void setValues(Map map) {

        if(map == null)
            throw new IllegalArgumentException("map == null");

        for(Iterator iterator = (map.entrySet()).iterator();
            iterator.hasNext(); ) {
            Map.Entry entry;

            entry = (Map.Entry) iterator.next();
            setValue((String) entry.getKey(), entry.getValue());
        }
    }





    /*
        public void addHandler(Object object) {
            HandlerAdapter[] adapters;
            adapters = provider.getHandlerAdapters();
            for(int i = 0; i < adapters.length; i++) {
                final HandlerAdapter adapter;
                final Object handler;


                handler = object;
                if((adapter = adapters[i]).supports(handler)) {
                    //delegate to bean wrapper
                    instance.addHandler
                        (new HandlerWrapper() {

                            public Object unwrap() {

                                return handler;
                            }

                            public int execute(ProcessingContext processingContext) {

                                return adapter.process(processingContext, handler);
                            }

                            public int hashCode() {

                                return handler.hashCode();
                            }

                            public boolean equals(Object object) {

                                return handler.equals(object);
                            }
                        });
                    return;
                }
            }
            if(object instanceof Handler) {
                instance.addHandler(object);
                return;
            }

            throw new IllegalArgumentException
                ("Unsupported handler ' " + object.getClass() + "'.");
        }

        public Object[] getHandlers() {
            Object[] handlers;

            handlers = null; // instance.getHandlers();
            for(int i = 0; i < handlers.length; i++) {
                Object handler;

                if(((handler = handlers[i]) instanceof HandlerWrapper))
                    handlers[i] = ((HandlerWrapper) handler).unwrap();
            }
            return handlers;
        }

        public void setHandlers(Object[] objects) {

            for(int i = 0; i < objects.length; i++) {
                addHandler(objects[i]);
            }
        }

        public void removeHandler(Object object) {

            //instance.removeHandler(object);
        }
    */



    private static class HandlerAdapterAdapter implements Handler, AdapterAdapter {

        private HandlerAdapter adapter;
        private Object handler;

        private HandlerAdapterAdapter(HandlerAdapter adapter, Object handler) {

            this.adapter = adapter;
            this.handler = handler;
        }

        public Object getInstance() {

            return handler;
        }

        public int execute(ProcessingContext processingContext) {

            return adapter.execute(processingContext, handler);
        }

    }

    private interface AdapterAdapter {

        Object getInstance();
    }
}
