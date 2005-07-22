/*
  org.iterx.miru.support.aspectj.aop.AopAllianceAdaptor

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
package org.iterx.miru.support.aspectj.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.iterx.miru.support.aspectj.aop.MethodInvocationImpl;

public abstract aspect AopAllianceAdaptor {

    protected MethodInterceptor getMethodInterceptor() {

        return null;
    }

    protected ConstructorInterceptor getConstructorInterceptor() {

        return null;
    }

   protected abstract pointcut targetJoinPoint();

   pointcut methodExecution() : execution (* *(..));

   pointcut constructorExecution() : execution (new (..));

   Object around() : targetJoinPoint() && methodExecution() {
      MethodInterceptor interceptor;

      if((interceptor = getMethodInterceptor()) != null) {
          MethodInvocationImpl invocation;

          invocation = new MethodInvocationImpl(thisJoinPoint) {
              public Object implProceed() throws Throwable {
                return proceed();
              }
          };

          try {
              interceptor.invoke(invocation);
          }
          catch(Throwable t) {
              throw new RuntimeException(t);
          }
       }

       return proceed();
   }

   Object around() : targetJoinPoint()  && constructorExecution() {
      ConstructorInterceptor interceptor;

      if((interceptor = getConstructorInterceptor()) != null) {
          ConstructorInvocationImpl invocation;

          invocation = new ConstructorInvocationImpl(thisJoinPoint) {
              public Object implProceed() throws Throwable {

                return proceed();
              }
          };
          try {
              interceptor.construct(invocation);
          }
          catch(Throwable t) {
              throw new RuntimeException(t);
          }
       }
       return proceed();
   }

}