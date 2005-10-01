/*
  org.iterx.miru.spring.beans.XmlSpringBeanFactory

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

import java.io.InputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.InputStreamResource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import org.iterx.miru.io.Resource;
import org.iterx.miru.bean.BeanFactory;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.support.spring.bean.factory.SpringBeanFactory;
import org.iterx.miru.support.spring.bean.SpringBeanWrapper;

public class XmlSpringBeanFactory extends SpringBeanFactory  {

    protected static final Log logger =
        LogFactory.getLog(XmlSpringBeanFactory.class);

    public XmlSpringBeanFactory()  {

        beanFactory = new XmlSpringBeanFactoryProxy();
    }

    public XmlSpringBeanFactory(BeanFactory parent)  {

    if(parent == null)
        throw new IllegalArgumentException("parent == null");

        beanFactory = new XmlSpringBeanFactoryProxy();
        this.parent = parent;
    }

    public XmlSpringBeanFactory
        (org.springframework.beans.factory.BeanFactory parent) {

        if(parent == null)
            throw new IllegalArgumentException("parent == null");

        beanFactory = new XmlSpringBeanFactoryProxy(parent);
    }

    public void load(Resource resource) throws IOException {
        assert (resource != null) : "resource == null";

        ((XmlSpringBeanFactoryProxy) beanFactory).load(resource);
    }


    public BeanWrapper assignBeanWrapper(Object object) {
        assert (object != null) : "object == null";

        return ((XmlSpringBeanFactoryProxy) beanFactory).assignBeanWrapper(object);
    }


    private class XmlSpringBeanFactoryProxy extends DefaultListableBeanFactory {

        private final XmlBeanDefinitionReader reader =
            new XmlBeanDefinitionReader(this);

    private XmlSpringBeanFactoryProxy() {}

        private XmlSpringBeanFactoryProxy
            (org.springframework.beans.factory.BeanFactory parent) {

        super(parent);
        }

        private void load(Resource resource) throws IOException {
            InputStream in;

            if((in = resource.getInputStream()) != null)
                reader.loadBeanDefinitions(new InputStreamResource(in));
            else {
                if(logger.isDebugEnabled())
                    logger.debug("Invalid resource InputStream [" +
                                 resource +  "]");
                throw new RuntimeException("Invalid resource InputStream");
            }
        }

        private SpringBeanWrapper assignBeanWrapper(Object object) {
            BeanWrapperImpl beanWrapper;

            initBeanWrapper(beanWrapper = new BeanWrapperImpl(object));
            return new SpringBeanWrapper(beanWrapper);
        }

    }

}