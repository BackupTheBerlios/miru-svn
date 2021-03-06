/*
  org.iterx.miru.resolver.stream.UriStreamResourceResolver

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

package org.iterx.miru.resolver.resource;

import java.io.File;
import java.net.URI;

import org.iterx.miru.io.Resource;
import org.iterx.miru.io.stream.UriStreamResource;
import org.iterx.miru.resolver.ResourceResolver;

public class UriStreamResourceResolver implements ResourceResolver {

    protected static final URI BASE_URI =
        (new File(System.getProperty("user.dir"))).toURI();

    protected URI baseUri;

    public UriStreamResourceResolver() {

        baseUri = BASE_URI;
    }

    public UriStreamResourceResolver(URI base) {

        setBaseUri(base);
    }

    public URI getBaseUri() {

        return baseUri;
    }

    public void setBaseUri(URI base) {
        if(base == null)
            throw new IllegalArgumentException("baseUri == null");
        if(base.getScheme() == null)
            throw new IllegalArgumentException("Relative baseUri uri [" +
                                               base + "]");
        this.baseUri = base;
    }

    public Resource resolve(URI uri) {
        Resource resource;

        if(uri.getScheme() == null) uri = baseUri.resolve(uri);
        resource = new UriStreamResource(uri);

        return (resource.exists())? resource : null;
    }

}
