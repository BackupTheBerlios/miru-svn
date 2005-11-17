package org.iterx.miru.pipeline.transformer;

import java.net.URI;

import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;
import org.iterx.miru.io.ResourceFactory;
import org.iterx.miru.io.MockResourceFactory;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.pipeline.PipelineChain;
import org.iterx.miru.pipeline.PipelineChainImpl;
import org.iterx.miru.pipeline.Transformer;
import org.iterx.miru.pipeline.serializer.SaxSerializer;
import org.iterx.miru.pipeline.generator.SaxGenerator;
import org.iterx.miru.context.ProcessingContextFactory;
import org.iterx.sax.helper.XMLWriterImpl;

public class TestXsltTransformer extends TestCase {

    private ProcessingContextFactory processingContextFactory;
    private ResourceFactory resourceFactory;

    protected void setUp() {
        MockResource resource;


        resource = new MockResource(URI.create("template.xsl"));
        //load in data

        processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
        resourceFactory = new MockResourceFactory(resource);

    }

    protected void tearDown() {

        processingContextFactory = null;
        resourceFactory = null;
    }


    public void testTransformer() {
        PipelineChain pipelineChain;
        XsltTransformer transformer;

        pipelineChain = new PipelineChainImpl(new SaxGenerator(),
                                              new Transformer[] {
                                                  transformer = new XsltTransformer()
                                              },
                                              new SaxSerializer(new XMLWriterImpl()));

        transformer.setUri("template.xsl");
        transformer.setResourceFactory(resourceFactory);



    }

}
