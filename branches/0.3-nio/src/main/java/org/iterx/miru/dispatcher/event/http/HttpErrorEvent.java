/*
  org.iterx.miru.dispatcher.event.HttpErrorEvent

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
package org.iterx.miru.dispatcher.event.http;

import org.iterx.miru.dispatcher.event.ErrorEvent;

public class HttpErrorEvent extends ErrorEvent {

    public static final int NOT_FOUND             = 404;

    public static final int INTERNAL_SERVER_ERROR = 500;


    public HttpErrorEvent(int status) {

        super(status);
    }

    public HttpErrorEvent(int status, Throwable throwable) {

        super(status, throwable);
    }

}
