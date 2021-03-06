/*
 * Copyright (c) 2014, Christoph Engelbert (aka noctarius) and
 * contributors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.noctarius.snowcast.impl;

import com.noctarius.snowcast.SnowcastEpoch;
import com.noctarius.snowcast.SnowcastMaxLogicalNodeIdOutOfBoundsException;
import org.junit.Test;

public class NodeSequencerServiceTestCase {

    @Test(expected = SnowcastMaxLogicalNodeIdOutOfBoundsException.class)
    public void test_logical_node_id_illegal_low_bound()
            throws Exception {

        NodeSequencerService sequencerService = new NodeSequencerService();
        sequencerService.createSequencer("test", SnowcastEpoch.byTimestamp(1), 1, (short) 1);
    }

    @Test(expected = SnowcastMaxLogicalNodeIdOutOfBoundsException.class)
    public void test_logical_node_id_illegal_high_bound()
            throws Exception {

        NodeSequencerService sequencerService = new NodeSequencerService();
        sequencerService.createSequencer("test", SnowcastEpoch.byTimestamp(1), 8193, (short) 1);
    }
}
