/*
  org.iterx.miru.resolver.resource.UriResourceResolver

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
import java.net.URISyntaxException;

import org.iterx.miru.io.Resource;
import org.iterx.miru.io.resource.UriResource;
import org.iterx.miru.resolver.ResourceResolver;

public class UriResourceResolver implements ResourceResolver {

    protected static final URI BASE_URI =
	(new File(System.getProperty("user.dir"))).toURI();
    
    protected URI base;

    public UriResourceResolver() {

	base = BASE_URI;
    }

    public UriResourceResolver(URI base) {

	setBase(base);
    }

    public URI getBase() {

	return base;
    }
    
    public void setBase(URI base) {
	if(base == null)
	    throw new IllegalArgumentException("base == null");
	if(base.getScheme() == null) 
	    throw new IllegalArgumentException("Relative base uri [" +
					       base + "]");

	this.base = base;
    }

    public Resource resolve(URI uri) {
	Resource resource;

	if(uri.getScheme() == null) uri = base.resolve(uri);
	resource = new UriResource(uri);
	return (resource.exists())? resource : null;
    }

}
