/*
  org.iterx.miru.context.PerfTestProcessingContextImpl

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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.extensions.RepeatedTest;
import com.clarkware.junitperf.TimedTest;
import com.clarkware.junitperf.LoadTest;
import org.iterx.miru.context.http.MockHttpRequestContext;
import org.iterx.miru.context.http.MockHttpResponseContext;

public class PerfTestProcessingContextImpl extends TestCase {

    public static final int ITERATIONS  = 100;
    public static final int CONCURRENCY = 100;
    public static final int TIMEOUT     = 10000;

    public static Test suite() {
        Test test;

        test = new ProcessingContextTest("testProcessingContext");
        test = new RepeatedTest(test, ITERATIONS);
        test = new TimedTest(test, TIMEOUT);
        return new LoadTest(test, CONCURRENCY);
    }

    public static class ProcessingContextTest extends TestCase {

        private static ProcessingContextFactory processingContextFactory;

        static {
            processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
        }

        public ProcessingContextTest(String name) {

            super(name);
        }

        public void testProcessingContext() {
            ProcessingContext context;

            context = processingContextFactory.getProcessingContext
                (MockHttpRequestContext.newInstance("/"),
                 MockHttpResponseContext.newInstance());
        }

    }


}