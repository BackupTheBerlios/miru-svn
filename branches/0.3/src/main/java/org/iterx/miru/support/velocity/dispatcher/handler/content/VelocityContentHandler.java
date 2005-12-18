/*
  org.iterx.miru.support.velocity.dispatcher.handler.content.VelocityContentHandler

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
package org.iterx.miru.support.velocity.dispatcher.handler.content;

import java.net.URI;
import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.LogSystem;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.ResourceManager;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.ResourceCache;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import org.iterx.miru.dispatcher.handler.ContentHandler;
import org.iterx.miru.dispatcher.Dispatcher;
import org.iterx.miru.dispatcher.event.ErrorEvent;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.io.factory.ResourceFactory;
import org.iterx.miru.io.ReadableResource;
import org.iterx.miru.io.StreamTarget;
import org.iterx.miru.cache.Cache;
import org.iterx.miru.cache.factory.CacheFactory;
import org.iterx.util.URIUtils;

public class VelocityContentHandler implements ContentHandler {

    private static final Log LOGGER = LogFactory.getLog(VelocityContentHandler.class);

    private VelocityEngine engine;

    private ResourceFactory resourceFactory;
    private CacheFactory cacheFactory;
    private URI baseUri;

    private String uri = "{0}";

    public VelocityContentHandler() {

        try {
            engine = new VelocityEngine();
            engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
                               (CommonsLoggingWrapper.class).getName());
            engine.setProperty(VelocityEngine.RESOURCE_MANAGER_CLASS,
                               (DelegatingResourceManager.class).getName());
            engine.setProperty((ResourceLoader.class).getName(),
                               new ResourceLoaderWrapper(this));
            engine.setProperty((ResourceCache.class).getName(),
                               new ResourceCacheWrapper(this));
            engine.init();
        }
        catch(Exception e) {
            throw new RuntimeException("Failed to initialise Velocity.", e);
        }
    }

    public String getUri() {

        return uri;
    }

    public void setUri(String uri) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
        this.uri = uri;
    }

    public URI getBaseUri() {

        return baseUri;
    }

    public void setBaseUri(URI baseUri) {

        this.baseUri = baseUri;
    }

    public CacheFactory getCacheFactory() {

        return cacheFactory;
    }

    public void setCacheFactory(CacheFactory cacheFactory) {

        this.cacheFactory = cacheFactory;
    }

    public ResourceFactory getResourceFactory() {

          return resourceFactory;
      }

      public void setResourceFactory(ResourceFactory resourceFactory) {

          this.resourceFactory = resourceFactory;
      }

    public int execute(ProcessingContext processingContext) {
        RequestContext requestContext;
        ResponseContext responseContext;
        URI uri;

        uri = null;
        requestContext = processingContext.getRequestContext();
        responseContext = processingContext.getResponseContext();
        assert (responseContext instanceof StreamTarget) : "responseContext is not a StreamTarget";

        try {
            Template template;
            VelocityContext velocityContext;

            uri = URIUtils.resolve(this.uri,
                                   baseUri,
                                   new String[] { (requestContext.getURI()).getPath() });

            template = engine.getTemplate(uri.toString());

            velocityContext = new VelocityContext();
            velocityContext.put("ctx", processingContext);
            velocityContext.put("req", requestContext);
            velocityContext.put("res", responseContext);

            template.merge(velocityContext,
                           ((StreamTarget) responseContext).getWriter());
            return Dispatcher.OK;
        }
        catch(ResourceNotFoundException e) {
            LOGGER.error("Velocity template '" + uri + "' not found.", e);
            //TODO: Fix error status passing - need conversion based on on ctx.
            throw new ErrorEvent(404);
        }
        catch(Exception e) {
            LOGGER.error("Velocity transform failed for '" + uri + "'.", e);
        }

        return Dispatcher.ERROR;
    }


    private static class ResourceCacheWrapper implements ResourceCache {

        private VelocityContentHandler velocityContentHandler;
        private CacheFactory cacheFactory;
        private Cache cache;

        public ResourceCacheWrapper(VelocityContentHandler velocityContentHandler) {

            if(velocityContentHandler ==  null)
                throw new IllegalArgumentException("velocityContentHandler == null");
            this.velocityContentHandler = velocityContentHandler;
        }

        public void initialize(RuntimeServices runtimeServices) {}

        public Cache getCache() {

            if(cache == null &&
               (cacheFactory = velocityContentHandler.getCacheFactory()) != null) {
                synchronized(this) {
                    if(cache == null)
                        cache = cacheFactory.getCache((ResourceCacheWrapper.class).getName() + ":" + System.identityHashCode(this));
                }
            }
            return cache;
        }

        public Resource get(Object key) {
            Cache cache;

            return (((cache = getCache()) != null)?
                    (Resource) cache.get(key) : null);
        }

        public Resource put(Object key, Resource resource) {
            Cache cache;

            if((cache = getCache()) != null) cache.put(key, resource);
            return resource;
        }

        public Resource remove(Object key) {
            Resource resource;
            Cache cache;

            if((cache = getCache()) != null) {
                resource = (Resource) cache.get(key);
                cache.remove(key);
            }
            else resource = null;
            return resource;
        }

        public Iterator enumerateKeys() {
            Cache cache;

            return (((cache = getCache()) != null)?
                    cache.keys() : Collections.EMPTY_LIST.iterator());
        }

        protected void finalize() throws Throwable {

            if(cache != null)
                cacheFactory.recycleCache((ResourceCacheWrapper.class).getName() + ":" + System.identityHashCode(this));
            super.finalize();
        }

    }

    private static class ResourceLoaderWrapper extends ResourceLoader {

        private VelocityContentHandler velocityContentHandler;

        public ResourceLoaderWrapper(VelocityContentHandler velocityContentHandler) {

            if(velocityContentHandler ==  null)
                throw new IllegalArgumentException("velocityContentHandler == null");
            this.velocityContentHandler = velocityContentHandler;
        }

        public void init(ExtendedProperties extendedProperties) {}

        public long getLastModified(Resource resource) {

            return 0;
        }

        public boolean isSourceModified(Resource resource) {

            return false;
        }

        public InputStream getResourceStream(String name) throws ResourceNotFoundException {
            try {
                ResourceFactory resourceFactory;

                if((resourceFactory = velocityContentHandler.getResourceFactory()) != null) {
                    ReadableResource resource;

                    resource = (ReadableResource) resourceFactory.getResource(URI.create(name));
                    return resource.getInputStream();
                }
            }
            catch(IOException e) {
                LOGGER.error("Resource failure.", e);
            }
            throw new ResourceNotFoundException("Failed to load resource '" + name + "'.");
        }
    }

    public static class DelegatingResourceManager implements ResourceManager {

        private RuntimeServices runtimeServices;
        private ResourceLoader resourceLoader;
        private ResourceCache resourceCache;


        public void initialize(RuntimeServices runtimeServices) throws Exception {
            ResourceLoader resourceLoader;
            ResourceCache resourceCache;

            resourceLoader = (ResourceLoader) runtimeServices.getProperty((ResourceLoader.class).getName());
            if(resourceLoader == null)
                throw new IllegalStateException("No ResourceLoader specified.");
            resourceLoader.commonInit(runtimeServices, runtimeServices.getConfiguration());
            resourceLoader.init(runtimeServices.getConfiguration());


            resourceCache = (ResourceCache) runtimeServices.getProperty((ResourceCache.class).getName());
            if(resourceCache == null)
                throw new IllegalStateException("No ResourceCache specified.");
            resourceCache.initialize(runtimeServices);

            this.runtimeServices = runtimeServices;
            this.resourceLoader = resourceLoader;
            this.resourceCache = resourceCache;
        }

        public String getLoaderNameForResource(String resourceName) {

            throw new UnsupportedOperationException();
        }

        public Resource getResource(String resourceName,
                                    int resourceType,
                                    String encoding)
            throws ResourceNotFoundException, ParseErrorException, Exception {
            Resource resource;

            if((resource = resourceCache.get(resourceName)) == null) {
                resource = org.apache.velocity.runtime.resource.ResourceFactory.getResource(resourceName,
                                                                                        resourceType);            resource.setRuntimeServices(runtimeServices);
                resource.setName(resourceName);
                resource.setEncoding(encoding);
                resource.setResourceLoader(resourceLoader);
                resource.setRuntimeServices(runtimeServices);
                resource.process();

                resourceCache.put(resourceName, resource);
            }
            return resource;
        }
    }

    public static class CommonsLoggingWrapper implements LogSystem {

        public void init(RuntimeServices runtimeServices) throws Exception {}

        public void logVelocityMessage(int level, String message) {

            switch(level) {
                case LogSystem.ERROR_ID:
                    LOGGER.error(message);
                    break;
                case LogSystem.WARN_ID:
                    LOGGER.warn(message);
                    break;
                case LogSystem.INFO_ID:
                    LOGGER.info(message);
                    break;
                default:
                    LOGGER.debug(message);
                    break;
            }
        }
    }
}
