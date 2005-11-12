/*
  org.iterx.miru.dispatcher.resolver.resource.ClassPathResourceResolver

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

package org.iterx.miru.dispatcher.resolver.resource;

import java.net.URI;

import org.iterx.miru.io.Resource;
import org.iterx.miru.io.resource.ClassPathResource;
import org.iterx.miru.dispatcher.resolver.ResourceResolver;

public class ClassPathResourceResolver implements ResourceResolver {

    protected ClassLoader classLoader;

    public ClassPathResourceResolver() {}

    public ClassPathResourceResolver(ClassLoader classLoader) {

        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {

        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        if(classLoader == null)
            throw new IllegalArgumentException("classLoader == null");

        this.classLoader = classLoader;
    }

    public Resource resolve(URI uri) {
        Resource resource;

        resource = new ClassPathResource(uri, classLoader);
        return (resource.exists())? resource : null;
    }

}
