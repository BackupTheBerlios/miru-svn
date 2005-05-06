/*
  org.iterx.miru.pipeline.TestPipeline

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


import junit.framework.TestCase;


import org.iterx.miru.pipeline.Pipeline;
import org.iterx.miru.pipeline.Generator;
import org.iterx.miru.pipeline.Transformer;
import org.iterx.miru.pipeline.Serializer;
import org.iterx.miru.pipeline.GeneratorImpl;
import org.iterx.miru.pipeline.TransformerImpl;
import org.iterx.miru.pipeline.SerializerImpl;

public class TestPipeline extends TestCase {

    public void testConstructors() {
        Pipeline pipeline;
        Generator generator;
        Transformer[] transformers;
        Serializer serializer;

        pipeline = new Pipeline();
        assertNotNull(pipeline);
        assertNull(pipeline.getGenerator());
        assertNull(pipeline.getTransformers());
        assertNull(pipeline.getSerializer());

        pipeline = new Pipeline((generator = new GeneratorImpl()),
                                (serializer = new SerializerImpl()));
        assertNotNull(pipeline);
        assertEquals(generator, pipeline.getGenerator());
        assertNull(pipeline.getTransformers());
        assertEquals(serializer, pipeline.getSerializer());

        pipeline = new Pipeline((generator = new GeneratorImpl()),
                                null,
                                (serializer = new SerializerImpl()));
        assertNotNull(pipeline);
        assertEquals(generator, pipeline.getGenerator());
        assertNull(pipeline.getTransformers());
        assertEquals(serializer, pipeline.getSerializer());


        pipeline = new Pipeline
            ((generator = new GeneratorImpl()),
             (transformers = new Transformer[] { new TransformerImpl() }),
             (serializer = new SerializerImpl()));
        assertNotNull(pipeline);
        assertEquals(generator, pipeline.getGenerator());
        assertEquals(transformers, pipeline.getTransformers());
        assertEquals(serializer, pipeline.getSerializer());

        
        try {
            pipeline = new Pipeline(null,
                                    new SerializerImpl());
            fail("Failed to detect null Generator");
        }
        catch(IllegalArgumentException e) {}

        try {
            pipeline = new Pipeline(new GeneratorImpl(),
                                    null);
            fail("Failed to detect null Generator");
        }
        catch(IllegalArgumentException e) {}
    }

    public void testGeneratorAccessors() {
        Pipeline pipeline;
        Generator generator;

        pipeline = new Pipeline();
        assertNull(pipeline.getGenerator());

        pipeline.setGenerator((generator = new GeneratorImpl()));
        assertEquals(generator, pipeline.getGenerator());
        
        pipeline.setGenerator(null);
        assertNull(pipeline.getGenerator());
    }

    public void testSerializerAccessors() {
        Pipeline pipeline;
        Serializer serializer;

        pipeline = new Pipeline();
        assertNull(pipeline.getSerializer());

        pipeline.setSerializer((serializer = new SerializerImpl()));
        assertEquals(serializer, pipeline.getSerializer());
        
        pipeline.setSerializer(null);
        assertNull(pipeline.getSerializer());
    }

    public void testTransformerAccessors() {
        Pipeline pipeline;
        Transformer[] transformers;
        Transformer transformer;

        pipeline = new Pipeline();
        assertNull(pipeline.getTransformers());
        
        pipeline.addTransformer((transformer = new TransformerImpl()));
        assertEquals(1, (transformers = pipeline.getTransformers()).length);
        assertEquals(transformer, transformers[0]);
        
        pipeline.addTransformer((transformer = new TransformerImpl()));
        assertEquals(2, (transformers = pipeline.getTransformers()).length);
        assertEquals(transformer, transformers[1]);

        pipeline.removeTransformer(transformers[0]);
        assertEquals(1, (transformers = pipeline.getTransformers()).length);
        assertEquals(transformer, transformers[0]);
        
        pipeline.removeTransformer(transformer);
        assertNull(pipeline.getTransformers());
    }
    
    

}
