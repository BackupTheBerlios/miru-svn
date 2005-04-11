/*
  org.iterx.util.TestArrays

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


public class TestArrays extends TestCase {

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

        assertEquals(0, Arrays.indexOf(arrayA, null));
        assertEquals(1, Arrays.indexOf(arrayA, a));
        assertEquals(2, Arrays.indexOf(arrayA, b));
        assertEquals(0, Arrays.indexOf(arrayB, null));
        assertEquals(1, Arrays.indexOf(arrayB, a));
        assertEquals(2, Arrays.indexOf(arrayB, b));        
    }

    public void testLastIndexOf() {

        assertEquals(6, Arrays.lastIndexOf(arrayA, null));
        assertEquals(5, Arrays.lastIndexOf(arrayA, a));
        assertEquals(4, Arrays.lastIndexOf(arrayA, b));
        assertEquals(6, Arrays.lastIndexOf(arrayB, null));
        assertEquals(5, Arrays.lastIndexOf(arrayB, a));
        assertEquals(4, Arrays.lastIndexOf(arrayB, b));        
    }

    public void testContains() {

        assertTrue(Arrays.contains(arrayA, null));
        assertTrue(Arrays.contains(arrayA, a));
        assertTrue(Arrays.contains(arrayA, b));
        assertFalse(Arrays.contains(arrayA, "c"));

        assertTrue(Arrays.contains(arrayB, null));
        assertTrue(Arrays.contains(arrayB, a));
        assertTrue(Arrays.contains(arrayB, b));
        assertFalse(Arrays.contains(arrayB, "c"));
    }

    public void testAdd() {
        String[] array;

        array = new String[0];

        array = (String[]) Arrays.add(array, a);
        assertEquals(a, array[0]);
        array = (String[]) Arrays.add(array, b);
        assertEquals(b, array[1]);
        array = (String[]) Arrays.add(array, null);
        assertEquals(null, array[2]);
    }

    public void testRemove() {
        String[] array;

        array = new String[] { null, b, a, b, a};

        array = (String[]) Arrays.remove(array, "c");
        assertEquals(5, array.length);
        array = (String[]) Arrays.remove(array, a);
        assertEquals(4, array.length);
        array = (String[]) Arrays.remove(array, b);
        assertEquals(3, array.length);
        array = (String[]) Arrays.remove(array, null);
        assertEquals(2, array.length);
        array = (String[]) Arrays.remove(array, b);
        assertEquals(1, array.length);
        array = (String[]) Arrays.remove(array, a);
        assertEquals(0, array.length);
    }

}
