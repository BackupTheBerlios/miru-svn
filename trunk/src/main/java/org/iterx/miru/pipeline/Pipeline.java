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

import org.iterx.miru.pipeline.Generator;
import org.iterx.miru.pipeline.Transformer;
import org.iterx.miru.pipeline.Serializer;

public class Pipeline {

    public Pipeline() {}


    public Generator getGenerator() {
        
        return null;
    }


    public void setGenerator(Generator generator) {
        
    }
    
    public Transformer[] getTransformers() {

        return null;
    }

    public void addTransformer(Transformer transformer) {

    }

    public void removeTransformer(Transformer transformer) {

    }


    public Serializer getSerializer() {
        
        return null;
    }

    public void setSerializer(Serializer serializer) {
        

    }


    public void execute(ProcessingContext processingContext) throws IOException {
        


    }



}
