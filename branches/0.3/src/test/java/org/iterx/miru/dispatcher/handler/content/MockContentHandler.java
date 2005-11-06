package org.iterx.miru.dispatcher.handler.content;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.Dispatcher;


public class MockContentHandler implements ContentHandler {

    public int execute(ProcessingContext processingContext) {

        return Dispatcher.OK;
    }

}
