/*
  org.iterx.util.KeyValue

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

import java.io.Serializable;

public class KeyValue implements Serializable {

    private Object key;
    private Object value;

    public KeyValue(Object key, Object value) {

        if(key == null)
            throw new IllegalArgumentException("key == null");

        this.key = key;
        this.value = value;
    }

    public Object getKey() {

        return key;
    }

    public Object getValue() {

        return value;
    }

    public int hashCode() {

        return (((key != null)? key.hashCode() : 0) +
                ((value != null)? value.hashCode() : 0));
    }

    public boolean equals(Object object) {
        KeyValue keyValue;

        return ((this == object) ||
                ((object instanceof KeyValue) &&
                 ((keyValue = (KeyValue) object).key).equals(key) &&
                 ((keyValue.value == null)?
                  (keyValue.value == value) :
                  (keyValue.value).equals(value))));
    }

    public String toString() {
        StringBuffer buffer;

        buffer = new StringBuffer();
        buffer.append('{');
        buffer.append(key);
        buffer.append(',');        
        buffer.append(value);
        buffer.append('}');
        return buffer.toString();
    }

}
