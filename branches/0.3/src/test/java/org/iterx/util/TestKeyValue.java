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
        KeyValue<String, Object> keyValue;
        Object value;

        assertNotNull(keyValue = new KeyValue<String, Object>("name", value = new Object()));
        assertEquals("name", keyValue.getKey());
        assertEquals(value, keyValue.getValue());

        assertNotNull(keyValue = new KeyValue<String, Object>("name", null));
        assertEquals("name", keyValue.getKey());
        assertNull(keyValue.getValue());

        try {
            new KeyValue<String, Object>(null, new Object());
            fail("name == value");
        }
        catch(IllegalArgumentException e) {}
    }

    public void testHashCode() {
        KeyValue<String, String> keyValue;

        assertTrue((keyValue = new KeyValue<String, String>("name", "value")).hashCode() ==
                     keyValue.hashCode());
        assertTrue((new KeyValue<String, String>("name", "value")).hashCode() ==
                   (new KeyValue<String, String>("name", "value")).hashCode());
        
        assertFalse((new KeyValue<String, String>("a", "value")).hashCode() ==
                    (new KeyValue<String, String>("b", "value")).hashCode());
        assertFalse((new KeyValue<String, String>("name", "a")).hashCode() ==
                    (new KeyValue<String, String>("name", "b")).hashCode());
        assertFalse((new KeyValue<String, String>("name", null)).hashCode() ==
                    (new KeyValue<String, String>("name", "value")).hashCode());
    }


    public void testEquals() {
        KeyValue<String, String> keyValue;

        assertTrue((keyValue = new KeyValue<String, String>("name", "value")).equals(keyValue));
        assertTrue((new KeyValue<String, String>("name", "value")).equals(new KeyValue<String, String>("name", "value")));
        assertTrue((new KeyValue<String, String>("name", null)).equals(new KeyValue<String, String>("name", null)));

        assertFalse((new KeyValue<String, String>("a", "value")).equals(new KeyValue<String, String>("b", "value")));
        assertFalse((new KeyValue<String, String>("name", "a")).equals(new KeyValue<String, String>("name", "b")));
        assertFalse((new KeyValue<String, String>("name", "value")).equals(new KeyValue<String, String>("name", null)));
        assertFalse((new KeyValue<String, String>("name", null)).equals(new KeyValue<String, String>("name", "value")));
    }

}
