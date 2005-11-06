/*
  org.iterx.miru.pipeline.PerfTestDispatcher

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
package org.iterx.miru.dispatcher;

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

import org.iterx.miru.io.StreamSource;
import org.iterx.miru.io.StreamTarget;

import org.iterx.miru.bean.factory.BeanFactoryImpl;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.http.HttpRequestContextImpl;
import org.iterx.miru.context.http.HttpResponseContextImpl;
import org.iterx.miru.context.ProcessingContextFactory;

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.handler.HandlerChain;
import org.iterx.miru.dispatcher.handler.HandlerWrapper;
import org.iterx.miru.dispatcher.handler.factory.HandlerChainFactoryImpl;


public class PerfTestDispatcher extends TestCase {

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

          private ProcessingContextFactory processingContextFactory;
          private Dispatcher dispatcher;

          {
              HandlerChainFactoryImpl handlerChainFactory;

              Runtime runtime;
              long memory;
              runtime = Runtime.getRuntime();

              System.gc();
              memory = runtime.freeMemory();


              handlerChainFactory = new HandlerChainFactoryImpl(new BeanFactoryImpl());
              HandlerWrapper handlerWrapper = handlerChainFactory.assignHandlerWrapper
                  (handlerChainFactory.createHandlerChain());
              handlerWrapper.setHandler(new SimpleHandler());
              handlerChainFactory.addHandlerChain((HandlerChain) handlerWrapper.getWrappedInstance());
              handlerChainFactory.recycleHandlerWrapper(handlerWrapper);

              dispatcher = new Dispatcher(handlerChainFactory.getHandlerChains());

              memory -= runtime.freeMemory();

              System.out.println("Memory: " +
                                 memory +
                                 " bytes");

              processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
              System.gc();
          }

          public DispatcherTest(String name) {

              super(name);
          }


          private static String createMessage() {

              return  ("<message><thread>" +
                       Thread.currentThread() +
                       "</thread></message>");
          }

          public void testDispatcher() throws Exception {
              ProcessingContext processingContext;
              StringReader reader;
              StringWriter writer;
              String message;

              message = createMessage();
              reader = new StringReader(message);
              writer = new StringWriter();
              processingContext = processingContextFactory.getProcessingContext
                  (new HttpRequestContextImpl(reader), new HttpResponseContextImpl(writer));

              assertEquals(Dispatcher.OK,
                           dispatcher.dispatch(processingContext));
              reader.close();
              writer.close();
              assertEquals(message, writer.toString());
          }
      }


    private static class SimpleHandler implements ContentHandler {

        public int execute(ProcessingContext processingContext) {
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
