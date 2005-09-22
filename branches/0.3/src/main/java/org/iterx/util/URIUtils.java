package org.iterx.util;

import java.net.URI;

public class URIUtils {

    private URIUtils() {}

    public static URI resolve(String uri, URI base) {

        return (base != null)? base.resolve(uri) : URI.create(uri);
    }

    public static URI resolve(String uri, URI base, String[] values) {

        if(values != null) {
            StringBuilder builder;
            char[] buffer;

            builder = new StringBuilder();
            buffer = uri.toCharArray();
            for(int i = 0; i < buffer.length; i++) {
                char c;

                OUTER: switch(c = buffer[i]) {
                    case '{':
                        for(int j = i; j < buffer.length; j++) {
                            if(buffer[j] == '}') {
                                builder.append
                                    (values[Integer.parseInt
                                        (new String(buffer, i + 1, j - 1))]);
                                i = j;
                                break OUTER;
                            }
                        }
                        throw new IllegalArgumentException
                            ("Invalid uri format '" + uri + "'.");
                    default:
                        builder.append(c);
                        break;
                }
            }
            uri = builder.toString();
        }

        return resolve(uri, base);
    }

}
