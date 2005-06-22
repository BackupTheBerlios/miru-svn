/*
  org.iterx.miru.servlet.spring.context.SpringServletApplicationContext

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
package org.iterx.miru.support.servlet.spring.context;

import javax.servlet.ServletContext;

import org.iterx.miru.context.ApplicationContext;

import org.iterx.miru.beans.BeanFactory;

import org.iterx.miru.support.spring.context.SpringApplicationContext;

public class SpringServletApplicationContext extends SpringApplicationContext {

    protected ServletContext m_servletContext;

    public SpringServletApplicationContext() {    

        super();
    }

    public SpringServletApplicationContext(ServletContext servletContext) {

        super();
        if(servletContext == null)
	    throw new IllegalArgumentException("servletContext == null");
	m_servletContext = servletContext;
    }

    public SpringServletApplicationContext(ApplicationContext parent) {

        super(parent);
    }

    public SpringServletApplicationContext(ApplicationContext parent,
                                           ServletContext servletContext)  {
	
        super(parent);
        if(servletContext == null)
	    throw new IllegalArgumentException("servletContext == null");
	m_servletContext = servletContext;
    }

    public SpringServletApplicationContext(BeanFactory beanFactory) {

        super(beanFactory);
    }

    public SpringServletApplicationContext(BeanFactory beanFactory,
                                           ServletContext servletContext) {
        super(beanFactory);
        if(servletContext == null)
	    throw new IllegalArgumentException("servletContext == null");
        m_servletContext = servletContext;

    }

    public SpringServletApplicationContext
        (org.springframework.beans.factory.BeanFactory beanFactory) {

        super(beanFactory);
    }

    public SpringServletApplicationContext
        (org.springframework.beans.factory.BeanFactory beanFactory,
         ServletContext servletContext) {

        super(beanFactory);
        if(servletContext == null)
	    throw new IllegalArgumentException("servletContext == null");
        m_servletContext = servletContext;        
    }


    public ServletContext getServletContext() {

	return m_servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
	if(servletContext == null)
	    throw new IllegalArgumentException("servletContext == null");

	synchronized(this) {
	    m_servletContext = servletContext;
	}
    }



}
