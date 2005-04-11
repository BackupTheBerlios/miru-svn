/*
  org.iterx.miru.context.WebResponseContext

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

package org.iterx.miru.context;

import org.iterx.miru.io.StreamTarget;
import org.iterx.miru.context.ResponseContext;

public interface WebResponseContext extends ResponseContext, StreamTarget {

    public static final int OK                    = 200;
    
    public static final int REDIRECT              = 302;

    public static final int BAD_REQUEST           = 400;
    public static final int UNAUTHORISED          = 401;
    public static final int FORBIDDEN             = 403;
    public static final int NOT_FOUND             = 404;

    public static final int SERVER_ERROR          = 500;
    public static final int NOT_IMPLEMENTED       = 501;
    public static final int SERVICE_UNAVAILABLE   = 503;

    public int getStatus();

    public void setStatus(int status);

    public String getProperty(String name);

    public void setProperty(String name, String value);


}
