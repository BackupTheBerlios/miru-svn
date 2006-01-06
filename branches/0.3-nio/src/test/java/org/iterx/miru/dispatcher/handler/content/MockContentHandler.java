package org.iterx.miru.dispatcher.handler.content;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.Status;


public class MockContentHandler<S extends RequestContext, T extends ResponseContext> implements ContentHandler<S, T> {

    public Status execute(ProcessingContext<? extends S, ? extends T> processingContext) {

        return Status.OK;
    }

}
