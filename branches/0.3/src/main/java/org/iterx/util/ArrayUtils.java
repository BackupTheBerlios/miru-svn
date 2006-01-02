/*
  org.iterx.util.ArrayUtils

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

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

public final class ArrayUtils {

    private ArrayUtils() {}

    public static Object[] resize(Object[] array, int size) {
        Object[] clone;
        int length;

        clone = (Object[])
            Array.newInstance((array.getClass()).getComponentType(),
                              size);

        length = (size > array.length)? array.length : size;
        System.arraycopy(array, 0, clone, 0, length);
        return clone;
    }


    public static Object[] add(Object[] array, Object object) {
        Object[] clone;
        int length;

        clone = (Object[])
            Array.newInstance((array.getClass()).getComponentType(),
                              1 + (length = array.length));

        System.arraycopy(array, 0, clone, 0, length);
        clone[length] = object;
        return clone;
    }

    public static boolean contains(Object[] array, Object object) {

        for(int i = array.length; i-- > 0;) {
            Object value;

            if((value = array[i]) == object ||
               (value != null && value.equals(object)))
                return true;
        }
        return false;
    }

    public static int indexOf(Object[] array, Object object) {

        for(int i = 0; i < array.length; i++) {
            Object value;

            if((value = array[i]) == object ||
               (value != null && value.equals(object)))
                return i;
        }
        return -1;
    }

    public static int lastIndexOf(Object[] array, Object object) {

        for(int i = array.length; i-- > 0;) {
            Object value;

            if((value = array[i]) == object ||
               (value != null && value.equals(object)))
                return i;
        }
        return -1;
    }

    public static Object[] remove(Object[] array, Object object) {

        for(int i = array.length; i-- > 0;) {
            Object value;

            if((value = array[i]) == object ||
               (value != null && value.equals(object))) {
                Object[] clone;
                int length;

                clone = (Object[])
                    Array.newInstance((array.getClass()).getComponentType(),
                                      (length = array.length) - 1);

                if(i > 0)
                    System.arraycopy(array, 0, clone, 0, i);
                if(i < length - 1)
                    System.arraycopy(array, i + 1, clone, i, length - i - 1);
                return clone;
            }
        }
        return array;
    }
}
