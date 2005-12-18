package org.iterx.miru.support.ehcache.cache.factory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collections;

import org.iterx.miru.cache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

public class EhCache implements Cache {

    private net.sf.ehcache.Cache cache;

    public EhCache() {}

    public EhCache(net.sf.ehcache.Cache cache) {

        if(cache == null)
            throw new IllegalArgumentException("cache == null");
        this.cache = cache;
    }

    public net.sf.ehcache.Cache getCache() {

        return cache;
    }

    public void setCache(net.sf.ehcache.Cache cache) {

        if(cache == null)
            throw new IllegalArgumentException("cache == null");
        this.cache = cache;
    }

    public Object get(Object key) {
        assert (cache != null) : "cache == null";

        try {
            Element element;

            element = cache.get((Serializable) key);
            return (element != null)? element.getValue() : null;
        }
        catch(CacheException e){}
        return null;
    }

    public void put(Object key, Object object) {
        assert (cache != null) : "cache == null";

        cache.put(new Element((Serializable) key, (Serializable) object));
    }

    public void remove(Object key) {
        assert (cache != null) : "cache == null";

        cache.remove((Serializable) key);
    }

    public Iterator keys() {
        assert (cache != null) : "cache == null";

        try {
            return (cache.getKeys()).iterator();
        }
        catch(CacheException e) {}

        return (Collections.EMPTY_LIST).iterator();
    }
}