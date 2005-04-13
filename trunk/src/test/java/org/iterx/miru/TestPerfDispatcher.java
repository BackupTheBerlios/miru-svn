/*
  org.iterx.miru.pipeline.TestPerfDispatcher

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
package org.iterx.miru;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.extensions.RepeatedTest;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;


import org.iterx.miru.Dispatcher;
import org.iterx.miru.io.StreamSource;
import org.iterx.miru.io.StreamTarget;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.WebRequestContextImpl;
import org.iterx.miru.context.WebResponseContextImpl;

import org.iterx.miru.handler.Handler;
import org.iterx.miru.handler.HandlerMapping;
import org.iterx.miru.handler.HandlerMappingImpl;

import org.iterx.miru.interceptor.HandlerInterceptor;


public class TestPerfDispatcher extends TestCase {

    public static final int ITERATIONS  = 100;
    public static final int CONCURRENCY = 100;
    public static final int TIMEOUT     = 10000;

    public static Test suite() {
        Test test;

        test = new DispatcherTest("testDispatcher");
        
        test = new RepeatedTest(test, ITERATIONS);
        test = new TimedTest(test, TIMEOUT);
        return new LoadTest(test, CONCURRENCY);
    }

    public static class DispatcherTest extends TestCase {
        
        private Dispatcher dispatcher;
        {   
            HandlerMapping handlerMapping;
            Runtime runtime;
            long memory;
            runtime = Runtime.getRuntime();

            System.gc();
            memory = runtime.freeMemory();            

            handlerMapping = new HandlerMappingImpl();
            handlerMapping.addHandler
                ("default",
                 new SimpleHandler(),
                 new HandlerInterceptor[] { new SimpleHandlerInterceptor() }); 
            dispatcher = new Dispatcher(handlerMapping);

            memory -= runtime.freeMemory();

            System.out.println("Memory: " + 
                               memory +
                               " bytes");
            System.gc();
        }

        public DispatcherTest(String name) {

            super(name);
        }
        
        
        private static String createMessage() {
            
            return  ("<parent><child>" + 
                     Thread.currentThread() + 
                     "</child></parent>");
        }

        public void testDispatcher() throws Exception {
            ProcessingContext processingContext;
            StringReader reader;
            StringWriter writer;
            String message;

            message = createMessage();
            reader = new StringReader(message);
            writer = new StringWriter();
            processingContext = new ProcessingContext
                (new WebRequestContextImpl(reader),
                 new WebResponseContextImpl(writer));
            
            assertEquals(Dispatcher.OK,
                         dispatcher.dispatch(processingContext));
            reader.close();
            writer.close();
            assertEquals(message, writer.toString());            
        }
    }


    private static class SimpleHandlerInterceptor implements HandlerInterceptor {

        public boolean preHandle(ProcessingContext processingContext) {

            return (processingContext.getRequestContext() instanceof StreamSource &&
                    processingContext.getResponseContext() instanceof StreamTarget);
        }

        public void postHandle(ProcessingContext processingContext) {}
        

    }

    private static class SimpleHandler implements Handler {

        public int handle(ProcessingContext processingContext) {
            StreamSource source;
            StreamTarget target;

            source = (StreamSource) processingContext.getRequestContext();
            target = (StreamTarget) processingContext.getResponseContext();

            try {
                Reader reader;
                Writer writer;
                char[] buffer;
                int length;
            
                reader = source.getReader();
                writer = target.getWriter();

                buffer = new char[256];
                while((length = reader.read(buffer, 0, 256)) != -1) {
                    writer.write(buffer, 0, length); 
                }
                return Dispatcher.OK;
            }
            catch(IOException e) {}
            
            return Dispatcher.ERROR;
        }
    }
    
}
