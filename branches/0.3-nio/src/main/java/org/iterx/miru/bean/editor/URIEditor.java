/*
  org.iterx.miru.bean.editor.URIEditor

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

import java.beans.PropertyEditorSupport;
import java.net.URI;

public class URIEditor extends PropertyEditorSupport {

    public void setAsText(String text) throws IllegalArgumentException {

        setValue(URI.create(text));
    }

    public String getAsText() {
        Object value;

        return (String) (((value = getValue()) == null)? value : value.toString());
    }

}
