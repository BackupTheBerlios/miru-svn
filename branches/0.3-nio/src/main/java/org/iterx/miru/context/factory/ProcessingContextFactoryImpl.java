/*
  org.iterx.miru.context.factory.ProcessingContextFactoryImpl

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
package org.iterx.miru.context.factory;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.iterx.miru.resolver.ContextResolver;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.ProcessingContextImpl;
import org.iterx.miru.context.ProcessingContext;

public class ProcessingContextFactoryImpl<S extends RequestContext, T extends ResponseContext> extends ProcessingContextFactory<S, T> {

    private Map<String, ContextResolver> contextResolvers = new HashMap<String, ContextResolver>();

    public String[] getContextResolverNames() {
        Set<String> names;

        names = contextResolvers.keySet();
        return names.toArray(new String[names.size()]);
    }

    public ContextResolver getContextResolver(String name) {

        return contextResolvers.get(name);
    }

    public void setContextResolver(String name,
                                   ContextResolver contextResolver) {
        if(contextResolver == null)
            contextResolvers.remove(name);
        else contextResolvers.put(name, contextResolver);
    }


    public ProcessingContext<S, T> getProcessingContext(S request, T response) {
        ProcessingContext<S, T> processingContext;

        processingContext = new ProcessingContextImpl<S, T>(request, response);

        for(Map.Entry<String, ContextResolver> entry : contextResolvers.entrySet()) {
            processingContext.setAttribute(entry.getKey(),
                                           entry.getValue());
        }
        return processingContext;
    }


}
