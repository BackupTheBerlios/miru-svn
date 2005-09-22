/*
  org.iterx.miru.dispatcher.matcher.OrMatcher

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
package org.iterx.miru.dispatcher.matcher;

import org.iterx.util.ArrayUtils;
import org.iterx.miru.context.ProcessingContext;

public class OrMatcher implements Matcher {

    private Matcher[] matchers;

    public Matcher[] getMatchers() {

        return matchers;
    }

    public void setMatchers(Matcher[] matchers) {

        if(matchers == null)
            throw new IllegalArgumentException("matchers == null");
        this.matchers = matchers;
    }

    public Matcher addMatcher(Matcher matcher) {

        if(matcher == null)
            throw new IllegalArgumentException("matcher == null");
        matchers = (Matcher[]) ArrayUtils.add(matchers, matcher);

        return matcher;
    }

    public void removeMatcher(Matcher matcher) {

        if(matcher == null)
            throw new IllegalArgumentException("matcher == null");
        matchers = (Matcher[]) ArrayUtils.remove(matchers, matcher);
    }


    public Object[] getMatches(ProcessingContext context) {
        assert (matchers != null) : "matchers == null";
        Object[] objects;

        objects = null;
        for(int i = 0; i < matchers.length; i++) {
            if((objects = matchers[i].getMatches(context)) != null) break;
        }
        return objects;
    }

    public boolean hasMatches(ProcessingContext context) {
        assert (matchers != null) : "matchers == null";

        for(int i = 0; i < matchers.length; i++) {
            if(matchers[i].hasMatches(context)) return true;
        }
        return false;
    }

}
