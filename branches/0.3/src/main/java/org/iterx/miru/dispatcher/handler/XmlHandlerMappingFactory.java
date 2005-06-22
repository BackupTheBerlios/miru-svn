/*
  org.iterx.miru.dispatcher.handler.XmlHandlerMappingFactory

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

package org.iterx.miru.dispatcher.handler;

import java.io.IOException;

import org.iterx.miru.io.Resource;
import org.iterx.miru.dispatcher.handler.HandlerMapping;
import org.iterx.miru.dispatcher.handler.HandlerMappingFactoryImpl;

import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.context.ApplicationContextAware;


public class XmlHandlerMappingFactory extends HandlerMappingFactoryImpl
    implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public XmlHandlerMappingFactory() {

        super();
    }

    public XmlHandlerMappingFactory(HandlerMapping handlerMapping) {

        super(handlerMapping);
    }


    public void setApplicationContext(ApplicationContext applicationContext) {
        if(applicationContext == null)
            throw new IllegalArgumentException("applicationContext == null");

    this.applicationContext = applicationContext;
    }

    public void load(Resource resource) throws IOException {
    XmlHandlerMappingParser parser;

    if(resource == null)
        throw new IllegalArgumentException("resource == null");

    parser = new XmlHandlerMappingParser(applicationContext,
                                         getHandlerMapping());
    parser.parse(resource);
    }

}

