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

import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.extensions.RepeatedTest;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.iterx.sax.OutputTarget;
import org.iterx.sax.helpers.XMLWriterImpl;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.WebRequestContextImpl;
import org.iterx.miru.context.WebResponseContextImpl;

import org.iterx.miru.pipeline.Pipeline;
import org.iterx.miru.pipeline.PipelineImpl;
import org.iterx.miru.pipeline.generator.SaxGenerator;
import org.iterx.miru.pipeline.serializer.SaxSerializer;

public class TestPerfPipelineImpl extends TestCase {

    public static final int ITERATIONS  = 100;
    public static final int CONCURRENCY = 100;
    public static final int TIMEOUT     = 10000;

    public static Test suite() {
        Test test;

        test = new PipelineImplTest("testPipeline");
        
        test = new RepeatedTest(test, ITERATIONS);
        test = new TimedTest(test, TIMEOUT);
        return new LoadTest(test, CONCURRENCY);
    }

    public static class PipelineImplTest extends TestCase {
        
        private Pipeline[] pipelines;
        private int next, recycle;
        {   
            Runtime runtime;
            long memory;
            runtime = Runtime.getRuntime();

            System.gc();
            memory = runtime.freeMemory();            

            new PipelineImpl
                (new SaxGenerator(),
                 new SaxSerializer(new SimpleXmlWriter()));
            
            memory -= runtime.freeMemory();
            System.out.println("Memory: " + 
                               memory +
                               " bytes");

            memory = runtime.freeMemory();    
            pipelines = new PipelineImpl[CONCURRENCY];
            for(int i = CONCURRENCY; i-- > 0; ) {
                pipelines[i] = new PipelineImpl
                    (new SaxGenerator(),
                     new SaxSerializer(new SimpleXmlWriter()));
            }
            memory -= runtime.freeMemory();
            System.out.println("Memory: " + 
                               (memory / CONCURRENCY) +
                               " bytes/PipelineImpl");
            System.gc();
            next = recycle = -1;
        }

        public PipelineImplTest(String name) {

            super(name);
        }
        
        
        private static String createMessage() {
            
            return  ("<parent><child>" + 
                     Thread.currentThread() + 
                     "</child></parent>");
        }

        private Pipeline getPipeline() {

            return pipelines[(next = (++next % CONCURRENCY))];
        }

        private void recyclePipeline(Pipeline pipeline) {
            
            pipelines[(recycle = (++recycle % CONCURRENCY))] = pipeline;
        }
    
        public void testPipeline() throws Exception {
            ProcessingContext processingContext;
            StringReader reader;
            StringWriter writer;
            Pipeline pipeline;
            String message;

            message = createMessage();
            reader = new StringReader(message);
            writer = new StringWriter();
            processingContext = new ProcessingContext
                (new WebRequestContextImpl(reader),
                 new WebResponseContextImpl(writer));
            
            pipeline = getPipeline();
            pipeline.execute(processingContext);
            recyclePipeline(pipeline);

            reader.close();
            writer.close();
    
            assertEquals(message, writer.toString());            
        }
    }
    
    private static class SimpleXmlWriter extends XMLWriterImpl {
        private Writer writer;

        public void parse(InputSource source, OutputTarget target) 
            throws IOException, SAXException {

            writer = target.getCharacterStream();
            parse(source);
            writer.flush();
            writer = null;
        }

        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes atts)
            throws SAXException {
            try {
                writer.write("<");
                writer.write(qName);
                writer.write(">");
            }
            catch(IOException e) {}

        }

        public void characters(char[] ch,
                               int start,
                               int length)
            throws SAXException {
            try {

                writer.write(ch, start, length);
            }
            catch(IOException e) {}
        }

        public void endElement(String namespaceURI,
                               String localName,
                               String qName)
            throws SAXException {

            try {
                writer.write("</");
                writer.write(qName);
                writer.write(">");           
            }
            catch(IOException e) {}

        }
    }
}
