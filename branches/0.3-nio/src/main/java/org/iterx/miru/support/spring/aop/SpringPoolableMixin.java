/*
  org.iterx.miru.support.spring.aop.SpringPoolableMixin

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

package org.iterx.miru.support.spring.aop;

import org.aopalliance.intercept.MethodInvocation;

import org.springframework.aop.support.DelegatingIntroductionInterceptor;
import org.springframework.aop.TargetSource;

import org.iterx.miru.aop.Poolable;

public class SpringPoolableMixin<T> extends DelegatingIntroductionInterceptor
    implements Poolable<T> {

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String method;
        Object[] arguments;

        method = (methodInvocation.getMethod()).getName();
        arguments = methodInvocation.getArguments();
        if("getInstance".equals(method) && arguments.length == 0) {

            return ((TargetSource) methodInvocation.getThis()).getTarget();
        }
        else if("recycleInstance".equals(method) && arguments.length == 1) {

            ((TargetSource) methodInvocation.getThis()).releaseTarget(arguments[0]);
            return null;
        }
        return super.invoke(methodInvocation);
    }

    
    public T getInstance() throws Exception {

        //NOTE: Stub - method interceptor supplies actual implementation.
        throw new UnsupportedOperationException();
    }


    public void recycleInstance(T object) throws Exception {

        //NOTE: Stub - method interceptor supplies actual implementation.
        throw new UnsupportedOperationException();
    }

}
