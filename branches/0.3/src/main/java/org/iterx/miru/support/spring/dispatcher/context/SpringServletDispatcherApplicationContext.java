/*
  org.iterx.miru.support.spring.dispatcher.context.SpringServletDispatcherApplicationContext

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
package org.iterx.miru.support.spring.dispatcher.context;

import javax.servlet.ServletContext;

import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.bean.BeanFactory;

public class SpringServletDispatcherApplicationContext extends SpringDispatcherApplicationContext {

    private ServletContext servletContext;

    public SpringServletDispatcherApplicationContext() {}

    public SpringServletDispatcherApplicationContext(ServletContext servletContext) {

        if(servletContext == null)
            throw new IllegalArgumentException("servletContext == null");
        this.servletContext = servletContext;
    }

    public SpringServletDispatcherApplicationContext(ApplicationContext parent) {

        super(parent);
    }

    public SpringServletDispatcherApplicationContext(ApplicationContext parent,
                                                     ServletContext servletContext)  {
	
        super(parent);
        if(servletContext == null)
            throw new IllegalArgumentException("servletContext == null");
        this.servletContext = servletContext;
    }

    public SpringServletDispatcherApplicationContext(BeanFactory beanFactory) {

        super(beanFactory);
    }

    public SpringServletDispatcherApplicationContext(BeanFactory beanFactory,
                                                     ServletContext servletContext) {
        super(beanFactory);
        if(servletContext == null)
            throw new IllegalArgumentException("servletContext == null");
        this.servletContext = servletContext;

    }

    public SpringServletDispatcherApplicationContext
        (org.springframework.beans.factory.BeanFactory beanFactory) {

        super(beanFactory);
    }

    public SpringServletDispatcherApplicationContext
        (org.springframework.beans.factory.BeanFactory beanFactory,
         ServletContext servletContext) {

        super(beanFactory);
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

}
