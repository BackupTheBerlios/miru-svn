/*
  org.iterx.miru.beans.BeanFactoryImpl

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

package org.iterx.miru.beans;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.iterx.miru.beans.BeanFactory;

public class BeanFactoryImpl extends BeanFactory {

    protected BeanFactory parent;

    private LinkedHashMap beans;    

    {
        beans = new LinkedHashMap();
    }

    public BeanFactoryImpl() {}

    public BeanFactoryImpl(BeanFactory parent) {
        
        this.parent = parent;
    }

    public Object getBean(String name) {
        Bean bean;

        if((bean = (Bean) beans.get(name)) != null) return bean.newInstance();

        return ((parent != null)? parent.getBean(name) : null);
    }
    
    public void addBean(String name, Class cls) {
        
        addBean(name, cls, true);
    }

    public void addBean(String name, Class cls, boolean singleton) {

        if(name == null)
            throw new IllegalArgumentException("name == null");
        if(cls == null)
            throw new IllegalArgumentException("cls == null");

        synchronized(beans) {
            beans.put(name, new Bean(cls, singleton));
        }
    }


    public void removeBean(String name) {

        synchronized(beans) {
            beans.remove(name);
        }
    }

    public Object getBeanOfType(Class type) {
        
        for(Iterator iterator = (beans.values()).iterator();
            iterator.hasNext();) {
            Bean bean;
    
            if(type.isAssignableFrom((bean = (Bean) iterator.next()).cls))
                return bean.newInstance();
        }            
        return ((parent != null)? parent.getBeanOfType(type) : null);
    }

    public Object getBeanOfType(Class[] types) {


        for(Iterator iterator = (beans.values()).iterator();
            iterator.hasNext();) {
            Bean bean;
            int i;

            bean = (Bean) iterator.next();
            for(i = types.length; i-- > 0; ) {
                if(!types[i].isAssignableFrom(bean.cls)) break;
            }
            if(i < 0) return bean.newInstance();
        }            
        return ((parent != null)? parent.getBeanOfType(types) : null);
    }

    public boolean containsBean(String name) {

        return ((beans.containsKey(name)) ||
                (parent != null && parent.containsBean(name)));
    }

    public boolean isSingleton(String name) {
        Bean bean;

        if((bean = (Bean) beans.get(name)) != null) return bean.singleton;

        return ((parent != null)? parent.isSingleton(name) : false);       
    }

    private class Bean {

        private Class cls;
        private Object object; 
        private boolean singleton;

        private Bean(Class cls, boolean singleton) {

            this.cls = cls;
            this.singleton = singleton;
        }

        private Object newInstance() {
            try {
                if(singleton) {
                    if(object == null) object = cls.newInstance();
                    return object;
                }
                return cls.newInstance();
            }
            catch(Exception e) {
                throw new BeanException("Invalid bean instance.", e);
            }
        }
    }

}
