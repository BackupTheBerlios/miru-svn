package org.iterx.miru.pipeline.transformer;

import java.net.URI;
import java.io.IOException;

import junit.framework.TestCase;
import org.iterx.miru.io.factory.ResourceFactory;
import org.iterx.miru.io.MockResourceFactory;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.pipeline.PipelineChainImpl;
import org.iterx.miru.pipeline.serializer.XmlSerializer;
import org.iterx.miru.pipeline.generator.XmlGenerator;
import org.iterx.miru.context.factory.ProcessingContextFactory;
import org.iterx.miru.context.http.MockHttpRequestContext;
import org.iterx.miru.context.http.MockHttpResponseContext;

public class TestXsltTransformer extends TestCase {

    private static final String TEMPLATE =
        ("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
         "<xsl:template match=\"node\">"+
         "<match><xsl:apply-templates/></match>" +
         "</xsl:template>"+
         "</xsl:stylesheet>");

    private static final String DOCUMENT =
        ("<node>text</node>");

    private static final String RESULT =
        ("<match>text</match>");



    private ProcessingContextFactory processingContextFactory;
    private ResourceFactory resourceFactory;

    protected void setUp() {
        MockResource resource;


        resource = new MockResource(URI.create("template.xsl"));
        resource.setData(TEMPLATE.getBytes());

        processingContextFactory = ProcessingContextFactory.getProcessingContextFactory();
        resourceFactory = new MockResourceFactory(resource);

    }

    protected void tearDown() {

        processingContextFactory = null;
        resourceFactory = null;
    }


    public void testTransformer() throws IOException {
        PipelineChainImpl pipelineChain;
        MockHttpResponseContext response;
        XsltTransformer transformer;
        XmlSerializer serializer;

        pipelineChain = new PipelineChainImpl(new XmlGenerator(),
                                              serializer = new XmlSerializer());
        serializer.setOmitXMLDeclaration(true);

        pipelineChain.addTransformer(transformer = new XsltTransformer());
        transformer.setUri("template.xsl");
        transformer.setResourceFactory(resourceFactory);

        pipelineChain.execute(processingContextFactory.getProcessingContext
            (MockHttpRequestContext.newInstance("/", DOCUMENT.getBytes()),
             response = MockHttpResponseContext.newInstance()));

        assertEquals(RESULT, new String(response.getData()));
    }

}
