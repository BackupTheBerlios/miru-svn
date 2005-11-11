/*
  org.iterx.util.FilterIteratorChain

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
package org.iterx.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilterIteratorChain extends IteratorChainImpl {

    private Filter filter;
    private Object next;

    public FilterIteratorChain(Iterator parent,
                               Filter filter) {
        super(parent);
        this.filter = filter;
    }

    public boolean hasNext() {
        Iterator parent;

        parent = getParent();
        if(next == null && parent.hasNext()) {
            do {
                Object object;

                object = parent.next();
                if(filter.equals(object)) {
                    next = object;
                    break;
                }
            }
            while(parent.hasNext());
        }
        return (next != null);
    }

    public Object next() {

        if(next != null || hasNext()) {
            Object next;

            next = this.next;
            this.next = null;
            return next;
        }
        throw new NoSuchElementException();
    }
}
