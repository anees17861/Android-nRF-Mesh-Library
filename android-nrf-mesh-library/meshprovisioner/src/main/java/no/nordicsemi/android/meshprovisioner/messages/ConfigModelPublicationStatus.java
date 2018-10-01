/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.meshprovisioner.messages;

import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import no.nordicsemi.android.meshprovisioner.meshmessagestates.ProvisionedMeshNode;
import no.nordicsemi.android.meshprovisioner.messagetypes.AccessMessage;
import no.nordicsemi.android.meshprovisioner.opcodes.ConfigMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * To be used as a wrapper class for when creating the ConfigModelAppStatus Message.
 */
@SuppressWarnings("unused")
public class ConfigModelPublicationStatus extends ConfigStatusMessage {

    private static final String TAG = ConfigModelPublicationStatus.class.getSimpleName();
    private static final int OP_CODE = ConfigMessageOpCodes.CONFIG_MODEL_APP_STATUS;
    private static final int CONFIG_MODEL_PUBLICATION_STATUS_SIG_MODEL_PDU_LENGTH = 14;
    private static final int CONFIG_MODEL_APP_BIND_STATUS_VENDOR_MODEL_PDU_LENGTH = 16;

    private int mElementAddress;
    private byte[] publishAddress;
    private int mAppKeyIndex;
    private int credentialFlag;
    private int publishTtl;
    private int publishPeriod;
    private int publishRetransmitCount;
    private int publishRetransmitIntervalSteps;
    private int mModelIdentifier; //16-bit SIG Model or 32-bit Vendor Model identifier

    /**
     * Constructs the ConfigModelAppStatus mMessage.
     *
     * @param node    Node from which the mMessage originated from
     * @param message Access Message
     */
    public ConfigModelPublicationStatus(final ProvisionedMeshNode node, @NonNull final AccessMessage message) {
        super(node, message);
        this.mMessage = message;
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
    final void parseStatusParameters() {
        final AccessMessage message = mMessage;
        final ByteBuffer buffer = ByteBuffer.wrap(message.getParameters()).order(ByteOrder.LITTLE_ENDIAN);
        mStatusCode = mParameters[0];
        final byte[] elementAddress = new byte[]{mParameters[2], mParameters[1]};
        mElementAddress = ByteBuffer.wrap(elementAddress).order(ByteOrder.BIG_ENDIAN).getShort();
        publishAddress = new byte[]{mParameters[4], mParameters[3]};
        final byte[] appKeyIndex = new byte[]{(byte) (mParameters[6] & 0x0F), mParameters[5]};
        mAppKeyIndex = ByteBuffer.wrap(appKeyIndex).order(ByteOrder.BIG_ENDIAN).getShort();

        credentialFlag = (mParameters[8] & 0xF0) >> 4;
        publishTtl = mParameters[7];
        publishPeriod = mParameters[8];
        publishRetransmitCount = mParameters[9] >> 5;
        publishRetransmitIntervalSteps = mParameters[11] & 0x1F;

        final byte[] modelIdentifier;
        if (mParameters.length == CONFIG_MODEL_PUBLICATION_STATUS_SIG_MODEL_PDU_LENGTH) {
            modelIdentifier = new byte[]{mParameters[13], mParameters[12]};
            mModelIdentifier = ByteBuffer.wrap(modelIdentifier).order(ByteOrder.BIG_ENDIAN).getShort();
        } else {
            modelIdentifier = new byte[]{mParameters[13], mParameters[12], mParameters[15], mParameters[14]};
            mModelIdentifier = ByteBuffer.wrap(modelIdentifier).order(ByteOrder.BIG_ENDIAN).getInt();
        }

        Log.v(TAG, "Status: " + mStatusCode);
        Log.v(TAG, "Status message: " + mStatusCodeName);
        Log.v(TAG, "Element address: " + MeshParserUtils.bytesToHex(elementAddress, false));
        Log.v(TAG, "Element Address: " + MeshParserUtils.bytesToHex(elementAddress, false));
        Log.v(TAG, "Publish Address: " + MeshParserUtils.bytesToHex(publishAddress, false));
        Log.v(TAG, "App key index: " + MeshParserUtils.bytesToHex(appKeyIndex, false));
        Log.v(TAG, "Credential Flag: " + credentialFlag);
        Log.v(TAG, "Publish TTL: " + publishTtl);
        Log.v(TAG, "Publish Period: " + publishPeriod);
        Log.v(TAG, "Publish Retransmit Count: " + publishRetransmitCount);
        Log.v(TAG, "Publish Publish Interval Steps: " + publishRetransmitIntervalSteps);
        Log.v(TAG, "Model Identifier: " + MeshParserUtils.bytesToHex(modelIdentifier, false));
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    /**
     * Returns the element address that the key was bound to
     *
     * @return element address
     */
    public int getElementAddress() {
        return mElementAddress;
    }

    /**
     * Returns the global app key index.
     *
     * @return appkey index
     */
    public final int getAppKeyIndex() {
        return mAppKeyIndex;
    }

    /**
     * Returns the model identifier
     *
     * @return 16-bit sig model identifier or 32-bit vendor model identifier
     */
    public final int getModelIdentifier() {
        return mModelIdentifier;
    }
}
