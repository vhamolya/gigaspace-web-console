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

import com.github.terma.gigaspacewebconsole.provider.driver.GigaSpaceUtils;
import org.openspaces.core.GigaSpace;

public class EmbeddedSpace {

    public static void main(String[] args) {
        System.out.println("Starting...");
        GigaSpace gigaSpace = GigaSpaceUtils.getGigaSpace("/./embedded?locators=localhost:4174");
        System.out.println("Filling...");
        DataPreload.fill(gigaSpace);
        System.out.println("Filled");
    }

}