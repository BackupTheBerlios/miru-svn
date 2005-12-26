/*
  org.iterx.miru.matcher.AndMatcher

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

import java.util.List;

import org.iterx.util.ArrayUtils;
import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;

public class AndMatcher<S extends RequestContext, T extends ResponseContext> implements Matcher<S, T> {

    private Matcher<S, T>[] matchers = (Matcher<S, T>[]) new Object[0];

    public Matcher<S, T>[] getMatchers() {

        return matchers;
    }

    public void setMatchers(List<Matcher<? extends S, ? extends T>> matchers) {

        if(matchers == null)
            throw new IllegalArgumentException("matchers == null");
        this.matchers = matchers.toArray(this.matchers);
    }

    public void addMatcher(Matcher<? extends S, ? extends T> matcher) {

        if(matcher == null)
            throw new IllegalArgumentException("matcher == null");
        matchers = (Matcher<S, T>[]) ArrayUtils.add(matchers, matcher);
    }

    public void removeMatcher(Matcher<? extends S, ? extends T> matcher) {

        if(matcher == null)
            throw new IllegalArgumentException("matcher == null");
        matchers = (Matcher<S, T>[]) ArrayUtils.remove(matchers, matcher);
    }


    public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (matchers != null) : "matchers == null";
        Matches matches;

        matches = new Matches();
        for(Matcher<S, T> matcher : matchers) {
            Matches value;

            if((value = matcher.getMatches(processingContext)) != null)
                matches.put(value);
            else return null;
        }
        return matches;
    }

    public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (matchers != null) : "matchers == null";

        for(Matcher<S, T> matcher : matchers) {
            if(!matcher.hasMatches(processingContext)) return false;
        }
        return true;
    }

}
