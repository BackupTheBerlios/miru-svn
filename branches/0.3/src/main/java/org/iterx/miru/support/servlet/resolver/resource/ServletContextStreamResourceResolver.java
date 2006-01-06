/*
  org.iterx.miru.support.servlet.resolver.stream.ServletContextStreamResourceResolver

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


package org.iterx.miru.support.servlet.resolver.resource;

import java.net.URI;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.ServletContext;

import org.iterx.miru.resolver.ResourceResolver;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.stream.UriStreamResource;
import org.iterx.miru.context.ApplicationContextAware;
import org.iterx.miru.context.ApplicationContext;
import org.iterx.miru.support.servlet.context.ServletApplicationContext;

public class ServletContextStreamResourceResolver
    implements ResourceResolver, ApplicationContextAware {

    private ServletContext servletContext;
    private String path;

    public ServletContextStreamResourceResolver() {}

    public ServletContextStreamResourceResolver(ServletContext servletContext) {

        if(servletContext == null)
            throw new IllegalArgumentException("servletContext== null");
        setServletContext(servletContext);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {

        if(!(applicationContext instanceof ServletApplicationContext))
            throw new IllegalArgumentException("applicationContext is not a ServletApplicationContext.");
        setServletContext(((ServletApplicationContext) applicationContext).getServletContext());
    }

    public ServletContext getServletContext() {

        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {

        if(servletContext == null)
            throw new IllegalArgumentException("servletContext== null");
        String path;
        int next;

        path = (URI.create(servletContext.getRealPath("/"))).getPath();
        next = path.length() - 1;
        while(servletContext.getContext(path.substring(next)) != servletContext) {
            next = path.lastIndexOf('/', next - 1);
        }

        this.path = path.substring(next);
        this.servletContext = servletContext;
    }

    public Resource resolve(URI uri) {
        assert (servletContext != null) : "servletContext == null";
        Resource resource;

        resource = null;
        try {
            String path;
            URL url;

            if((path = uri.toString()).startsWith(this.path))
                path = path.substring(this.path.length() - 1);
            if((url = servletContext.getResource(path)) != null) {
                try {
                    resource = new UriStreamResource(url.toURI());
                }
                catch(URISyntaxException e){}
            }
        }
        catch(MalformedURLException e) {
            throw new RuntimeException("Invalid uri [" + uri + "]", e);
        }
        return resource;
    }

}
