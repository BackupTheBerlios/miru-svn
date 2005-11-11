/*
  org.iterx.util.IteratorChainImpl

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

public class IteratorChainImpl implements IteratorChain {

    private Iterator parent;

    public IteratorChainImpl() {}

    public IteratorChainImpl(Iterator parent) {

        this.parent = parent;
    }

    public Iterator getParent() {

        return parent;
    }

    public void setParent(Iterator parent) {

        this.parent = parent;
    }

    public boolean hasNext() {

        return (parent != null)? parent.hasNext() : false;
    }

    public Object next() {

        if(parent == null)
            throw new NoSuchElementException();
        return parent.next();
    }

    public void remove() {

        if(parent == null)
            throw new UnsupportedOperationException();
        parent.remove();
    }

}
