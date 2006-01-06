/*
  org.iterx.miru.bean.editor.URLEditor

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
package org.iterx.miru.bean.editor;

import java.beans.PropertyEditor;
import java.net.URL;

import junit.framework.TestCase;

public class TestURLEditor extends TestCase {

    private PropertyEditor editor;

    protected void setUp() {

        editor = new URLEditor();
    }

    protected void tearDown() {

        editor = null;
    }

    public void testValueAccessors() throws Exception {
        URL url;

        url = new URL("file:///");
        assertNull(editor.getValue());

        editor.setValue(url);
        assertTrue(editor.getValue() == url);
    }

    public void testAsTextAccessors() throws Exception {
        Object value;
        URL url;

        url = new URL("file:///");
        assertNull(editor.getAsText());
        editor.setAsText("file:///");

        assertTrue((value = editor.getValue()) instanceof URL);
        assertEquals(url, value);

        try {
            editor.setAsText("/");
            fail("Failed to detect invalid URL.");
        }
        catch(IllegalArgumentException e){}
    }


}
