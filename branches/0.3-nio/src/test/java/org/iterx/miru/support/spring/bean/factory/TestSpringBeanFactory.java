/*
  org.iterx.miru.spring.beans.TestSpringBeanFactory

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

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import junit.framework.TestCase;

import org.iterx.miru.bean.factory.BeanFactory;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.bean.BeanWrapperAware;
import org.iterx.miru.support.spring.bean.factory.SpringBeanFactory;

public class TestSpringBeanFactory extends TestCase {


    public void testConstructors(){
    BeanFactory beanFactory;

    beanFactory = new SpringBeanFactory();
    assertNotNull(beanFactory);

    beanFactory = new SpringBeanFactory(beanFactory);

    beanFactory = new SpringBeanFactory
        (new DefaultListableBeanFactory());

    try {
        beanFactory = new SpringBeanFactory((BeanFactory) null);
        fail("SpringBeanFactory initialised with null arguments");
    }
    catch(IllegalArgumentException e) {}

    try {
        beanFactory = new SpringBeanFactory
        ((org.springframework.beans.factory.BeanFactory) null);
        fail("SpringBeanFactory initialised with null arguments");
    }
    catch(IllegalArgumentException e) {}

    }


    public void testBeanAccessors() {
    DefaultListableBeanFactory springFactory;
    RootBeanDefinition beanDefinition;
    BeanFactory beanFactory;

    Object bean;

    springFactory = new DefaultListableBeanFactory();
    beanFactory = new SpringBeanFactory(springFactory);

    assertFalse(beanFactory.containsBean("bean"));
    assertNull(beanFactory.getBean("bean"));

    beanDefinition = new RootBeanDefinition
        (SpringBeanFactory.class, 0);
    beanDefinition.setSingleton(true);

    springFactory.registerBeanDefinition("bean", beanDefinition);

    assertTrue(beanFactory.containsBean("bean"));
    assertNotNull(beanFactory.getBean("bean"));
    assertNotNull((bean = beanFactory.getBean("bean")));
    assertTrue(bean instanceof SpringBeanFactory);

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


    beanDefinition.setSingleton(false);
    springFactory.registerBeanDefinition("bean-prototype",
                                         beanDefinition);

    assertFalse(beanFactory.isSingleton("bean-prototype"));
    }

    public void testBeanWrappers() {
    SpringBeanFactory beanFactory;
    BeanWrapper beanWrapper;

    beanFactory = new SpringBeanFactory();

    assertNotNull((beanWrapper = beanFactory.assignBeanWrapper
               (new Object())));
    beanFactory.recycleBeanWrapper(beanWrapper);
    }

}
