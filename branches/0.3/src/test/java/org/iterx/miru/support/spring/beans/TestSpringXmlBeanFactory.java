/*
  org.iterx.miru.spring.beans.TestSpringXmlBeanFactory

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

package org.iterx.miru.support.spring.beans;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import junit.framework.TestCase;

import org.iterx.miru.beans.BeanFactory;
import org.iterx.miru.beans.BeanWrapper;
import org.iterx.miru.beans.BeanWrapperSupport;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.support.spring.beans.SpringXmlBeanFactory;


public class TestSpringXmlBeanFactory extends TestCase {

    private MockResource resource;

    protected void setUp() throws URISyntaxException {
	byte[] data;

	data = ("<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" " + 
		"\"http://www.springframework.org/dtd/spring-beans.dtd\">\n" +
		"<beans>" +
		"<bean id=\"bean\" singleton=\"true\" " +  
		"class=\"org.iterx.miru.support.spring.beans.SpringXmlBeanFactory\"/>" +
		"<bean id=\"bean-prototype\" singleton=\"false\" " +  
		"class=\"org.iterx.miru.support.spring.beans.SpringXmlBeanFactory\"/>" +
		"</beans>").getBytes();
	resource = new MockResource(new URI("spring-beans.xml"));
	resource.setContent(data);
    }
    
    protected void tearDown() {

	resource = null;
    }

    
    public void testConstructors(){
	BeanFactory beanFactory;
	
	beanFactory = new SpringXmlBeanFactory();
	assertNotNull(beanFactory);

	beanFactory = new SpringXmlBeanFactory(beanFactory);

	beanFactory = new SpringXmlBeanFactory
	    (new DefaultListableBeanFactory());
	
	try {
	    beanFactory = new SpringXmlBeanFactory((BeanFactory) null);	    
	    fail("SpringXmlBeanFactory initialised with null arguments");
	}
	catch(IllegalArgumentException e) {}

	try {
	    beanFactory = new SpringXmlBeanFactory
		((org.springframework.beans.factory.BeanFactory) null);	    
	    fail("SpringXmlBeanFactory initialised with null arguments");
	}
	catch(IllegalArgumentException e) {}
    }


    public void testBeanAccessors() throws IOException{
	SpringXmlBeanFactory beanFactory;
	Object bean;

	beanFactory = new SpringXmlBeanFactory();
	
	assertFalse(beanFactory.containsBean("bean"));
	assertNull(beanFactory.getBean("bean"));

	beanFactory.load(resource);	

	assertTrue(beanFactory.containsBean("bean"));
	assertNotNull(beanFactory.getBean("bean"));
	assertNotNull((bean = beanFactory.getBean("bean")));
	assertTrue(bean instanceof SpringXmlBeanFactory);
	
	assertNull((bean = beanFactory.getBeanOfType
		    (TestCase.class)));
	assertNotNull((bean = beanFactory.getBeanOfType
		       (BeanFactory.class)));
	assertTrue(bean instanceof BeanFactory);

	assertNull((bean = beanFactory.getBeanOfType
		    (new Class[]{ TestCase.class })));

	assertNotNull((bean = beanFactory.getBeanOfType
		       (new Class[]{ 
			   BeanFactory.class,
			   BeanWrapperSupport.class
		       })));
	assertTrue(bean instanceof BeanFactory &&
		   bean instanceof BeanWrapperSupport);
		
	assertNull(beanFactory.getBeanOfType
		    (new Class[]{ 
			BeanFactory.class,
			TestCase.class
		    }));

	assertTrue(beanFactory.isSingleton("bean"));
	assertFalse(beanFactory.isSingleton("bean-prototype"));
    }

    public void testBeanWrappers() {
	SpringXmlBeanFactory beanFactory;
	BeanWrapper beanWrapper;

	beanFactory = new SpringXmlBeanFactory();
	
	assertNotNull((beanWrapper = beanFactory.assignBeanWrapper
		       (new Object())));
	beanFactory.recycleBeanWrapper(beanWrapper);
    }

}
