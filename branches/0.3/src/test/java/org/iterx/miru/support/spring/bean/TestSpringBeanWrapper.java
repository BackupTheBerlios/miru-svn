/*
  org.iterx.miru.spring.beans.TestSpringBeanWrapper

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

import java.util.HashMap;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl; 
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import junit.framework.TestCase;

import org.iterx.miru.support.spring.bean.SpringBeanWrapper;

public class TestSpringBeanWrapper extends TestCase {

    private  MockDefaultListableBeanFactory beanFactory;

    protected void setUp()  {

	beanFactory = new MockDefaultListableBeanFactory();
    }

    protected void tearDown() {

	beanFactory = null;
    }
   
    public void testConstructors(){
	SpringBeanWrapper beanWrapper;

	beanWrapper = new SpringBeanWrapper
	    (beanFactory.assignBeanWrapper(new Object()));
	assertNotNull(beanWrapper);

	try {
	    beanWrapper = new SpringBeanWrapper(null);	    
	    fail("SpringBeanWrapper initialised with null arguments");
	}
	catch(IllegalArgumentException e) {}
    }   

    
    public void testWrappedInstanceAccessors() {
	SpringBeanWrapper beanWrapper;
	Object object;

	beanWrapper = new SpringBeanWrapper
	    (beanFactory.assignBeanWrapper(object = new Object()));
	assertEquals(object, beanWrapper.getWrappedInstance());

	beanWrapper.setWrappedInstance(object = new Object());
	assertEquals(object, beanWrapper.getWrappedInstance());
	
	try {
	    beanWrapper.setWrappedInstance(null);
	    fail("Object is null");
	}
	catch(IllegalArgumentException e) {}
    }

    public void testPropertyAccessors() {
	SpringBeanWrapper beanWrapper;
	MockObject object;
	HashMap map;
	String value;


	beanWrapper = new SpringBeanWrapper
	    (beanFactory.assignBeanWrapper(object = new MockObject()));
	assertNull(beanWrapper.getValue("string"));
	assertNull(object.getString());

	beanWrapper.setValue("string", (value = "value"));
	assertEquals(value, beanWrapper.getValue("string"));
	assertEquals(value, object.getString());

	map = new HashMap();
	map.put("string", (value = "map"));

	beanWrapper.setValues(map);
	assertEquals(value, beanWrapper.getValue("string"));
	assertEquals(value, object.getString());	

	try {
	    beanWrapper.getValue("method-does-not-exist");
	    fail("Failed to detect non-existant property");
	}
	catch(Exception e) {}

	map = new HashMap();
	map.put("method-does-not-exist", "value");
	try {

	    beanWrapper.setValues(map);
	    fail("Failed to detect non-existant property");
	}
	catch(Exception e) {}

    }

    private class MockObject {

	private String string;

	public MockObject() {}
	

	public String getString() {

	    return string;
	}

	public void setString(String string) {

	    this.string = string;
	}
    }

    private class MockDefaultListableBeanFactory
	extends DefaultListableBeanFactory {

	private MockDefaultListableBeanFactory() {}

	private BeanWrapper assignBeanWrapper(Object object) {
            BeanWrapper beanWrapper;

            initBeanWrapper(beanWrapper = new BeanWrapperImpl(object));
	    return beanWrapper;
	}
    }
}
