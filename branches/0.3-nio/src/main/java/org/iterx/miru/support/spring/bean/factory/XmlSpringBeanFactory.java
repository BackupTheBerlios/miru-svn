/*
  org.iterx.miru.support.spring.bean.XmlSpringBeanFactory

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

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.InputStreamResource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import org.iterx.miru.io.Resource;
import org.iterx.miru.io.Loadable;
import org.iterx.miru.io.stream.StreamSource;
import org.iterx.miru.io.stream.UriStreamResource;
import org.iterx.miru.bean.factory.BeanFactory;
import org.iterx.miru.bean.BeanWrapper;
import org.iterx.miru.support.spring.bean.SpringBeanWrapper;

public class XmlSpringBeanFactory extends SpringBeanFactory implements Loadable  {


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

    public void load(String uri) throws IOException {
        Resource resource;

        if((resource = new UriStreamResource(uri)).exists()) load(resource);
        else throw new IllegalArgumentException
                       ("Resource [" + uri + "] does not exist.");
    }


    public void load(Resource resource) throws IOException {

        if(resource == null)
            throw new IllegalArgumentException("stream == null");
        if(!(resource instanceof StreamSource))
            throw new IllegalArgumentException("stream is not a 'StreamSource'");
        ((XmlSpringBeanFactoryProxy) beanFactory).parse((StreamSource) resource);
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

        private void parse(StreamSource source) throws IOException {
            InputStream in;

            if((in = source.getInputStream()) != null)
                reader.loadBeanDefinitions(new InputStreamResource(in));
            else throw new IOException("Invalid xml stream [" + source + "].");
        }

        private SpringBeanWrapper assignBeanWrapper(Object object) {
            BeanWrapperImpl beanWrapper;

            initBeanWrapper(beanWrapper = new BeanWrapperImpl(object));
            return new SpringBeanWrapper(beanWrapper);
        }

    }

}
