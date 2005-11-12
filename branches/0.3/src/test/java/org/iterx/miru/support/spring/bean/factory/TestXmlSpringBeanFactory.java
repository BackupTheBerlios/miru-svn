/*
  org.iterx.miru.spring.beans.TestXmlSpringBeanFactory

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

package org.iterx.miru.support.spring.bean.factory;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import junit.framework.TestCase;

import org.iterx.miru.bean.factory.BeanFactory;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.support.spring.bean.factory.XmlSpringBeanFactory;


public class TestXmlSpringBeanFactory extends TestCase {

    private MockResource resource;

    protected void setUp() throws URISyntaxException {
        byte[] data;

        data = ("<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" " +
                "\"http://www.springframework.org/dtd/spring-beans.dtd\">\n" +
                "<beans>" +
                "<bean id=\"bean\" singleton=\"true\" " +
                "class=\"org.iterx.miru.support.spring.bean.factory.XmlSpringBeanFactory\"/>" +
                "<bean id=\"bean-prototype\" singleton=\"false\" " +
                "class=\"org.iterx.miru.support.spring.bean.factory.XmlSpringBeanFactory\"/>" +
                "</beans>").getBytes();
        resource = new MockResource(new URI("spring-beans.xml"));
        resource.setData(data);
    }

    protected void tearDown() {

        resource = null;
    }


    public void testConstructors(){
        BeanFactory beanFactory;

        beanFactory = new XmlSpringBeanFactory();
        assertNotNull(beanFactory);

        beanFactory = new XmlSpringBeanFactory(beanFactory);

        beanFactory = new XmlSpringBeanFactory
            (new DefaultListableBeanFactory());

        try {
            beanFactory = new XmlSpringBeanFactory((BeanFactory) null);
            fail("XmlSpringBeanFactory initialised with null arguments");
        }
        catch(IllegalArgumentException e) {}

        try {
            beanFactory = new XmlSpringBeanFactory
                ((org.springframework.beans.factory.BeanFactory) null);
            fail("XmlSpringBeanFactory initialised with null arguments");
        }
        catch(IllegalArgumentException e) {}
    }


    public void testBeanAccessors() throws IOException{
        XmlSpringBeanFactory beanFactory;
        Object bean;

        beanFactory = new XmlSpringBeanFactory();

        assertFalse(beanFactory.containsBean("bean"));
        assertNull(beanFactory.getBean("bean"));

        beanFactory.load(resource);

        assertTrue(beanFactory.containsBean("bean"));
        assertNotNull(beanFactory.getBean("bean"));
        assertNotNull((bean = beanFactory.getBean("bean")));
        assertTrue(bean instanceof XmlSpringBeanFactory);

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
                BeanWrapperAware.class
            })));
        assertTrue(bean instanceof BeanFactory &&
                   bean instanceof BeanWrapperAware);

        assertNull(beanFactory.getBeanOfType
            (new Class[]{
                BeanFactory.class,
                TestCase.class
            }));

        assertTrue(beanFactory.isSingleton("bean"));
        assertFalse(beanFactory.isSingleton("bean-prototype"));
    }

    public void testBeanWrappers() {
        XmlSpringBeanFactory beanFactory;
        BeanWrapper beanWrapper;

        beanFactory = new XmlSpringBeanFactory();

        assertNotNull((beanWrapper = beanFactory.assignBeanWrapper
            (new Object())));
        beanFactory.recycleBeanWrapper(beanWrapper);
    }

}
