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
import org.iterx.miru.pipeline.transformer.TransformerImpl;
import org.iterx.miru.pipeline.generator.GeneratorImpl;
import org.iterx.miru.pipeline.serializer.SerializerImpl;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;


public class TestPipelineChainImpl extends TestCase {

    public void testConstructors() {
        PipelineChainImpl<RequestContext, ResponseContext> pipelineChain;
        Generator generator;
        Transformer[] transformers;
        Serializer serializer;

        pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>();
        assertNotNull(pipelineChain);
        assertNull(pipelineChain.getGenerator());
        assertNull(pipelineChain.getTransformers());
        assertNull(pipelineChain.getSerializer());

        pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>((generator = new GeneratorImpl()),
                                                                               (serializer = new SerializerImpl()));
        assertNotNull(pipelineChain);
        assertEquals(generator, pipelineChain.getGenerator());
        assertNull(pipelineChain.getTransformers());
        assertEquals(serializer, pipelineChain.getSerializer());

        pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>((generator = new GeneratorImpl()),
                                                                               null,
                                                                               (serializer = new SerializerImpl()));
        assertNotNull(pipelineChain);
        assertEquals(generator, pipelineChain.getGenerator());
        assertNull(pipelineChain.getTransformers());
        assertEquals(serializer, pipelineChain.getSerializer());


        pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>
            ((generator = new GeneratorImpl()),
             (transformers = new Transformer[] { new TransformerImpl() }),
             (serializer = new SerializerImpl()));
        assertNotNull(pipelineChain);
        assertEquals(generator, pipelineChain.getGenerator());
        assertEquals(transformers, pipelineChain.getTransformers());
        assertEquals(serializer, pipelineChain.getSerializer());


        try {
            pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>(null,
                                                                                   new SerializerImpl());
            fail("Failed to detect null Generator");
        }
        catch(IllegalArgumentException e) {}

        try {
            pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>(new GeneratorImpl(),
                                                                                   null);
            fail("Failed to detect null Generator");
        }
        catch(IllegalArgumentException e) {}
    }

    public void testGeneratorAccessors() {
        PipelineChainImpl<RequestContext, ResponseContext> pipelineChain;
        Generator generator;

        pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>();
        assertNull(pipelineChain.getGenerator());

        pipelineChain.setGenerator((generator = new GeneratorImpl()));
        assertEquals(generator, pipelineChain.getGenerator());

        pipelineChain.setGenerator(null);
        assertNull(pipelineChain.getGenerator());
    }

    public void testSerializerAccessors() {
        PipelineChainImpl<RequestContext, ResponseContext> pipelineChain;
        Serializer serializer;

        pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>();
        assertNull(pipelineChain.getSerializer());

        pipelineChain.setSerializer((serializer = new SerializerImpl()));
        assertEquals(serializer, pipelineChain.getSerializer());

        pipelineChain.setSerializer(null);
        assertNull(pipelineChain.getSerializer());
    }

    public void testTransformerAccessors() {
        PipelineChainImpl<RequestContext, ResponseContext> pipelineChain;
        Transformer[] transformers;
        Transformer transformer;

        pipelineChain = new PipelineChainImpl<RequestContext, ResponseContext>();
        assertNull(pipelineChain.getTransformers());

        pipelineChain.addTransformer((transformer = new TransformerImpl()));
        assertEquals(1, (transformers = pipelineChain.getTransformers()).length);
        assertEquals(transformer, transformers[0]);

        pipelineChain.addTransformer((transformer = new TransformerImpl()));
        assertEquals(2, (transformers = pipelineChain.getTransformers()).length);
        assertEquals(transformer, transformers[1]);

        pipelineChain.removeTransformer(transformers[0]);
        assertEquals(1, (transformers = pipelineChain.getTransformers()).length);
        assertEquals(transformer, transformers[0]);

        pipelineChain.removeTransformer(transformer);
        assertNull(pipelineChain.getTransformers());
    }



}
