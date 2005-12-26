package org.iterx.miru.support.ehcache.cache.factory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Collections;

import org.iterx.miru.cache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

public class EhCache<K extends Serializable, V extends Serializable>  implements Cache<K, V> {

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

    public V get(K key) {
        assert (cache != null) : "cache == null";

        try {
            Element element;

            element = cache.get(key);
            return (V) ((element != null)? element.getValue() : null);
        }
        catch(CacheException e){}
        return null;
    }

    public void put(K key, V object) {
        assert (cache != null) : "cache == null";

        cache.put(new Element(key, object));
    }

    public void remove(K key) {
        assert (cache != null) : "cache == null";

        cache.remove(key);
    }

    public Iterator<K> keys() {
        assert (cache != null) : "cache == null";

        try {
            return (cache.getKeys()).iterator();
        }
        catch(CacheException e) {}

        return (Collections.EMPTY_LIST).iterator();
    }
}