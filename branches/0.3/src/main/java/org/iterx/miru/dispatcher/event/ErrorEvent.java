/*
  org.iterx.miru.dispatcher.event.ErrorEvent

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

package org.iterx.miru.dispatcher.event;

public class ErrorEvent extends Event {

    private int status;

    public ErrorEvent(int status) {

        this.status = status;
    }

    public ErrorEvent(int status, Throwable throwable) {

         initCause(throwable);
         this.status = status;
     }
 
    public int getStatus() {

        return status;
    }

    public void setStatus(int status) {

        this.status = status;
    }

}
