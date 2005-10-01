/*
  org.iterx.util.TestKeyValue

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

import junit.framework.TestCase;

public class TestKeyValue extends TestCase {

    public void testConstructors() {
        KeyValue keyValue;
        Object value;

        assertNotNull(keyValue = new KeyValue("name", value = new Object()));
        assertEquals("name", keyValue.getKey());
        assertEquals(value, keyValue.getValue());

        assertNotNull(keyValue = new KeyValue("name", null));
        assertEquals("name", keyValue.getKey());
        assertNull(keyValue.getValue());

        try {
            new KeyValue(null, new Object());
            fail("name == value");
        }
        catch(IllegalArgumentException e) {}
    }

    public void testHashCode() {
        KeyValue keyValue;

        assertTrue((keyValue = new KeyValue("name", "value")).hashCode() ==
                     keyValue.hashCode());
        assertTrue((new KeyValue("name", "value")).hashCode() ==
                   (new KeyValue("name", "value")).hashCode());
        
        assertFalse((new KeyValue("a", "value")).hashCode() ==
                    (new KeyValue("b", "value")).hashCode());
        assertFalse((new KeyValue("name", "a")).hashCode() ==
                    (new KeyValue("name", "b")).hashCode());
        assertFalse((new KeyValue("name", null)).hashCode() ==
                    (new KeyValue("name", "value")).hashCode());
    }


    public void testEquals() {
        KeyValue keyValue;

        assertTrue((keyValue = new KeyValue("name", "value")).equals(keyValue));
        assertTrue((new KeyValue("name", "value")).equals(new KeyValue("name", "value")));
        assertTrue((new KeyValue("name", null)).equals(new KeyValue("name", null)));

        assertFalse((new KeyValue("a", "value")).equals(new KeyValue("b", "value")));
        assertFalse((new KeyValue("name", "a")).equals(new KeyValue("name", "b")));
        assertFalse((new KeyValue("name", "value")).equals(new KeyValue("name", null)));
        assertFalse((new KeyValue("name", null)).equals(new KeyValue("name", "value")));
    }

}
