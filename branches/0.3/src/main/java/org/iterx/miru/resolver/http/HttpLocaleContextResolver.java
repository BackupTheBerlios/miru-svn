/*
  org.iterx.miru.resolver.http.HttpLocaleContextResolver

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

package org.iterx.miru.resolver.http;

import java.util.Locale;

import org.iterx.miru.context.http.HttpRequestContext;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ResponseContext;

import org.iterx.miru.resolver.ContextResolver;

public class HttpLocaleContextResolver<S extends HttpRequestContext, T extends ResponseContext> implements ContextResolver<Locale, S, T> {

    private static final String ACCEPT_LANGUAGE  = "Accept-Language";

    private Locale defaultLocale;

    public HttpLocaleContextResolver() {

        defaultLocale = Locale.getDefault();
    }

    public Locale getDefaultLocale() {

        return defaultLocale;
    }

    public void setDefaultLocale(Locale defaultLocale) {

        if(defaultLocale == null)
            throw new IllegalArgumentException("defaultLocale == null");
        this.defaultLocale = defaultLocale;
    }



    public Locale resolve(ProcessingContext<? extends S, ? extends T> processingContext) {
        HttpRequestContext request;
        String string;

        request = processingContext.getRequestContext();
        if((string = request.getHeader(ACCEPT_LANGUAGE)) != null &&
           string.length() > 0) {
            int index;

            string = (string.split("[,;]", 2))[0];
            if((index = string.indexOf('-')) != -1) {
                return new Locale(string.substring(0, index),
                                  string.substring(index + 1));
            }
            return new Locale(string);
        }

        return defaultLocale;
    }

}
