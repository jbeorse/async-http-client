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

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

public class ComplexClientTest extends AbstractBasicTest {

    @Test(groups = "standalone")
    public void multipleRequestsTest() throws Throwable {
        AsyncHttpClient c = new AsyncHttpClient();

        String body = "hello there";

        // once
        Response response = c.preparePost(getTargetUrl())
                .setBody(body)
                .execute().get(TIMEOUT, TimeUnit.SECONDS);

        assertEquals(response.getResponseBody(), body);

        // twice
        response = c.preparePost(getTargetUrl())
                .setBody(body)
                .execute().get(TIMEOUT, TimeUnit.SECONDS);

        assertEquals(response.getResponseBody(), body);
    }

    @Test(groups = "standalone")
    public void multipleMaxConnectionOpenTest() throws Throwable {
        AsyncHttpClientConfig cg = new AsyncHttpClientConfig.Builder().setKeepAlive(true)
                .setConnectionTimeoutInMs(5000).setMaximumConnectionsTotal(1).build();
        AsyncHttpClient c = new AsyncHttpClient(cg);

        String body = "hello there";

        // once
        Response response = c.preparePost(getTargetUrl())
                .setBody(body)
                .execute().get(TIMEOUT, TimeUnit.SECONDS);

        assertEquals(response.getResponseBody(), body);

        // twice
        Exception exception = null;
        try {
            c.preparePost(getTargetUrl()).setBody(body).execute().get(TIMEOUT, TimeUnit.SECONDS);
            fail("Should throw exception. Too many connections issued.");
        } catch (Exception ex) {
            ex.printStackTrace();
            exception = ex;
        }
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Too many connections");
    }

    @Test(groups = "standalone")
    public void multipleMaxConnectionOpenTestWithQuery() throws Throwable {
        AsyncHttpClientConfig cg = new AsyncHttpClientConfig.Builder().setKeepAlive(true)
                .setConnectionTimeoutInMs(5000).setMaximumConnectionsTotal(1).build();
        AsyncHttpClient c = new AsyncHttpClient(cg);

        String body = "hello there";

        // once
        Response response = c.preparePost(getTargetUrl() + "?foo=bar")
                .setBody(body)
                .execute().get(TIMEOUT, TimeUnit.SECONDS);

        assertEquals(response.getResponseBody(), "foo_" + body);

        // twice
        Exception exception = null;
        try {
            c.preparePost(getTargetUrl()).setBody(body).execute().get(TIMEOUT, TimeUnit.SECONDS);
            fail("Should throw exception. Too many connections issued.");
        } catch (Exception ex) {
            ex.printStackTrace();
            exception = ex;
        }
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "Too many connections");
    }

    @Test(groups = "standalone")
    public void urlWithoutSlashTest() throws Throwable {
        AsyncHttpClient c = new AsyncHttpClient();

        String body = "hello there";

        // once
        Response response = c.preparePost(String.format("http://127.0.0.1:%d/foo/test", port1))
                .setBody(body)
                .execute().get(TIMEOUT, TimeUnit.SECONDS);

        assertEquals(response.getResponseBody(),body);
    }

    @Test(groups = "standalone")
    public void urlWithColonTest() throws Throwable {
        AsyncHttpClient c = new AsyncHttpClient();

        String query = "q=test:colon:";
        Response response = c.prepareGet(String.format("http://127.0.0.1:%d/foo/test/colon?%s", port1, query))
                .execute().get(TIMEOUT, TimeUnit.SECONDS);

        assertEquals(response.getHeader("X-queryString"), query);
    }
}
