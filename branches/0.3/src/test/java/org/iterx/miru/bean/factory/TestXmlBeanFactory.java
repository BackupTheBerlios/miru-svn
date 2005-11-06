/*
  org.iterx.miru.bean.factory.TestXmlBeanFactory

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
package org.iterx.miru.bean.factory;

import java.io.IOException;

import junit.framework.TestCase;
import org.iterx.miru.io.Resource;
import org.iterx.miru.io.resource.MockResource;

public class TestXmlBeanFactory extends TestCase {


    public void testConstructors() {

        assertNotNull(new XmlBeanFactory());
        assertNotNull(new XmlBeanFactory(new MockBeanFactory()));
    }

    public void testLoadExceptions() {
        XmlBeanFactory factory;

        factory = new XmlBeanFactory();

        try {
            factory.load((String) null);
            fail("uri == null");
        }
        catch(IllegalArgumentException e) {}
        catch(Exception e) {
            fail("Invalid exception thrown " + e.getClass() + ".");
        }

        try {
            factory.load((Resource) null);
            fail("resource == null");
        }
        catch(IllegalArgumentException e) {}
        catch(Exception e) {
            fail("Invalid exception thrown " + e.getClass() + ".");
        }

        try {
            factory.load("scheme://uri-does-not-exist");
            fail("Failed to detected invalid uri.");
        }
        catch(IllegalArgumentException e) {}
        catch(Exception e) {
            fail("Invalid exception thrown " + e.getClass() + ".");
        }
        try {
            MockResource resource;

            resource = new MockResource();
            resource.setData("".getBytes());
            factory.load(resource);
            fail("Failed to detect invalid stream.");
            }
        catch(IOException e) {}
        catch(Exception e) {
            fail("Invalid exception thrown " + e.getClass() + ".");
        }
    }

}
