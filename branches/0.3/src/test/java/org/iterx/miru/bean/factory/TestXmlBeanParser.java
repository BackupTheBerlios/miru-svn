/*
  org.iterx.miru.bean.factory.TestXmlBeanParser

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
import java.net.URL;
import java.net.URI;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.bean.BeanException;

public class TestXmlBeanParser extends TestCase  {

    private static final String NS = "http://iterx.org/miru/1.0";

    private static final Object[][] BEANS = {
        //singletons
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\" singleton=\"true\"/>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            null
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\" singleton=\"false\"/>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.FALSE,
            null
        },
        //propertyValue setters
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            "value"
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<string>value</string>" +
            "</bean>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            "value"
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<object value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            "value"
        },
        //property editors
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<uri value=\"" + NS + "\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            URI.create(NS)
        },

        //bean references
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<object ref=\"b\"/>" +
            "</bean>" +
            "<bean id=\"b\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            new MockObject("value")
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<object ref=\"b\"/>" +
            "</bean>" +
            "<bean id=\"b\" class=\"" + (AnotherMockObject.class).getName()  + "\"/>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            new AnotherMockObject()
        },
        //anonymous beans
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<object>" +
            "<bean id=\"b\" class=\"" + (AnotherMockObject.class).getName()  + "\"/>" +
            "</object>" +
            "</bean>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            new AnotherMockObject()
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<object>" +
            "<bean id=\"b\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</object>" +
            "</bean>" +
            "</beans>",
            "a",
            MockObject.class,
            Boolean.TRUE,
            new MockObject("value")
        },

        //tests for map/lists


    };

    private static final Object[][] BAD_BEANS = {
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"bean\"/>" +
            "</beans>",
            IllegalArgumentException.class
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean class=\"" + (MockObject.class).getName() + "\"/>" +
            "</beans>",
            IllegalArgumentException.class
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"bean\" class=\"BogusClass\"/>" +
            "</beans>",
            RuntimeException.class
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"bean\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<bogus value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            BeanException.class
        },

    };

    private XmlBeanFactory beanFactory;
    private XmlBeanParser beanParser;
    private Object[] data;

    public static Test suite() {
        TestSuite suite;

        suite = new TestSuite();

        for(int i = 0; i < BEANS.length; i++) {
            suite.addTest(new TestXmlBeanParser("testParse", BEANS[i]));
        }
        for(int i = 0; i < BAD_BEANS.length; i++) {
            suite.addTest(new TestXmlBeanParser("testFailure", BAD_BEANS[i]));
        }
        return suite;
    }

    private TestXmlBeanParser(String name, Object[] data) {

        super(name);
        this.data = data;
    }

    protected void setUp() {

         beanFactory = new XmlBeanFactory();
         beanParser = new XmlBeanParser(beanFactory);
     }


    protected void tearDown() {

        beanFactory = null;
        beanParser = null;
        data = null;
    }


    public void testParse() throws IOException {
        MockResource resource;
        Object bean;
        String id;

        resource = new MockResource();

        id = (String) data[1];
        resource.setData(((String) data[0]).getBytes());
        beanParser.parse(resource);

        assertNotNull(bean = beanFactory.getBean(id));
        assertEquals(data[2], bean.getClass());
        if(beanFactory.isSingleton(id)) {
            assertEquals(Boolean.TRUE, data[3]);
            assertTrue(bean == beanFactory.getBean(id));
        }
        else {
            assertEquals(Boolean.FALSE, data[3]);
            assertFalse(bean == beanFactory.getBean(id));
        }

        assertEquals(data[4], ((MockObject) bean).getObject());

     }

    public void testFailure() throws IOException {
        MockResource resource;

        resource = new MockResource();
        try {
            resource.setData(((String) data[0]).getBytes());
            beanParser.parse(resource);
            beanFactory.getBean("bean");
            fail("Failed to detect invalid Bean.");
        }
        catch(Exception e) {
            e.printStackTrace();
            assertEquals(data[1], e.getClass());

        }

    }

    public static class MockObject {

        private Object object;

        public MockObject() {}

        public MockObject(Object object) {

                   this.object = object;
               }

        public Object getObject() {

            return object;
        }

        public void setObject(Object object) {

            this.object = object;
        }

        public void setURL(URL url) {

            this.object = url;
        }

        public void setURI(URI uri) {

            this.object = uri;
        }

        public void setString(String string) {

            this.object = string;
        }

        public int hashCode() {

            return (object != null)? object.hashCode() : 0;
        }

        public boolean equals(Object object) {

            return ((object instanceof MockObject) &&
                    (((MockObject) object).getObject()).equals(this.object));

        }
    }

    public static class AnotherMockObject {

        public AnotherMockObject() {}

        public int hashCode() {

            return 1;
        }

        public boolean equals(Object object) {

            return (object instanceof AnotherMockObject);
        }

    }

}
