/*
  org.iterx.miru.matcher.NotMatcher

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


import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;

public class NotMatcher<S extends RequestContext, T extends ResponseContext>  implements Matcher<S, T> {

    private Matcher<S, T> matcher;

    public Matcher<S, T> getMatcher() {

        return matcher;
    }

    public void setMatcher(Matcher<? extends S, ? extends T> matcher) {

        if(matcher == null)
            throw new IllegalArgumentException("matcher == null");
        this.matcher = (Matcher<S, T>) matcher;
    }

    public Matches getMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (matcher != null) : "matcher == null";

        return ((!matcher.hasMatches(processingContext))? new Matches() : null);
    }

    public boolean hasMatches(ProcessingContext<? extends S, ? extends T> processingContext) {
        assert (matcher != null) : "matcher == null";

        return (!matcher.hasMatches(processingContext));
    }

}
