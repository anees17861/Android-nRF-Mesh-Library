package no.nordicsemi.android.meshprovisioner.messages;

import android.support.annotation.NonNull;

import no.nordicsemi.android.meshprovisioner.meshmessagestates.ProvisionedMeshNode;
import no.nordicsemi.android.meshprovisioner.messagetypes.AccessMessage;

import static no.nordicsemi.android.meshprovisioner.messages.ConfigStatusMessage.StatusCodeNames.fromStatusCode;

public abstract class ConfigStatusMessage extends MeshMessage {

    final int AKF = 0;
    AccessMessage mMessage;
    int mStatusCode;
    String mStatusCodeName;

    public ConfigStatusMessage(final ProvisionedMeshNode node, @NonNull final AccessMessage message) {
        this(node, message.getAszmic());
        mMessage = message;
    }

    private ConfigStatusMessage(final ProvisionedMeshNode node, final int aszmic) {
        super(node, aszmic);
    }

    /**
     * Parses the status parameters returned by a status message
     */
    abstract void parseStatusParameters();

    @Override
    public final int getAkf() {
        return mMessage.getAkf();
    }

    @Override
    public final int getAid() {
        return mMessage.getAid();
    }

    @Override
    public final byte[] getParameters() {
        return mParameters;
    }

    /**
     * Returns the status code received by the status message
     *
     * @return Status code
     */
    public final int getStatusCode() {
        return mStatusCode;
    }

    /**
     * Returns the status message for the status code received by the status message
     *
     * @return Status message
     */
    public final String getStatusCodeName() {
        return mStatusCodeName;
    }

    /**
     * Returns the status code name for a status code returned by a status message.
     *
     * @param statusCode StatusCode received by the status message
     * @return The specific status code name
     */
    final String getStatusCodeName(final int statusCode) {
        switch (fromStatusCode(statusCode)) {
            case SUCCESS:
                return "Success";
            case INVALID_ADDRESS:
                return "Invalid Address";
            case INVALID_MODEL:
                return "Invalid Model";
            case INVALID_APPKEY_INDEX:
                return "Invalid AppKey Index";
            case INVALID_NETKEY_INDEX:
                return "Invalid NetKey Index";
            case INSUFFICIENT_RESOURCES:
                return "Insufficient Resources";
            case KEY_INDEX_ALREADY_STORED:
                return "Key Index Already Stored";
            case INVALID_PUBLISH_PARAMETERS:
                return "Invalid Publish Parameters";
            case NOT_A_SUBSCRIBE_MODEL:
                return "Not a Subscribe Model";
            case STORAGE_FAILURE:
                return "Storage Failure";
            case FEATURE_NOT_SUPPORTED:
                return "Feature Not Supported";
            case CANNOT_UPDATE:
                return "Cannot Update";
            case CANNOT_REMOVE:
                return "Cannot Remove";
            case CANNOT_BIND:
                return "Cannot Bind";
            case TEMPORARILY_UNABLE_TO_CHANGE_STATE:
                return "Temporarily Unable to Change State";
            case CANNOT_SET:
                return "Cannot Set";
            case UNSPECIFIED_ERROR:
                return "Unspecified Error";
            case INVALID_BINDING:
                return "Invalid Binding";
            case RFU:
            default:
                return "RFU";
        }
    }

    public enum StatusCodeNames {
        SUCCESS(0x00),
        INVALID_ADDRESS(0x01),
        INVALID_MODEL(0x02),
        INVALID_APPKEY_INDEX(0x03),
        INVALID_NETKEY_INDEX(0x04),
        INSUFFICIENT_RESOURCES(0x05),
        KEY_INDEX_ALREADY_STORED(0x06),
        INVALID_PUBLISH_PARAMETERS(0x07),
        NOT_A_SUBSCRIBE_MODEL(0x08),
        STORAGE_FAILURE(0x09),
        FEATURE_NOT_SUPPORTED(0x0A),
        CANNOT_UPDATE(0x0B),
        CANNOT_REMOVE(0x0C),
        CANNOT_BIND(0x0D),
        TEMPORARILY_UNABLE_TO_CHANGE_STATE(0x0E),
        CANNOT_SET(0x0F),
        UNSPECIFIED_ERROR(0x10),
        INVALID_BINDING(0x11),
        RFU(0x12);

        private final int statusCode;

        StatusCodeNames(final int statusCode) {
            this.statusCode = statusCode;
        }

        public static StatusCodeNames fromStatusCode(final int statusCode) {
            for (StatusCodeNames code : values()) {
                if (code.getStatusCode() == statusCode) {
                    return code;
                }
            }
            throw new IllegalArgumentException("Enum not found in StatusCodeNames");
        }

        public final int getStatusCode() {
            return statusCode;
        }
    }
}
