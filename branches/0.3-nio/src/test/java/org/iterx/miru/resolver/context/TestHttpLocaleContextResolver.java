/*
  org.iterx.miru.resolver.context.TestHttpLocaleContextResolver

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

package org.iterx.miru.resolver.context;

import java.util.Locale;

import junit.framework.TestCase;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.MockProcessingContext;
import org.iterx.miru.context.http.MockHttpRequestContext;
import org.iterx.miru.context.http.MockHttpResponseContext;
import org.iterx.miru.context.http.HttpRequestContext;
import org.iterx.miru.context.http.HttpResponseContext;

public class TestHttpLocaleContextResolver extends TestCase {

    private static final String ACCEPT_LANGUAGE  = "Accept-Language";

    private ProcessingContext<HttpRequestContext, HttpResponseContext> processingContext;

    private static Object[][] LOCALES = {
        { "en", Locale.ENGLISH },
        { "en;qa=1.0", Locale.ENGLISH },
        { "en-GB", Locale.UK },
        { "en-GB;qa=1.0", Locale.UK },
        { "ja", Locale.JAPANESE },
        { "ja,en", Locale.JAPANESE },
        { "ja;qa=1.0,en", Locale.JAPANESE },
        { "ja-JP", Locale.JAPAN },
        { "ja-JP,en", Locale.JAPAN },
        { "ja-JP;qa=1.0,en", Locale.JAPAN },
        { "en-cockney", new Locale("en", "COCKNEY") },
        { "x-pig-latin", new Locale("x", "PIG-LATIN") },
        { "", Locale.getDefault() },
        { null, Locale.getDefault() }
    };


    protected void setUp() {

        processingContext = new MockProcessingContext<HttpRequestContext, HttpResponseContext>
            (MockHttpRequestContext.newInstance("/"),
             MockHttpResponseContext.newInstance());

    }

    protected void tearDown() {

        processingContext = null;
    }

    public void testConstructors() {
        HttpLocaleContextResolver<HttpRequestContext, HttpResponseContext> resolver;

        resolver = new HttpLocaleContextResolver<HttpRequestContext, HttpResponseContext>();
        assertNotNull(resolver);
    }


    public void testDefaultLocaleAccessors() {
        HttpLocaleContextResolver<HttpRequestContext, HttpResponseContext> resolver;
        Locale locale;


        resolver = new HttpLocaleContextResolver<HttpRequestContext, HttpResponseContext>();

        assertEquals(locale = Locale.getDefault(), resolver.getDefaultLocale());
        assertEquals(locale, resolver.resolve(processingContext));

        resolver.setDefaultLocale(locale = new Locale("x", "PIG-LATIN"));
        assertEquals(locale, resolver.getDefaultLocale());
        assertEquals(locale, resolver.resolve(processingContext));
    }

    public void testResolve() {
        HttpLocaleContextResolver<HttpRequestContext, HttpResponseContext> resolver;
        MockHttpRequestContext request;
        Locale locale;

        request = (MockHttpRequestContext) processingContext.getRequestContext();
        resolver = new HttpLocaleContextResolver<HttpRequestContext, HttpResponseContext>();

        locale = resolver.resolve(processingContext);
        assertEquals(Locale.getDefault(), locale);

        for(int i = 0; i < LOCALES.length; i++) {
            request.setHeader(ACCEPT_LANGUAGE,
                              (String) LOCALES[i][0]);
            locale = resolver.resolve(processingContext);
            assertEquals(LOCALES[i][1], locale);
        }
    }



}
