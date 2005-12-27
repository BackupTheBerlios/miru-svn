/*
  org.iterx.miru.resolver.MockContextResolver

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


package org.iterx.miru.resolver;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;


public class MockContextResolver<S extends RequestContext, T extends ResponseContext> implements ContextResolver<Object, S, T> {

    private Object resolved;

    public MockContextResolver(Object resolved) {

        this.resolved = resolved;
    }

    public Object getResolved() {

        return resolved;
    }

    public void setResolved(Object resolved) {

        this.resolved = resolved;
    }


    public Object resolve(ProcessingContext<? extends S, ? extends T> processingContext) {

        return resolved;
    }

}
