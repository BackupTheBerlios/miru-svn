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

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.extensions.RepeatedTest;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.http.HttpRequestContextImpl;
import org.iterx.miru.context.http.HttpResponseContextImpl;
import org.iterx.miru.context.ProcessingContextFactory;

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

        private ProcessingContextFactory processingContextFactory;
        private PipelineChain[] pipelines;
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
            pipelines = new PipelineChainImpl[CONCURRENCY];
            for(int i = CONCURRENCY; i-- > 0; ) {
                PipelineChainImpl pipelineChain;
                XmlSerializer xmlSerializer;
                pipelineChain = (new PipelineChainImpl
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

        private PipelineChain getPipeline() {

            synchronized(pipelines) {
                return pipelines[(next = (++next % CONCURRENCY))];
            }
        }

        private void recyclePipeline(PipelineChain pipelineChain) {

            synchronized(pipelines) {
                pipelines[(recycle = (++recycle % CONCURRENCY))] = pipelineChain;
            }
        }

        public void testPipeline() throws Exception {
            ProcessingContext processingContext;
            StringReader reader;
            StringWriter writer;
            PipelineChain pipelineChain;
            String message;

            message = createMessage();
            reader = new StringReader(message);
            writer = new StringWriter();
            processingContext = processingContextFactory.getProcessingContext
                                  (new HttpRequestContextImpl(reader),
                                   new HttpResponseContextImpl(writer));

            pipelineChain = getPipeline();
            pipelineChain.execute(processingContext);
            recyclePipeline(pipelineChain);

            reader.close();
            writer.close();

            assertTrue((writer.toString()).length() > 0 );
            assertEquals(message, writer.toString());
        }
    }
}
