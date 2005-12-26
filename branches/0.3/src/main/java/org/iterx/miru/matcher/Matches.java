/*
  org.iterx.miru.matcher.Matches

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

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.io.Serializable;

public class Matches implements Serializable {

    private Map<String, String[]> matches = new HashMap<String, String[]>(1);

    public String[] get(String key) {

        return matches.get(key);
    }

    public void put(Matches matches) {

        (this.matches).putAll(matches.matches);
    }

    public void put(String key, String[] values) {

        matches.put(key, values);
    }


    public void remove(String key)  {

        matches.remove(key);
    }

    public boolean equals(Object object) {

        if(object == this) return true;
        else if(object instanceof Matches) {
            Map<String, String[]> a, b;

            a = matches;
            b = ((Matches) object).matches;
            if(a.size() == b.size()) {
                for(Map.Entry<String, String[]> entry : a.entrySet()) {
                    if(!Arrays.equals(entry.getValue(),
                                      b.get(entry.getKey()))) return false;
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int value;

        value = 0;
        for(Map.Entry<String, String[]> entry : matches.entrySet()) {
            value += (entry.getKey()).hashCode();
            value += Arrays.hashCode(entry.getValue());
        }
        return value;
    }

}
