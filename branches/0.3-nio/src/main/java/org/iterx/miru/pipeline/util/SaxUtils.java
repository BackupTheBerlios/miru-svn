/*
  org.iterx.miru.pipeline.util.SaxUtils

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
package org.iterx.miru.pipeline.util;

import java.io.InputStream;
import java.io.Reader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.iterx.sax.InputSource;
import org.iterx.sax.OutputTarget;
import org.iterx.miru.io.stream.StreamSource;
import org.iterx.miru.io.stream.StreamTarget;

public class SaxUtils {

    public static InputSource newInputSource(final StreamSource streamSource)  {
        return new InputSource() {

            public InputStream getByteStream() {

                try {
                    return streamSource.getInputStream();
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public Reader getCharacterStream() {

                try {
                    return streamSource.getReader();
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String getEncoding() {

                return streamSource.getCharacterEncoding();
            }

            public String getPublicId() {

                return null;
            }

            public String getSystemId() {

                return null;
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

    public static OutputTarget newOutputTarget(final StreamTarget streamTarget)  {

        return new OutputTarget() {

            public OutputStream getByteStream() {

                try {
                    return streamTarget.getOutputStream();
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public Writer getCharacterStream() {

                try {
                    return streamTarget.getWriter();
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String getEncoding() {

                return streamTarget.getCharacterEncoding();
            }

            public void setEncoding(String encoding) {

                streamTarget.setCharacterEncoding(encoding);

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
