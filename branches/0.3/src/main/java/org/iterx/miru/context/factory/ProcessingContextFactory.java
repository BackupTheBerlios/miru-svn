/*
  org.iterx.miru.context.factory.ProcessingContextFactory

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

import org.iterx.util.SystemUtils;
import org.iterx.miru.context.ProcessingContextProvider;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;

public abstract class ProcessingContextFactory<S extends RequestContext, T extends ResponseContext> implements ProcessingContextProvider<S, T> {

    private static Object processingContextFactory;

    public static <S extends RequestContext, T extends ResponseContext> ProcessingContextFactory<S, T> getProcessingContextFactory() {

        if(processingContextFactory == null) {
            String cls;

            if((cls = SystemUtils.getProperty((ProcessingContextFactory.class).getName())) != null) {
                try {
                    processingContextFactory = (Class.forName(cls)).newInstance();
                }
                catch(Exception e) {
                    throw new RuntimeException
                        ("Failed to create ProcessingContextFactory '" + cls + "'.", e);
                }
            }
            else processingContextFactory = new ProcessingContextFactoryImpl();
        }
        return (ProcessingContextFactory<S, T>) processingContextFactory;
    }

    public static <S extends RequestContext, T extends ResponseContext> void setProcessingContextFactory
        (ProcessingContextFactory<? extends S, ? extends T> processingContextFactory) {

        if(processingContextFactory == null)
            throw new IllegalArgumentException("processingContextFactory == null");

        ProcessingContextFactory.processingContextFactory = processingContextFactory;
    }
}
