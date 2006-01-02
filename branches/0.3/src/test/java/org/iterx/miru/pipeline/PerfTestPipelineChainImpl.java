/*
  org.iterx.miru.pipeline.TestPerfPipeline

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
package org.iterx.miru.pipeline;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.extensions.RepeatedTest;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.context.http.MockHttpRequestContext;
import org.iterx.miru.context.http.MockHttpResponseContext;
import org.iterx.miru.context.factory.ProcessingContextFactory;

import org.iterx.miru.pipeline.generator.XmlGenerator;
import org.iterx.miru.pipeline.serializer.XmlSerializer;

public class PerfTestPipelineChainImpl extends TestCase {

    public static final int ITERATIONS  = 100;
    public static final int CONCURRENCY = 100;
    public static final int TIMEOUT     = 10000;

    public static Test suite() {
        Test test;

        test = new PipelineChainImplTest("testPipeline");

        test = new RepeatedTest(test, ITERATIONS);
        test = new TimedTest(test, TIMEOUT);
        return new LoadTest(test, CONCURRENCY);
    }

    public static class PipelineChainImplTest extends TestCase {

        private ProcessingContextFactory<RequestContext, ResponseContext> processingContextFactory;
        private PipelineChain<RequestContext, ResponseContext>[] pipelines;
        private int next, recycle;
        {
            Runtime runtime;
            long memory;
            runtime = Runtime.getRuntime();

            System.gc();
            memory = runtime.freeMemory();

            (new PipelineChainImpl
             (new XmlGenerator(),
              new XmlSerializer())).init();

            memory -= runtime.freeMemory();
            System.out.println("Memory: " +
                               memory +
                               " bytes");

            memory = runtime.freeMemory();
            pipelines = (PipelineChain<RequestContext, ResponseContext>[]) new PipelineChainImpl[CONCURRENCY];
            for(int i = CONCURRENCY; i-- > 0; ) {
                PipelineChainImpl<RequestContext, ResponseContext> pipelineChain;
                XmlSerializer xmlSerializer;

                pipelineChain = (new PipelineChainImpl<RequestContext, ResponseContext>
                                 (new XmlGenerator(),
                                  xmlSerializer = new XmlSerializer()));
                xmlSerializer.setOmitXMLDeclaration(true);
                pipelineChain.init();
                pipelines[i] = pipelineChain;
            }
            memory -= runtime.freeMemory();
            System.out.println("Memory: " +
                               (memory / CONCURRENCY) +
                               " bytes/PipelineImpl");
            System.gc();
            processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
            next = recycle = -1;
        }

        public PipelineChainImplTest(String name) {

            super(name);
        }


        private static String createMessage() {

            return  ("<parent><child>" +
                     Thread.currentThread() +
                     "</child></parent>");
        }

        private PipelineChain<RequestContext, ResponseContext> getPipeline() {

            synchronized(pipelines) {
                return pipelines[(next = (++next % CONCURRENCY))];
            }
        }

        private void recyclePipeline(PipelineChain<RequestContext, ResponseContext> pipelineChain) {

            synchronized(pipelines) {
                pipelines[(recycle = (++recycle % CONCURRENCY))] = pipelineChain;
            }
        }

        public void testPipeline() throws Exception {
            ProcessingContext<RequestContext, ResponseContext> processingContext;
            PipelineChain<RequestContext, ResponseContext> pipelineChain;
            MockHttpRequestContext requestContext;
            MockHttpResponseContext responseContext;
            String message;

            message = createMessage();
            requestContext = MockHttpRequestContext.newInstance("/", message.getBytes());
            responseContext = MockHttpResponseContext.newInstance();

            processingContext = processingContextFactory.getProcessingContext(requestContext,
                                                                              responseContext);

            pipelineChain = getPipeline();
            pipelineChain.execute(processingContext);
            recyclePipeline(pipelineChain);

            assertEquals(message, new String(responseContext.getData()));
        }
    }
}
