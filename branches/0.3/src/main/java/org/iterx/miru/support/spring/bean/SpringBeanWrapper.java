/*
  org.iterx.miru.bean.SpringBeanWrapper

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

package org.iterx.miru.support.spring.bean;

import java.util.Map;

import org.springframework.beans.BeanWrapper;

public class SpringBeanWrapper implements org.iterx.miru.bean.BeanWrapper {

    private BeanWrapper wrapper;

    public SpringBeanWrapper(BeanWrapper wrapper) {

	if(wrapper == null) 
	    throw new IllegalArgumentException("wrapper == null");

	this.wrapper = wrapper;
    }

    public Object getWrappedInstance() {

	return wrapper.getWrappedInstance();
    }

    public void setWrappedInstance(Object object) {
	
	wrapper.setWrappedInstance(object);
    }
    
    public Object getPropertyValue(String property) {

	return wrapper.getPropertyValue(property);
    }

    public void setPropertyValue(String property, Object value) {

	wrapper.setPropertyValue(property, value);
    }

    public void setPropertyValues(Map map) {

	wrapper.setPropertyValues(map);
    }
}
