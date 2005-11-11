package org.iterx.util;

import java.util.Properties;

import junit.framework.TestCase;


public class TestSystemUtils extends TestCase {

    private static final String KEY = (TestSystemUtils.class).getName();
    private static final String VALUE = "VALUE";

    public void testProperty() {

        assertNull(SystemUtils.getProperty(KEY));

        SystemUtils.setProperty(KEY, VALUE);
        assertEquals(VALUE, SystemUtils.getProperty(KEY));

        SystemUtils.setProperty(KEY, null);
        assertNull(SystemUtils.getProperty(KEY));
    }


}
