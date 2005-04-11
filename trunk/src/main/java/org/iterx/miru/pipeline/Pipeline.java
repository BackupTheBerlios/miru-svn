/*
  org.iterx.miru.pipeline.Pipeline

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

import java.io.IOException;

import org.iterx.miru.context.ProcessingContext;

import org.iterx.miru.pipeline.Stage;
import org.iterx.miru.pipeline.Generator;
import org.iterx.miru.pipeline.Transformer;
import org.iterx.miru.pipeline.Serializer;

import org.iterx.util.Arrays;

public class Pipeline {

    protected Generator generator;
    protected Transformer[] transformers;
    protected Serializer serializer;

    protected boolean _mutable;

    private Stage pipeline;

    {
        _mutable = false;
    }
    public Pipeline() {}

    public Pipeline(Generator generator,
                    Serializer serializer) {

        this(generator, null, serializer);
    }
    
    public Pipeline(Generator generator,
                    Transformer[] transformers,
                    Serializer serializer) {
        
        if(generator == null) 
            throw new IllegalArgumentException("generator == null");
        if(serializer == null) 
            throw new IllegalArgumentException("serializer == null");

        this.generator = generator;
        this.transformers = transformers;
        this.serializer = serializer;
    }

    public Generator getGenerator() {
        
        return generator;
    }


    public void setGenerator(Generator generator) {
        assert (_mutable) : "Immutable.";

        this.generator = generator;
    }
    
    public Transformer[] getTransformers() {

        return transformers;
    }

    public void addTransformer(Transformer transformer) {
        assert (_mutable) : "Immutable.";

        transformers = ((transformers == null)?
                        new Transformer[] { transformer } :
                        (Transformer[]) Arrays.add(transformers, transformer));
    }

    public void removeTransformer(Transformer transformer) {
        assert (_mutable) : "Immutable.";

        transformers = 
            (Transformer[]) Arrays.remove(transformers, transformer);
        if(transformers.length == 0) transformers = null;
    }


    public Serializer getSerializer() {
        
        return serializer;
    }

    public void setSerializer(Serializer serializer) {
        assert (_mutable) : "Immutable.";

        this.serializer = serializer;
    }

    public void init() {
        assert ((_mutable = false) == false);
        assert (generator != null) : "generator == null";
        assert (serializer != null) : "serializer == null";

        Stage pipeline;
        
        pipeline = generator;
        if(transformers != null) {
            for(int i = 0; i < transformers.length; i++) {
                Transformer transformer;
                
                transformer = transformers[i];
                transformer.setParent((XmlProducer) pipeline);
                pipeline = transformer;
            }
        }
        serializer.setParent((XmlProducer) pipeline);
        pipeline = serializer;
        this.pipeline = pipeline;
    }

    public void execute(ProcessingContext processingContext) throws IOException {
        if(pipeline == null) init();
        
        pipeline.init(processingContext);
        pipeline.execute();
        pipeline.reset();
    }

    public void destroy() {
        
        pipeline.destroy();

        pipeline = null;
        generator = null;
        transformers = null;
        serializer = null;
    }

}
