/*
  org.iterx.miru.pipeline.helper.SaxHelper

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
package org.iterx.miru.pipeline.helper;

import java.util.ListIterator;
import java.util.Iterator;
import java.io.InputStream;
import java.io.Reader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;

import org.iterx.sax.InputSource;
import org.iterx.sax.OutputTarget;
import org.iterx.miru.context.RequestContext;
import org.iterx.miru.context.ResponseContext;
import org.iterx.miru.io.StreamSource;
import org.iterx.miru.io.StreamTarget;

public class SaxHelper {

    public static InputSource newInputSource(final RequestContext request)  {
        final StreamSource stream;
        final Iterable iterable;

        stream = (request instanceof StreamSource)? (StreamSource) request : null;
        iterable = (request instanceof Iterable)? (Iterable) request : null;

        return new InputSource() {

            public Iterator iterator() {
                Iterator iterator;

                return ((iterable != null &&
                         ((iterator = iterable.iterator()) instanceof ListIterator))? (ListIterator) iterator : null);
            }

            public InputStream getByteStream() {

                try {
                    return (stream != null)? stream.getInputStream() : null;
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public Reader getCharacterStream() {

                try {
                    return (stream != null)? stream.getReader() : null;
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String getEncoding() {

                return (stream != null)? stream.getCharacterEncoding() : null;
            }

            public String getPublicId() {

                return null;
            }

            public String getSystemId() {
                URI uri;

                return ((uri = request.getURI()) != null)? uri.toString() : null;
            }

            public void setByteStream(InputStream byteStream) {

                throw new UnsupportedOperationException();
            }

            public void setCharacterStream(Reader characterStream) {

                throw new UnsupportedOperationException();
            }

            public void setEncoding(String encoding) {

                throw new UnsupportedOperationException();
            }

            public void setPublicId(String publicId) {

                throw new UnsupportedOperationException();
            }

            public void setSystemId(String systemId) {

                throw new UnsupportedOperationException();
            }
        };
    }

    public static OutputTarget newOutputTarget(final ResponseContext response)  {
        final StreamTarget stream;
        final Iterable iterable;

        stream = (response instanceof StreamTarget)? (StreamTarget) response : null;
        iterable = (response instanceof Iterable)? (Iterable) response : null;

        return new OutputTarget() {

            public Iterator iterator() {
                Iterator iterator;

                return ((iterable != null &&
                         ((iterator = iterable.iterator()) instanceof ListIterator))? (ListIterator) iterator : null);
            }


            public OutputStream getByteStream() {

                try {
                    return (stream != null)? stream.getOutputStream() : null;
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public Writer getCharacterStream() {

                try {
                    return (stream != null)? stream.getWriter() : null;
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String getEncoding() {

                return (stream != null)? stream.getCharacterEncoding() : null;
            }

            public void setEncoding(String encoding) {

                if(stream != null) stream.setCharacterEncoding(encoding);

            }

            public String getPublicId() {

                return null;
            }

            public String getSystemId() {

                return null;
            }

            public void setByteStream(OutputStream byteStream) {

                throw new UnsupportedOperationException();
            }

            public void setCharacterStream(Writer characterStream) {

                throw new UnsupportedOperationException();
            }

            public void setPublicId(String publicId) {

                throw new UnsupportedOperationException();
            }

            public void setSystemId(String systemId) {

                throw new UnsupportedOperationException();
            }
        };
    }



}
