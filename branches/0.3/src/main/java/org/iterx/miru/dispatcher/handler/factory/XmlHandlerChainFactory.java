/*
  org.iterx.miru.dispatcher.handler.factory.XmlHandlerChainFactory

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

import java.io.IOException;

import org.iterx.miru.dispatcher.handler.HandlerChainFactoryImpl;
import org.iterx.miru.bean.BeanProvider;
import org.iterx.miru.bean.BeanWrapperAware;

import org.iterx.miru.io.Resource;
import org.iterx.miru.io.resource.UriResource;

public class XmlHandlerChainFactory extends HandlerChainFactoryImpl {

    private BeanProvider beanProvider;

    public XmlHandlerChainFactory() {}

    public XmlHandlerChainFactory(BeanProvider beanProvider){

        setBeanProvider(beanProvider);
    }

    public BeanProvider getBeanProvider() {

        return beanProvider;
    }

    public void setBeanProvider(BeanProvider beanProvider) {

        if(beanProvider == null)
            throw new IllegalArgumentException("beanProvider == null");
        if(!(beanProvider instanceof BeanWrapperAware))
            throw new IllegalArgumentException("beanProvider is not BeanWrapperAware.");

        synchronized(this)  {
            this.beanProvider = beanProvider;
        }
    }

    public void load(String uri) throws IOException {
        assert (beanProvider != null) : "beanProvider == null";
        Resource resource;

        if((resource = new UriResource(uri)).exists()) load(resource);
        else throw new IllegalArgumentException
                       ("Resource [" + uri + "] does not exist.");
    }


    public void load(Resource resource) throws IOException {
        assert (beanProvider != null) : "beanProvider == null";
        XmlHandlerChainParser parser;

        if(resource == null)
            throw new IllegalArgumentException("resource == null");

        parser = new XmlHandlerChainParser(this);
        parser.parse(resource);
    }

}