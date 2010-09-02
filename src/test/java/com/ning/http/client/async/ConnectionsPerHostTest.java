/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.ning.http.client.async;

import com.ning.http.client.AsyncHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.HttpResponseHeaders;
import com.ning.http.client.HttpResponseStatus;
import com.ning.http.client.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.testng.annotations.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Future;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Tests case where connections per host is exceeded
 *
 * @author Jeff Beorse
 */
public class ConnectionsPerHostTest extends AbstractBasicTest {

    @Test(groups = "standalone")
    public void testValidNumConnections() throws IOException {
		AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setKeepAlive(true).setMaximumConnectionsPerHost(2).build();
        AsyncHttpClient ahc = new AsyncHttpClient(cf);
		
		try{
			Future<Response> f1 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f2 = ahc.prepareGet(getTargetUrl()).execute();
			while(!f1.isDone() && !f2.isDone());
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be no log warnings here
		
		try{
			Future<Response> f3 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f4 = ahc.prepareGet(getTargetUrl()).execute();
			while(!f3.isDone() && !f4.isDone());
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be no log warnings here

    }
	
	@Test(groups = "standalone")
    public void testTooManyConnections() throws IOException {
		AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setKeepAlive(true).setMaximumConnectionsPerHost(2).build();
        AsyncHttpClient ahc = new AsyncHttpClient(cf);
		
		try{
			Future<Response> f1 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f2 = ahc.prepareGet(getTargetUrl()).execute();
			while(!f1.isDone() && !f2.isDone());
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be no log warnings here
		
		try{
			Future<Response> f3 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f4 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f5 = ahc.prepareGet(getTargetUrl()).execute();
			while(!f3.isDone() && !f4.isDone() && !f5.isDone() );
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be log warnings here
		
    }
	
	@Test(groups = "standalone")
    public void testValidNumConnectionsMultiHost() throws IOException {
		AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setKeepAlive(true).setMaximumConnectionsPerHost(2).build();
        AsyncHttpClient ahc = new AsyncHttpClient(cf);
		
		try{
			Future<Response> f1 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f2 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f3 = ahc.prepareGet("http://www.google.com/").execute();
			while(!f1.isDone() && !f2.isDone() && !f3.isDone());
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be no log warnings here
		
		try{
			Future<Response> f4 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f5 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f6 = ahc.prepareGet("http://www.google.com/").execute();
			while(!f4.isDone() && !f5.isDone() && !f6.isDone());
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be no log warnings here
		
    }
	
	@Test(groups = "standalone")
    public void testTooManyConnectionsMultiHost() throws IOException {
		AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setKeepAlive(true).setMaximumConnectionsPerHost(2).build();
        AsyncHttpClient ahc = new AsyncHttpClient(cf);
		
		try{
			Future<Response> f1 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f2 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f3 = ahc.prepareGet("http://www.google.com/").execute();
			while(!f1.isDone() && !f2.isDone() && !f3.isDone());
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be no log warnings here
		
		try{
			Future<Response> f4 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f5 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f6 = ahc.prepareGet(getTargetUrl()).execute();
			Future<Response> f7 = ahc.prepareGet("http://www.google.com/").execute();
			while(!f4.isDone() && !f5.isDone() && !f6.isDone() && !f7.isDone());
		} catch(Exception e){
			fail("Exception occured while opening connections to host", e);
		}
		
		//Should be log warnings here
		
    }
}