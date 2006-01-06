/*
  org.iterx.miru.support.servlet.dispatcher.context.ServletDispatcherApplicationContext

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
package org.iterx.miru.support.servlet.dispatcher.context;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.iterx.miru.io.Loadable;
import org.iterx.miru.io.Resource;
import org.iterx.miru.dispatcher.context.DispatcherApplicationContextImpl;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.support.servlet.context.ServletApplicationContext;
import org.iterx.miru.support.servlet.context.http.HttpServletRequestContext;
import org.iterx.miru.support.servlet.context.http.HttpServletResponseContext;

public class ServletDispatcherApplicationContext<S extends HttpServletRequestContext, T extends HttpServletResponseContext> extends DispatcherApplicationContextImpl<S, T>
    implements ServletApplicationContext, Loadable {

    public static final String CHAINS = "chains";
    public static final String BEANS  = "beans";

    private ServletContext servletContext;

    public ServletDispatcherApplicationContext(ServletContext servletContext) {

        if(servletContext == null)
            throw new IllegalArgumentException("servletContext == null");
        this.servletContext = servletContext;
    }

    public ServletDispatcherApplicationContext(ApplicationContext applicationContext,
                                               ServletContext servletContext) {

        super(applicationContext);
        if(servletContext == null)
            throw new IllegalArgumentException("servletContext == null");

        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {

        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {

        if(servletContext == null)
            throw new IllegalArgumentException("servletContext == null");

        synchronized(this) {
            this.servletContext = servletContext;
        }
    }

    public void load(Resource resource) throws IOException {

        if(beanProvider instanceof Loadable) {
            synchronized(beanProvider) {
                ((Loadable) beanProvider).load(resource);
            }
        }
        else throw new IOException("BeanProvider does not support dynamic loading.");
    }
}
