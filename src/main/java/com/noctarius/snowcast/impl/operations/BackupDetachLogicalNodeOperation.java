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
package com.noctarius.snowcast.impl.operations;

import com.hazelcast.nio.Address;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.spi.BackupOperation;
import com.noctarius.snowcast.SnowcastEpoch;
import com.noctarius.snowcast.impl.NodeSequencerService;
import com.noctarius.snowcast.impl.SequencerDataSerializerHook;
import com.noctarius.snowcast.impl.SequencerDefinition;
import com.noctarius.snowcast.impl.SequencerPartition;

import java.io.IOException;

public class BackupDetachLogicalNodeOperation
        extends AbstractSequencerOperation
        implements BackupOperation {

    private SequencerDefinition definition;
    private int logicalNodeId;
    private Address address;

    public BackupDetachLogicalNodeOperation() {
    }

    public BackupDetachLogicalNodeOperation(SequencerDefinition definition, int logicalNodeId, Address address) {
        super(definition.getSequencerName());
        this.definition = definition;
        this.logicalNodeId = logicalNodeId;
        this.address = address;
    }

    @Override
    public int getId() {
        return SequencerDataSerializerHook.TYPE_BACKUP_DETACH_LOGICAL_NODE;
    }

    @Override
    public void run()
            throws Exception {

        NodeSequencerService sequencerService = getService();
        SequencerPartition partition = sequencerService.getSequencerPartition(getPartitionId());
        partition.unassignLogicalNode(definition, logicalNodeId, address);
    }

    @Override
    protected void writeInternal(ObjectDataOutput out)
            throws IOException {

        super.writeInternal(out);
        out.writeInt(logicalNodeId);
        address.writeData(out);
        out.writeLong(definition.getEpoch().getEpochOffset());
        out.writeInt(definition.getMaxLogicalNodeCount());
        out.writeShort(definition.getBackupCount());
    }

    @Override
    protected void readInternal(ObjectDataInput in)
            throws IOException {

        super.readInternal(in);
        logicalNodeId = in.readInt();
        address = new Address();
        address.readData(in);

        long epochOffset = in.readLong();
        int maxLogicalNodeCount = in.readInt();
        short backupCount = in.readShort();

        SnowcastEpoch epoch = SnowcastEpoch.byTimestamp(epochOffset);
        definition = new SequencerDefinition(getSequencerName(), epoch, maxLogicalNodeCount, backupCount);
    }
}
