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
import java.io.Serializable;

public class Matches implements Serializable {

    private Map matches = new HashMap(1);

    public String[] get(String key) {

        return (String[]) matches.get(key);
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

}
