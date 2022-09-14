/*
 *  Copyright (c) 2020 Temporal Technologies, Inc. All Rights Reserved
 *
 *  Copyright 2012-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 *  Modifications copyright (C) 2017 Uber Technologies, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"). You may not
 *  use this file except in compliance with the License. A copy of the License is
 *  located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 *  or in the "license" file accompanying this file. This file is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package examples.activity;

import io.temporal.testing.TestActivityExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for {@link HelloActivity}. Doesn't use an external Temporal service.
 */
public class HelloActivityJUnit5Test {


    @RegisterExtension
    public static final TestActivityExtension testActivityExtension =
            TestActivityExtension.newBuilder()
                    .setActivityImplementations(new HelloActivity.GreetingActivitiesImpl())
                    .build();

    @Test
    public void testDynamicActivity(HelloActivity.GreetingActivities activity) {
        String result = activity.composeGreeting("Hello", "John");
        assertEquals("Hello John!", result);
    }


}
