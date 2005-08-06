package org.iterx.miru.dispatcher.handler.flow;

import org.iterx.miru.dispatcher.handler.FlowHandler;
import org.iterx.miru.dispatcher.handler.Handler;
import org.iterx.miru.dispatcher.matcher.Matcher;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.context.ProcessingContext;

public class IfFlowHandler implements FlowHandler {

    private Matcher matcher;
    private Handler trueHandler;
    private Handler falseHandler;


    public IfFlowHandler() {}

    public IfFlowHandler(Matcher matcher) {

        this.matcher = matcher;
    }

    public Matcher getMatcher() {

        return matcher;
    }

    public void setMatcher(Matcher matcher) {

        this.matcher = matcher;
    }


    //rename to :
    // getMatchesHandler & getOtherwiseHandler();

    public Handler getOnTrueHandler() {

        return trueHandler;
    }


    public void setOnTrueHandler(Handler trueHandler) {

        this.trueHandler = trueHandler;
    }

    public Handler getOnFalseHandler() {

        return falseHandler;
    }


    public void setOnFalseHandler(Handler falseHandler) {

        this.falseHandler = falseHandler;
    }


    public int execute(ProcessingContext processingContext) {
        Handler handler;

        handler = ((matcher == null || matcher.hasMatches(processingContext))?
                   trueHandler : falseHandler);

        return ((handler != null)?
                handler.execute(processingContext) : Dispatcher.DECLINE);
    }


}
