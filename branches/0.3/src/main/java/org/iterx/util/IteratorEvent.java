/*
  org.iterx.util.IteratorEvent

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

import java.util.EventObject;

public class IteratorEvent extends EventObject {

    public enum Type { ADD, HAS_NEXT, NEXT, HAS_PREVIOUS, PREVIOUS, REMOVE, CLOSE }

    private Type type;
    private Object data;


    public IteratorEvent(Object source, Type type) {

        this(source, type, null);
    }

    public IteratorEvent(Object source, Type type, Object data) {

        super(source);
        this.type = type;
        this.data = data;
    }

    public Object getSource() {

        return super.getSource();
    }

    public Type getType() {

        return type;
    }

    public Object getData() {

        return data;
    }

    public String toString() {
        StringBuilder builder;

        builder = new StringBuilder();
        builder.append((this.getClass()).getName());
        builder.append("[type=");
        builder.append(type);
        builder.append(",source=");
        builder.append(getSource());
        builder.append(",data=");
        builder.append(data);
        builder.append(']');

        return builder.toString();
    }

}
