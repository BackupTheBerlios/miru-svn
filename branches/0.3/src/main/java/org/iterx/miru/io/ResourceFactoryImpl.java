/*
  org.iterx.miru.io.ResourceFactoryImpl

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

package org.iterx.miru.io;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.dispatcher.resolver.ResourceResolver;

import org.iterx.util.ArrayUtils;

public class ResourceFactoryImpl extends ResourceFactory {

    private final static Log LOGGER = LogFactory.getLog(ResourceFactoryImpl.class);

    protected ResourceResolver[] resourceResolvers;

    public ResourceFactoryImpl() {

        resourceResolvers = new ResourceResolver[0];
    }

    public ResourceResolver[] getResourceResolvers() {

        return resourceResolvers;
    }

    public void setResourceResolvers(ResourceResolver[] resourceResolvers) {

        if(resourceResolvers == null)
            throw new IllegalArgumentException("resourceResolvers == null");
        this.resourceResolvers = resourceResolvers;
    }

    public void addResourceResolver(ResourceResolver resourceResolver) {

        resourceResolvers = 
            (ResourceResolver[]) ArrayUtils.add(resourceResolvers, resourceResolver);
    }

    public void removeResourceResolver(ResourceResolver resourceResolver) {

        resourceResolvers = 
            (ResourceResolver[]) ArrayUtils.remove(resourceResolvers, resourceResolver);
    }

    public Resource getResource(URI uri) {
        assert (uri != null) : "uri == null";

        for(int i = 0; i < resourceResolvers.length; i++) {
            Resource resource;

            if((resource = (resourceResolvers[i]).resolve(uri)) != null)
                return resource;
        }


        LOGGER.warn("Resource [" + uri + "] not found.");
        return null;
    }
}
