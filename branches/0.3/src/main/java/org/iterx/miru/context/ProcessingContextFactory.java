/*
  org.iterx.miru.context.ProcessingContextFactory

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

package org.iterx.miru.context;

public abstract class ProcessingContextFactory implements ProcessingContextProvider {

    private static ProcessingContextFactory processingContextFactory;

    public static ProcessingContextFactory getProcessingContextFactory() {

        if(processingContextFactory == null)
            processingContextFactory = new ProcessingContextFactoryImpl();
        return processingContextFactory;
    }

    public static void setProcessingContextFactory
        (ProcessingContextFactory processingContextFactory) {

        if(processingContextFactory == null)
            throw new IllegalArgumentException("processingContextFactory == null");

        ProcessingContextFactory.processingContextFactory = processingContextFactory;
    }
}
