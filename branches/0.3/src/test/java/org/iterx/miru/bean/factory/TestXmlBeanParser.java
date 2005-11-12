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
import java.util.ArrayList;
import java.util.LinkedHashMap;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.iterx.miru.io.resource.MockResource;
import org.iterx.miru.bean.BeanException;
import org.xml.sax.SAXException;

public class TestXmlBeanParser extends TestCase  {

    private static final String NS = "http://iterx.org/miru/1.0/beans";

    private static final Object[][] BEANS = {
        //singletons
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\" singleton=\"true\"/>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            null
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\" singleton=\"false\"/>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.FALSE,
            null
        },
        //propertyValue setters
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            "value"
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<string>value</string>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            "value"
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<object value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            "value"
        },
        //property editors
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<uri value=\"" + NS + "\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            URI.create(NS)
        },

        //bean references
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<object ref=\"b\"/>" +
            "</bean>" +
            "<bean id=\"b\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            new MyObject("value")
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<object ref=\"b\"/>" +
            "</bean>" +
            "<bean id=\"b\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"b\"/>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            new MyObject("b")
        },
        //anonymous beans
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<object>" +
            "<bean class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"anonymous\"/>" +
            "</bean>" +
            "</object>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            new MyObject("anonymous")
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<object>" +
            "<bean class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</object>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            new MyObject("value")
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<object>" +
            "<bean class=\"" + (MyObject.class).getName()  + "\">" +
            "<object>" +
            "<bean class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"value\"/>" +
            "</bean>" +
            "</object>" +
            "</bean>" +
            "</object>" +
            "</bean>" +
            "</beans>",
            "a",
            MyObject.class,
            Boolean.TRUE,
            new MyObject(new MyObject("value"))
        },
        //lists & maps
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"list\"/>" +
          "</bean>" +
          "<bean id=\"list\" class=\"" + (MyList.class).getName() + "\">" +
          "<addAll><list>" +
          "<entry value=\"a\"/>" +
          "<entry value=\"b\"/>" +
          "<entry value=\"c\"/>" +
          "</list></addAll>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyList(new String[] { "a", "b", "c" })
        },
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"list\"/>" +
          "</bean>" +
          "<bean id=\"list\" class=\"" + (MyList.class).getName() + "\">" +
          "<addAll><list>" +
          "<entry>a</entry>" +
          "<entry>b</entry>" +
          "<entry>c</entry>" +
          "</list></addAll>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyList(new String[] { "a", "b", "c" })
        },
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"list\"/>" +
          "</bean>" +
          "<bean id=\"list\" class=\"" + (MyList.class).getName() + "\">" +
          "<addAll><list>" +
          "<entry>a</entry>" +
          "<entry ref=\"b\"></entry>" +
          "<entry>" +
          "<bean class=\"" + (MyObject.class).getName()  + "\">" +
          "<string value=\"c\"/>" +
          "</bean>" +
          "</entry>" +
          "</list></addAll>" +
          "</bean>" +
          "<bean id=\"b\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<string value=\"b\"/>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyList(new Object[] { "a", new MyObject("b"), new MyObject("c") })
        },
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"list\"/>" +
          "</bean>" +
          "<bean id=\"list\" class=\"" + (MyList.class).getName() + "\">" +
          "<addAll><list>" +
          "<entry>" +
          "<bean class=\"" + (MyList.class).getName()  + "\">" +
          "<addAll><list>" +
          "<entry>a</entry>" +
          "</list></addAll>" +
          "</bean>" +
          "</entry>" +
          "</list></addAll>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyList(new Object[] { new MyList(new Object[] { "a" }) })
        },
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"map\"/>" +
          "</bean>" +
          "<bean id=\"map\" class=\"" + (MyMap.class).getName() + "\">" +
          "<putAll><map>" +
          "<entry key=\"1\" value=\"a\"/>" +
          "<entry key=\"2\" value=\"b\"/>" +
          "<entry key=\"3\" value=\"c\"/>" +
          "</map></putAll>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyMap(new String[] { "1", "a", "2", "b", "3", "c" })
        },
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"map\"/>" +
          "</bean>" +
          "<bean id=\"map\" class=\"" + (MyMap.class).getName() + "\">" +
          "<putAll><map>" +
          "<entry key=\"1\">a</entry>" +
          "<entry key=\"2\">b</entry>" +
          "<entry key=\"3\">c</entry>" +
          "</map></putAll>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyMap(new String[] { "1", "a", "2", "b", "3", "c" })
        },
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"map\"/>" +
          "</bean>" +
          "<bean id=\"map\" class=\"" + (MyMap.class).getName() + "\">" +
          "<putAll><map>" +
          "<entry key=\"1\">a</entry>" +
          "<entry key=\"2\" ref=\"b\"/>" +
          "<entry key=\"3\">" +
          "<bean class=\"" + (MyObject.class).getName()  + "\">" +
          "<string value=\"c\"/>" +
          "</bean>" +
          "</entry>" +
          "</map></putAll>" +
          "</bean>" +
          "<bean id=\"b\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<string value=\"b\"/>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyMap(new Object[] { "1", "a",
                                   "2", new MyObject("b"),
                                   "3", new MyObject("c")})
        },

        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<object ref=\"map\"/>" +
          "</bean>" +
          "<bean id=\"map\" class=\"" + (MyMap.class).getName() + "\">" +
          "<putAll><map>" +
          "<entry key=\"1\">" +
          "<bean class=\"" + (MyMap.class).getName() + "\">" +
          "<putAll><map>" +
          "<entry key=\"1\" value=\"a\"/>" +
          "</map></putAll>" +
          "</bean>" +
          "</entry>" +
          "</map></putAll>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyMap(new Object[] { "1", new MyMap(new Object[] { "1", "a" })  })
        },
        {
          "<beans xmlns=\"" + NS + "\">" +
          "<bean id=\"a\" class=\"" + (MyObject.class).getName()  + "\">" +
          "<array>" +
          "<list>" +
          "<entry value=\"a\"/>" +
          "<entry value=\"b\"/>" +
          "<entry value=\"c\"/>" +
          "</list>" +
          "</array>" +
          "</bean>" +
          "</beans>",
          "a",
          MyObject.class,
          Boolean.TRUE,
          new MyList(new String[] { "a", "b", "c" })
        },

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
            "<bean class=\"" + (MyObject.class).getName() + "\"/>" +
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
            "<bean id=\"bean\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<bogus value=\"value\"/>" +
            "</bean>" +
            "</beans>",
            BeanException.class
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"bean\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"value\">value</string>" +
            "</bean>" +
            "</beans>",
            IOException.class
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<my-bean id=\"bean\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<string value=\"value\">value</string>" +
            "</my-bean>" +
            "</beans>",
            IOException.class
        },
        {
            "<beans xmlns=\"" + NS + "\">" +
            "<bean id=\"bean\" class=\"" + (MyObject.class).getName()  + "\">" +
            "<bean class=\"" + (MyObject.class).getName()  + "\"/>" +
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
        assertEquals(data[4], ((MyObject) bean).getObject());

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
            assertEquals(data[1], e.getClass());
        }

    }

    public static class MyObject {

        private Object object;

        public MyObject() {}

        public MyObject(Object object) {

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

        public void setArray(Object[] array) {

            this.object = new MyList(array);
        }

        public int hashCode() {

            return (object != null)? object.hashCode() : 0;
        }

        public boolean equals(Object object) {

            return ((object instanceof MyObject) &&
                    (((MyObject) object).object).equals(this.object));

        }

    }

    public static class MyList extends ArrayList {

        public MyList() {}

        public MyList(Object[] objects) {

            for(int i = 0; i < objects.length; i++) {
                add(objects[i]);
            }
        }
    }

    public static class MyMap extends LinkedHashMap {

        public MyMap() {}

        public MyMap(Object[] objects) {

            for(int i = 0; i < objects.length; i += 2) {
                put(objects[i], objects[i + 1]);
            }
        }
    }

}
