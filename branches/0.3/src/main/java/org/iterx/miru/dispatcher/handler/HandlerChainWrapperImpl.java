package org.iterx.miru.dispatcher.handler;

import org.iterx.miru.dispatcher.adapter.HandlerAdapter;
import org.iterx.miru.dispatcher.matcher.Matcher;
import org.iterx.miru.context.ProcessingContext;

public class HandlerChainWrapperImpl implements HandlerChainWrapper {

    HandlerChainProvider provider;
    HandlerChainImpl instance;

    public HandlerChainWrapperImpl(HandlerChainProvider provider) {

        if(provider == null)
            throw new IllegalArgumentException("provider == null");
        this.provider = null;
    }


    public String getId() {

        return instance.getId();
    }

    public void setId(String id) {

        instance.setId(id);
    }

    public Matcher getMatcher() {

        return instance.getMatcher();
    }

    public void setMatcher(Matcher matcher) {

        instance.setMatcher(matcher);
    }

    public void addHandler(Object object) {
        HandlerAdapter[] adapters;
        adapters = provider.getHandlerAdapters();
        for(int i = 0; i < adapters.length; i++) {
            final HandlerAdapter adapter;
            final Object handler;


            handler = object;
            if((adapter = adapters[i]).supports(handler)) {

                instance.addHandler
                    (new HandlerWrapper() {

                        public Object unwrap() {

                            return handler;
                        }

                        public int execute(ProcessingContext processingContext) {

                            return adapter.process(processingContext, handler);
                        }

                        public int hashCode() {

                            return handler.hashCode();
                        }

                        public boolean equals(Object object) {

                            return handler.equals(object);
                        }
                    });
                return;
            }
        }
        if(object instanceof Handler) {
            instance.addHandler(object);
            return;
        }

        throw new IllegalArgumentException
            ("Unsupported handler ' " + object.getClass() + "'.");
    }

    public Object[] getHandlers() {
        Object[] handlers;

        handlers = instance.getHandlers();
        for(int i = 0; i < handlers.length; i++) {
            Object handler;

            if(((handler = handlers[i]) instanceof HandlerWrapper))
                handlers[i] = ((HandlerWrapper) handler).unwrap();
        }
        return handlers;
    }

    public void setHandlers(Object[] objects) {

        for(int i = 0; i < objects.length; i++) {
            addHandler(objects[i]);
        }
    }

    public void removeHandler(Object object) {

        instance.removeHandler(object);
    }

    public Object getWrappedInstance() {

        return instance;
    }

    public void setWrappedInstance(Object object) {
        assert (object == null ||
                object instanceof HandlerChainImpl) : "Invalid instance.";

        instance = (HandlerChainImpl) object;
    }

    private interface HandlerWrapper extends Handler {

        public Object unwrap();

    }
}
