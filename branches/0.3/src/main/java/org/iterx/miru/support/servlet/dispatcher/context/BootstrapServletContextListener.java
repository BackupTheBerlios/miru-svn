/*
  org.iterx.miru.support.servlet.dispatcher.context.BootstrapServletContextListener

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

import java.util.Map;
import java.net.URL;
import java.io.IOException;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

import org.iterx.miru.io.Loadable;
import org.iterx.miru.io.resource.UriResource;
import org.iterx.miru.dispatcher.context.DispatcherApplicationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BootstrapServletContextListener implements ServletContextListener {

    private static final Log LOGGER = LogFactory.getLog(BootstrapServletContextListener.class);

    //TODO: Add context heirarchy -> based on servlet path
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        try {
            DispatcherApplicationContext applicationContext;
            ServletContext servletContext;
            String parameter;

            servletContext = servletContextEvent.getServletContext();

            applicationContext = new ServletDispatcherApplicationContext(servletContext);
            if((parameter = servletContext.getInitParameter(ServletDispatcherApplicationContext.BEANS)) != null &&
               applicationContext instanceof Loadable) {
                URL url;

                if((url = (servletContext.getResource(parameter))) != null)
                    ((Loadable) applicationContext).load(new UriResource(url.toURI()));
                else throw new IOException("Invalid resource ["+  parameter + "]");

            }
            servletContext.setAttribute((DispatcherApplicationContext.class).getName(),
                                        applicationContext);
        }
        catch(Exception e) {
            LOGGER.error("Initialisation failed.", e);
            throw new RuntimeException("Initialisation failed.", e);
        }

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext;

        servletContext = servletContextEvent.getServletContext();
        servletContext.removeAttribute((DispatcherApplicationContext.class).getName());
    }

}
