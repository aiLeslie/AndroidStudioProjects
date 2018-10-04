package com.nabto.api;

import android.util.Log;

public enum NabtoStatus {
    OK,                     ///< operation successful.
    NO_PROFILE,             ///< no user profile found in home directory.
    ERROR_READING_CONFIG,   ///< could not read config file.
    API_NOT_INITIALIZED,    ///< nabtoStartup() was not invoked as the first function.
    INVALID_SESSION,        ///< operation requires a valid session.
    OPEN_CERT_OR_PK_FAILED, ///< certificate or private key files could not be opened.
    UNLOCK_PK_FAILED,       ///< private key could not be decrypted with specified password.
    PORTAL_LOGIN_FAILURE,   ///< could not login to portal to sign cert (invalid email/password for active portal (urlPortalDomain in config)).
    CERT_SIGNING_ERROR,     ///< portal failed when signing certificate request.
    CERT_SAVING_FAILURE,    ///< could not save signed certificate.
    ADDRESS_IN_USE,         ///< could not sign up with specified email address as it is already in use.
    INVALID_ADDRESS,        ///< could not sign up with specified email address as it is invalid.
    NO_NETWORK,             ///< no network available.
    CONNECT_TO_HOST_FAILED, ///< could not connect to specified host.
    STREAMING_UNSUPPORTED,  ///< peer does not support streaming.
    INVALID_STREAM,         ///< an invalid stream handle was specified.
    DATA_PENDING,           ///< unacknowledged stream data pending.
    BUFFER_FULL,            ///< all stream data slots are full.
    FAILED,                 ///< an unspecified error occurred, necessary to check logfile to find out what actually went wrong.
    INVALID_TUNNEL,         ///< an invalid tunnel handle was specified.
    ILLEGAL_PARAMETER,      ///< a parameter to a function is not supported.
    INVALID_RESOURCE,       ///< an invalid asynchronous resource was specified.
    ERROR_CODE_COUNT;        ///< number of posible error codes. This number is always larger than all other error codes.

    static NabtoStatus fromInteger(int nabtoStatus) {
        if (nabtoStatus < NabtoStatus.values().length) {
            return NabtoStatus.values()[nabtoStatus];
        } else {
        	Log.d("NabtoStatus", "Index out of bounds: " + nabtoStatus);
            return FAILED;
        }
    }
}