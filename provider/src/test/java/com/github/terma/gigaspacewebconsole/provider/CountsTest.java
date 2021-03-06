/*
Copyright 2015-2017 Artem Stasiuk

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.github.terma.gigaspacewebconsole.provider;

import com.github.terma.gigaspacewebconsole.core.CountsRequest;
import com.github.terma.gigaspacewebconsole.core.CountsResponse;
import com.github.terma.gigaspacewebconsole.provider.driver.GigaSpaceUtils;
import junit.framework.Assert;
import org.junit.Test;
import org.openspaces.core.GigaSpace;

import java.util.HashMap;
import java.util.Map;

public class CountsTest extends TestWithGigaSpace {

    @Test
    public void shouldReturnEmptyResultIfNoTypesInSpace() {
        CountsRequest countsRequest = new CountsRequest();
        countsRequest.url = gigaSpaceUrl;
        countsRequest.unmanaged = true;
        final CountsResponse countsResponse = new Counts().counts(countsRequest);

        // then only Object default
        Assert.assertEquals(1, countsResponse.counts.size());
    }

    @Test
    public void shouldReturnCountsOfTypes() {
        final GigaSpace gigaSpace = GigaSpaceUtils.getGigaSpace(gigaSpaceUrl);
        GigaSpaceUtils.registerType(gigaSpace, "com.a");

        GigaSpaceUtils.writeDocument(gigaSpace, "com.a");
        GigaSpaceUtils.writeDocument(gigaSpace, "com.a");
        GigaSpaceUtils.writeDocument(gigaSpace, "com.a");

        CountsRequest countsRequest = new CountsRequest();
        countsRequest.url = gigaSpaceUrl;
        countsRequest.unmanaged = true;
        final CountsResponse countsResponse = new Counts().counts(countsRequest);

        // then
        Assert.assertEquals(2, countsResponse.counts.size());
        Map<String, Integer> counts = new HashMap<>();
        counts.put(countsResponse.counts.get(0).name, countsResponse.counts.get(0).count);
        counts.put(countsResponse.counts.get(1).name, countsResponse.counts.get(1).count);
        Assert.assertEquals(new HashMap<String, Integer>() {{
            put("com.a", 3);
            put("java.lang.Object", 0);
        }}, counts);
    }

    /**
     * Not useful test as free GigaSpace license provides only one partitions.
     * No way to test count partitions.
     */
    @Test
    public void shouldReturnCountsByTypesAndPartitions() {
        final GigaSpace gigaSpace = GigaSpaceUtils.getGigaSpace(gigaSpaceUrl);
        GigaSpaceUtils.registerType(gigaSpace, "com.a");

        GigaSpaceUtils.writeDocument(gigaSpace, "com.a");
        GigaSpaceUtils.writeDocument(gigaSpace, "com.a");
        GigaSpaceUtils.writeDocument(gigaSpace, "com.a");

        CountsRequest countsRequest = new CountsRequest();
        countsRequest.url = gigaSpaceUrl;
        countsRequest.byPartitions = true;
        countsRequest.unmanaged = true;
        final CountsResponse countsResponse = new Counts().counts(countsRequest);

        // then
        Assert.assertEquals(2, countsResponse.counts.size());
        Map<String, Integer> counts = new HashMap<>();
        counts.put(countsResponse.counts.get(0).name, countsResponse.counts.get(0).count);
        counts.put(countsResponse.counts.get(1).name, countsResponse.counts.get(1).count);
        Assert.assertEquals(new HashMap<String, Integer>() {{
            put(gigaSpace.getName() + " [1] com.a", 3);
            put(gigaSpace.getName() + " [1] java.lang.Object", 0);
        }}, counts);
    }

}
