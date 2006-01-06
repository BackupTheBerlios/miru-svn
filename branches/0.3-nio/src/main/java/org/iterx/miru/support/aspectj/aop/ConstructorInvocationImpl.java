/*
  org.iterx.miru.support.aspectj.aop.ConstructorInvocationImpl

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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

import org.aopalliance.intercept.ConstructorInvocation;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.JoinPoint;

public class ConstructorInvocationImpl extends AbstractInvocationJoinpoint
    implements ConstructorInvocation {

    public ConstructorInvocationImpl(JoinPoint joinPoint) {
        super(joinPoint);
    }

    public AccessibleObject getStaticPart() {
        try {
            CodeSignature signature;
            Class cls;

            signature = (CodeSignature) joinPoint.getSignature();
            cls =  signature.getDeclaringType();

            return cls.getConstructor(signature.getParameterTypes());
        }
        catch(NoSuchMethodException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public Constructor getConstructor() {

        return (Constructor) getStaticPart();
    }
    
}
