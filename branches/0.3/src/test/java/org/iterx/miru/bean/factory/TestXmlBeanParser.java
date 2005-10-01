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
import org.iterx.miru.io.resource.MockResource;

public class TestXmlBeanParser extends TestCase  {

    private static final String NS = "http://iterx.org/miru/1.0";

    private static final Object[][] TESTS = {
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
            "<bean id=\"a\" class=\"" + (ExtendedMockObject.class).getName()  + "\">" +
            "<object ref=\"b\"/>" +
            "</bean>" +
            "<bean id=\"b\" class=\"" + (MockObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            ExtendedMockObject.class,
            Boolean.TRUE,
            new ExtendedMockObject("value")
        },
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



        //tests for map/lists
        //tests for anonymous

    };

    private XmlBeanFactory beanFactory;
    private XmlBeanParser beanParser;


    protected void setUp() {

         beanFactory = new XmlBeanFactory();
         beanParser = new XmlBeanParser(beanFactory);
     }


    protected void tearDown() {

        beanFactory = null;
        beanParser = null;
    }


    public void testParse() throws IOException {
        MockResource resource;


        resource = new MockResource();

        for(int i = 0; i < TESTS.length; i++) {
            Object bean;
            String id;

            id = (String) TESTS[i][1];
            resource.setData(((String) TESTS[i][0]).getBytes());
            beanParser.parse(resource);

            assertNotNull(bean = beanFactory.getBean(id));
            assertEquals(TESTS[i][2], bean.getClass());
            if(beanFactory.isSingleton(id)) {
                assertEquals(Boolean.TRUE, TESTS[i][3]);
                assertTrue(bean == beanFactory.getBean(id));
            }
            else {
                assertEquals(Boolean.FALSE, TESTS[i][3]);
                assertFalse(bean == beanFactory.getBean(id));
            }

            assertEquals(TESTS[i][4], ((MockObject) bean).getObject());
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

    public static class ExtendedMockObject extends MockObject {

        public ExtendedMockObject() {}

        public ExtendedMockObject(Object object) {

            super(object);
        }

    }

}
