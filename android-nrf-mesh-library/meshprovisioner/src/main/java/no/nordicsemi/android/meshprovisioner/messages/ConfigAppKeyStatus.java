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
 * To be used as a wrapper class for when creating the ConfigAppKeyStatus Message.
 */
@SuppressWarnings("unused")
public class ConfigAppKeyStatus extends ConfigStatusMessage {

    private static final String TAG = ConfigAppKeyStatus.class.getSimpleName();
    private static final int OP_CODE = ConfigMessageOpCodes.CONFIG_APPKEY_STATUS;
    private int mNetKeyIndex;
    private int mAppKeyIndex;

    /**
     * Constructs the ConfigAppKeyStatus mMessage.
     *
     * @param node    Node from which the mMessage originated from
     * @param message Access Message
     */
    public ConfigAppKeyStatus(final ProvisionedMeshNode node, @NonNull final AccessMessage message) {
        super(node, message);
        this.mMessage = message;
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
    final void parseStatusParameters() {
        mStatusCode = mParameters[0];
        mStatusCodeName = getStatusCodeName(mStatusCode);

        final byte[] netKeyIndex = new byte[]{(byte) (mParameters[2] & 0x0F), mParameters[1]};
        mNetKeyIndex = ByteBuffer.wrap(netKeyIndex).order(ByteOrder.BIG_ENDIAN).getShort();

        final byte[] appKeyIndex = new byte[]{(byte) ((mParameters[4] & 0xF0) >> 4), (byte) (mParameters[4] << 4 | ((mParameters[3] & 0xF0) >> 4))};
        mAppKeyIndex = ByteBuffer.wrap(appKeyIndex).order(ByteOrder.BIG_ENDIAN).getShort();

        Log.v(TAG, "Status: " + mStatusCode);
        Log.v(TAG, "Status message: " + mStatusCodeName);
        Log.v(TAG, "Net key index: " + MeshParserUtils.bytesToHex(netKeyIndex, false));
        Log.v(TAG, "App key index: " + MeshParserUtils.bytesToHex(appKeyIndex, false));
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    /**
     * Returns the global index of the net key.
     *
     * @return netkey index
     */
    public int getNetKeyIndex() {
        return mNetKeyIndex;
    }

    /**
     * Returns the global app key index.
     *
     * @return appkey index
     */
    public int getAppKeyIndex() {
        return mAppKeyIndex;
    }
}
