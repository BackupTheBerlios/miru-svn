/*
  org.iterx.miru.matcher.UriMatcher

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

package org.iterx.miru.matcher;

import java.net.URI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.bean.BeanAware;

public class UriMatcher<S extends RequestContext, T extends ResponseContext>
    implements org.iterx.miru.matcher.Matcher<S, T>, BeanAware {

    public static final int MASK_SCHEME    = 0x04;
    public static final int MASK_AUTHORITY = 0x08;
    public static final int MASK_PATH      = 0x01;
    public static final int MASK_QUERY     = 0x10;
    public static final int MASK_URI       = 0x02;

    private static final String DEFAULT_PATTERN = ".*";

    private Pattern regex;
    private String pattern;

    private String id;
    private int mask;

    public UriMatcher() {

        this(null);
    }

    public UriMatcher(String id) {

        this(id, MASK_PATH);
    }

    public UriMatcher(int mask) {

        this(null, mask);
    }

    public UriMatcher(String id, int mask) {

        this.id = id;
        this.mask = mask;
        setPattern(DEFAULT_PATTERN);
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public int getMask() {

        return mask;
    }

    public void setMask(int mask) {

        this.mask = mask;
    }

    public String getPattern() {

        return pattern;
    }

    public void setPattern(String pattern) {
        if(pattern == null)
            throw new IllegalArgumentException("pattern == null");

        if(!pattern.equals(this.pattern)) {
            StringBuilder builder;

            builder = new StringBuilder();
            if(!pattern.startsWith("$")) builder.append(".*");
            builder.append(pattern);
            if(!pattern.endsWith("^")) builder.append(".*");

            regex = Pattern.compile(builder.toString());
            this.pattern = pattern;
        }
    }

    public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (id != null) : "id == null";
        assert (regex != null) : "regex == null";

        RequestContext request;
        URI uri;

        request = processingContext.getRequestContext();
        if((uri = request.getURI()) != null) {
            Matcher matcher;

            matcher = regex.matcher(uriToString(uri, mask));
            return matcher.matches();
        }
        return false;
    }


    public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (id != null) : "id == null";
        assert (regex != null) : "regex == null";

        RequestContext request;
        URI uri;

        request = processingContext.getRequestContext();
        if((uri = request.getURI()) != null) {
            Matcher matcher;

            matcher = regex.matcher(uriToString(uri, mask));
            if(matcher.matches()) {
                String[] results;
                int count;
                Matches matches;

                count = matcher.groupCount();
                results = new String[count + 1];
                for(int i = 0; i <= count; i++) {
                    results[i] = matcher.group(i);
                }
                matches = new Matches();
                matches.put(id, results);
                return matches;
            }
        }
        return null;
    }

    private static String uriToString(URI uri, int mask) {
        String string;

        switch(mask) {
            case MASK_PATH:
                string = uri.getPath();
                break;
            case MASK_URI:
                string = uri.toString();
                break;
            case MASK_SCHEME:
                string = uri.getScheme();
                break;
            case MASK_AUTHORITY:
                string = uri.getAuthority();
                break;
            case MASK_QUERY:
                string = uri.getQuery();
                break;
            default:
                StringBuilder builder = new StringBuilder();
                if((mask & MASK_SCHEME) != 0) {
                    builder.append(uri.getScheme());
                    builder.append(':');
                }
                if((mask & MASK_AUTHORITY) != 0) {
                    builder.append("//");
                    builder.append(uri.getAuthority());
                }
                if((mask & MASK_PATH) != 0)
                    builder.append(uri.getPath());
                if((mask & MASK_QUERY) != 0) {
                    builder.append('?');
                    builder.append(uri.getQuery());
                }
                string = builder.toString();
        }
        return string;
    }


}
