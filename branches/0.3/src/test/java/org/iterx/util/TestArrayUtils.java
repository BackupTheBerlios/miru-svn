/*
  org.iterx.util.TestArrayUtils

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

public class TestArrayUtils extends TestCase {

    private Object[] arrayA;
    private String[] arrayB;
    private String a, b;

    {
        a = "a";
        b = "b";
        arrayA = new Object[] { null, a, b, null, b, a, null };
        arrayB = new String[] { null, a, b, null, b, a, null };
    }

    public void testIndexOf() {

        assertEquals(0, ArrayUtils.indexOf(arrayA, null));
        assertEquals(1, ArrayUtils.indexOf(arrayA, a));
        assertEquals(2, ArrayUtils.indexOf(arrayA, b));
        assertEquals(0, ArrayUtils.indexOf(arrayB, null));
        assertEquals(1, ArrayUtils.indexOf(arrayB, a));
        assertEquals(2, ArrayUtils.indexOf(arrayB, b));        
    }

    public void testLastIndexOf() {

        assertEquals(6, ArrayUtils.lastIndexOf(arrayA, null));
        assertEquals(5, ArrayUtils.lastIndexOf(arrayA, a));
        assertEquals(4, ArrayUtils.lastIndexOf(arrayA, b));
        assertEquals(6, ArrayUtils.lastIndexOf(arrayB, null));
        assertEquals(5, ArrayUtils.lastIndexOf(arrayB, a));
        assertEquals(4, ArrayUtils.lastIndexOf(arrayB, b));        
    }

    public void testContains() {

        assertTrue(ArrayUtils.contains(arrayA, null));
        assertTrue(ArrayUtils.contains(arrayA, a));
        assertTrue(ArrayUtils.contains(arrayA, b));
        assertFalse(ArrayUtils.contains(arrayA, "c"));

        assertTrue(ArrayUtils.contains(arrayB, null));
        assertTrue(ArrayUtils.contains(arrayB, a));
        assertTrue(ArrayUtils.contains(arrayB, b));
        assertFalse(ArrayUtils.contains(arrayB, "c"));
    }

    public void testResize() {
        String[] array;

        array = new String[]{ a };
        assertEquals(1, array.length);
        assertEquals(a, array[0]);

        array = (String[]) ArrayUtils.resize(array, 3);
        array[1] = b;
        assertEquals(3, array.length);
        assertEquals(a, array[0]);
        assertEquals(b, array[1]);
        assertNull(array[2]);

        array = (String[]) ArrayUtils.resize(array, 1);
        assertEquals(1, array.length);
        assertEquals(a, array[0]);

    }

    public void testAdd() {
        String[] array;

        array = new String[0];

        array = (String[]) ArrayUtils.add(array, a);
        assertEquals(a, array[0]);
        array = (String[]) ArrayUtils.add(array, b);
        assertEquals(b, array[1]);
        array = (String[]) ArrayUtils.add(array, null);
        assertEquals(null, array[2]);
    }

    public void testRemove() {
        String[] array;

        array = new String[] { null, b, a, b, a};

        array = (String[]) ArrayUtils.remove(array, "c");
        assertEquals(5, array.length);
        array = (String[]) ArrayUtils.remove(array, a);
        assertEquals(4, array.length);
        array = (String[]) ArrayUtils.remove(array, b);
        assertEquals(3, array.length);
        array = (String[]) ArrayUtils.remove(array, null);
        assertEquals(2, array.length);
        array = (String[]) ArrayUtils.remove(array, b);
        assertEquals(1, array.length);
        array = (String[]) ArrayUtils.remove(array, a);
        assertEquals(0, array.length);
    }

}
