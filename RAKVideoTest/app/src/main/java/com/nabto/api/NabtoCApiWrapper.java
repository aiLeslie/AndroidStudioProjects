package com.nabto.api;

class NabtoCApiWrapper {
    static {
    	//System.loadLibrary("nabto_client_api");
    	System.loadLibrary("nabto_client_api_jni");
    }

    public static native int nabtoStartup(String nabtoHomeDir);
    public static native int nabtoSetStaticResourceDir(String nabtoResDir);
    public static native int nabtoShutdown();
    public static native String nabtoLookupExistingProfile();
    public static native int nabtoCreateProfile(String email, String password);
    public static native Session nabtoOpenSession(String email, String password);
    public static native int nabtoCloseSession(Object session);
    public static native UrlFetchResult nabtoFetchUrl(String url, Object session);
    public static native int nabtoSignup(String email, String password);
    public static native int nabtoResetAccountPassword(String email);
    public static native int nabtoProbeNetwork(int timeoutMillis,
            String hostname);
    public static native UrlFetchResult nabtoSubmitPostData(String nabtoUrl,
            byte[] postData, String postMimeType, Object session);
    public static native String[] nabtoGetProtocolPrefixes();
    public static native String nabtoGetSessionToken(Object session);
    
    public static native Tunnel nabtoTunnelOpenTcp(int localPort, String nabtoHost, String remoteHost, int remotePort, Object session);
    public static native int nabtoTunnelCloseTcp(Object tunnel);
    public static native TunnelInfo nabtoTunnelInfo(Object tunnel);
}