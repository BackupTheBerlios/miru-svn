package org.iterx.miru.dispatcher.handler;

import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.context.ProcessingContext;

public class HandlerChainWrapperImpl implements HandlerChainWrapper {

    HandlerChainProvider provider;
    HandlerChainImpl instance;

    public HandlerChainWrapperImpl(HandlerChainProvider provider) {

        if(provider == null)
            throw new IllegalArgumentException("provider == mull");
        this.provider = null;
    }


    public String getName() {

        return instance.getName();
    }

    public void setName(String name) {

        instance.setName(name);
    }

    public void addHandler(Object object) {
        HandlerAdapter[] adapters;
        final Object handler;

        handler = object;
        adapters = provider.getHandlerAdapters();
        for(int i = 0; i < adapters.length; i++) {
            final HandlerAdapter adapter;
            if((adapter = adapters[i]).supports(handler)) {
                instance.addHandler
                    (new Handler() {
                        public int execute(ProcessingContext processingContext) {
                            return adapter.execute(processingContext, handler);
                        }
                    });
                return;
            }
        }
        if(object instanceof Handler) {
            instance.addHandler(handler);
            return;
        }

        throw new IllegalArgumentException
            ("Unsupported handler ' " + object.getClass() + "'.");
    }

    public Object[] getHandlers() {

        return null;
    }

    public void setHandlers(Object[] handlers) {


    }

    public void removeHandler(Object handler) {


    }

    public Object getWrappedInstance() {

        return instance;
    }

    public void setWrappedInstance(Object object) {
        assert (object instanceof HandlerChainImpl) : "Invalid instance.";

        instance = (HandlerChainImpl) object;
    }

    /*
    private static class HandlerAdapterAdapter implements Handler {
        private HandlerAdapter adapter;
        private Object handler;

        private HandlerAdapterAdapter(HandlerAdapter adapter,
                                      Object handler) {

            this.adapter =adapter;
            this.handler = handler;
        }

        public int execute(ProcessingContext processingContext) {

            return adapter.execute(processingContext, handler);
        }
    }
    */

}
