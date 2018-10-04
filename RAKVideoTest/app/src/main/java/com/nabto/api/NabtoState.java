package com.nabto.api;

import android.util.Log;

public enum NabtoState {
    NTCS_CLOSED,
    NTCS_CONNECTING,
    NTCS_READY_FOR_RECONNECT,
    NTCS_UNKNOWN,
    NTCS_LOCAL,
    NTCS_REMOTE_P2P,
    NTCS_REMOTE_RELAY,
    NTCS_REMOTE_RELAY_MICRO,
    FAILED;

    static NabtoState fromInteger(int nabtoState) {
    	nabtoState++;
        if (nabtoState < NabtoState.values().length && nabtoState >= 0) {
            return NabtoState.values()[nabtoState];
        } else {
        	Log.d("NabtoState", "Index out of bounds: " + nabtoState);
            return FAILED;
        }
    }
}