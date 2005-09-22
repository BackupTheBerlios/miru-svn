/*
  org.iterx.miru.spring.io.resource.ClassPathResource

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
package org.iterx.miru.io.resource;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.iterx.miru.io.Resource;

public class ClassPathResource implements Resource {

    private static final String SCHEME = "classpath";

    protected static final Log logger = LogFactory.getLog(ClassPathResource.class);

    protected ClassLoader classLoader;
    private InputStream in;
    private Reader reader;

    private URI uri;

    public ClassPathResource(String uri) throws URISyntaxException {
	
        this(new URI(uri));
    }
    
    public ClassPathResource(String uri, ClassLoader classLoader) throws URISyntaxException {

        this(new URI(uri), null);
    }

    public ClassPathResource(URI uri) {
        
        this(uri, null);
    }
    
    public ClassPathResource(URI uri, ClassLoader classLoader) {
        String scheme;

        if(uri == null)
            throw new IllegalArgumentException("uri == null");
        if((scheme = uri.getScheme()) != null &&
           !(SCHEME.equals(scheme)))
            throw new IllegalArgumentException("Invalid scheme '" + scheme + "'.");

        this.classLoader = ((classLoader == null)?
                            (Thread.currentThread()).getContextClassLoader() :
                            classLoader);	
        this.uri = uri;
    }


    public URI getURI() {
	
        return uri;
    }

    public String getProperty(String key) {
	
        return null;
    }   

    public int getContentLength() {

        return -1;
    }

    public String getContentType() {

        
        return null;
    }

    public String getCharacterEncoding() {

        return null;
    }

    public InputStream getInputStream() throws IOException {

        if(in != null) return in;
        else if(reader != null) return null;

        return (in = classLoader.getResourceAsStream(uri.getPath()));
    }

    public Reader getReader() throws IOException {

        if(reader != null) return reader;
        else if(in != null) return null;

        return (reader =  new InputStreamReader
            (classLoader.getResourceAsStream(uri.getPath())));
    }


    public boolean exists() {

        return ((classLoader.getResourceAsStream(uri.getPath())) != null);
    }

    public void reset() {

        if(in != null) {
            try {
                in.close();
            }
            catch(Exception e){}
            in = null;
        }
        if(reader != null) {
            try {
                reader.close();
            }
            catch(Exception e){}
            reader = null;
        }
    }

    public int hashCode() {
	
	return uri.hashCode();
    }

    public boolean equals(Object object) {

	return ((this == object) ||
		((object instanceof ClassPathResource) &&
		 uri.equals(((ClassPathResource) object).uri) &&
                 classLoader.equals((((ClassPathResource) object).classLoader))));
                
    }

    public String toString() {
	StringBuffer buffer;
        String cls;

        buffer = new StringBuffer();
	cls = (getClass().getName());
        buffer.append(cls.substring(1 + cls.lastIndexOf('.')));
	buffer.append('[');
        buffer.append("uri=[");
	buffer.append(uri);
	buffer.append("]]");
	return buffer.toString();
    }

    protected void finalize() {

	reset();
    }
}
