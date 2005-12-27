/*
  org.iterx.miru.util.MiruUtils

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


package org.iterx.miru.util;

import java.net.URI;

import org.iterx.miru.matcher.Matches;

public final class MiruUtils {

    private MiruUtils() {}

    public static URI resolve(String uri, URI base, Matches matches) {

        if(uri == null)
            throw new IllegalArgumentException("uri == null");

        if(matches != null) {
            StringBuilder builder;
            char[] buffer;

            builder = new StringBuilder();
            buffer = uri.toCharArray();
            for(int i = 0; i < buffer.length; i++) {
                char c;

                OUTER: switch(c = buffer[i]) {
                    case '$':
                        if((c = buffer[++i]) == '{') {
                            String key;

                            key = null;

                            for(int j = i; j < buffer.length; j++) {
                                c = buffer[j];

                                if(key == null && c == '[') {
                                    key = new String(buffer, i + 1, j - 2);
                                    i = j;
                                }
                                else if(c == '}') {
                                    if(key == null) {
                                        builder.append
                                            ((matches.get(key))[0]);
                                        i = j;
                                        break OUTER;
                                    }
                                    break;
                                }
                                else if(c == ']') {
                                    if(j < buffer.length -1 &&
                                       buffer[++j] == '}' &&
                                       key != null) {
                                        int index;

                                        index = Integer.parseInt
                                            (new String(buffer, i + 1, j - i - 2));

                                        builder.append
                                            ((matches.get(key))[index]);
                                        i = j;
                                        break OUTER;
                                    }
                                    break;
                                }
                            }
                        }
                        throw new IllegalArgumentException
                            ("Invalid uri format '" + uri + "'");
                    default:
                        builder.append(c);
                        break;
                }
            }
            uri = builder.toString();
        }
        return (base != null)? base.resolve(uri) : URI.create(uri);
    }


}
