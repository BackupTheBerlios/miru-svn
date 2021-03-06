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

import org.iterx.miru.bean.BeanProvider;

import org.iterx.miru.io.Resource;
import org.iterx.miru.io.stream.StreamSource;
import org.iterx.miru.io.Loadable;
import org.iterx.miru.io.stream.UriStreamResource;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;

public class XmlHandlerChainFactory<S extends RequestContext, T extends ResponseContext> extends HandlerChainFactoryImpl<S, T>
    implements Loadable {

    public XmlHandlerChainFactory() {}

    public XmlHandlerChainFactory(BeanProvider beanProvider){

        super(beanProvider);
    }

    public void load(String uri) throws IOException {
        assert (getBeanProvider() != null) : "beanProvider == null";
        Resource resource;

        if((resource = new UriStreamResource(uri)).exists()) load(resource);
        else throw new IllegalArgumentException
                       ("Resource [" + uri + "] does not exist.");
    }


    public void load(Resource resource) throws IOException {
        assert (getBeanProvider() != null) : "beanProvider == null";
        XmlHandlerChainParser<S, T> parser;

        if(resource == null)
            throw new IllegalArgumentException("stream == null");
        if(!(resource instanceof StreamSource))
            throw new IllegalArgumentException("stream is not a 'StreamSource'");


        parser = new XmlHandlerChainParser<S, T>(this);
        parser.parse((StreamSource) resource);
    }

}
