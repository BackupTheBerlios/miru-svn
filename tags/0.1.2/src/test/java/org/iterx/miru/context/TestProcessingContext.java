/*
  org.iterx.miru.context.TestProcessingContext

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

import junit.framework.TestCase;

import org.iterx.miru.context.ProcessingContext;
import org.iterx.miru.context.MockWebRequestContext;
import org.iterx.miru.context.MockWebResponseContext;

public class TestProcessingContext extends TestCase {
    
    private ProcessingContext processingContext;

    protected void setUp() {
	
	processingContext = new ProcessingContext(new MockWebRequestContext("/"),
						  new MockWebResponseContext());
    }

    protected void tearDown() {

	processingContext = null;
    }

    public void testConstructors() {
	ProcessingContext clone;

	processingContext = new ProcessingContext(new MockWebRequestContext("/"),
						  new MockWebResponseContext());
	assertNotNull(processingContext);

	clone = new ProcessingContext(processingContext);
	assertNotNull(clone);
	assertEquals(processingContext.getRequestContext(), 
		     clone.getRequestContext());
	assertEquals(processingContext.getResponseContext(), 
		     clone.getResponseContext());
	
	try {
	    new ProcessingContext(null, null);
	    fail("ProcessingContext initialised with null arguments");
	}
	catch(IllegalArgumentException e) {}
	try {
	    new ProcessingContext(null, new MockWebResponseContext());
	    fail("ProcessingContext initialised with null arguments");
	}
	catch(IllegalArgumentException e) {}

	try {
	    new ProcessingContext(new MockWebRequestContext("/"), null);
	    fail("ProcessingContext initialised with null arguments");
	}
	catch(IllegalArgumentException e) {}

	try {
	    new ProcessingContext(null);
	    fail("ProcessingContext initialised from null parent context");
	}
	catch(IllegalArgumentException e) {}
    }

    public void testRequestContextAccessors() {
	RequestContext request;

	request = new MockWebRequestContext("/");
	processingContext.setRequestContext(request);
	assertEquals(request, processingContext.getRequestContext());

	try {
	    processingContext.setRequestContext(null);
	    fail("RequestContext is null");
	}
	catch(IllegalArgumentException e) {}
    }

    public void testResponseContextAccessors() {
	ResponseContext response;

	response = new MockWebResponseContext();
	processingContext.setResponseContext(response);
	assertEquals(response, processingContext.getResponseContext());

	try {
	    processingContext.setResponseContext(null);
	    fail("ResponseContext is null");
	}
	catch(IllegalArgumentException e) {}
    }


    public void testAttributeAccessors() {
	Object object;

	processingContext.setAttribute("name", null); 
	assertNull(processingContext.getAttribute("name"));
	
	processingContext.setAttribute("name", (object = new Object())); 
	assertEquals(object, processingContext.getAttribute("name"));
	
	processingContext.setAttribute("name", null);
	assertNull(processingContext.getAttribute("name"));
	
	assertEquals(0, (processingContext.getAttributeNames()).length);
	
	processingContext.setAttribute("null", null); 
	processingContext.setAttribute("name", object); 
	assertEquals(1, (processingContext.getAttributeNames()).length);
	assertEquals("name", (processingContext.getAttributeNames())[0]);
    }

}
