package com.nabto.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.util.Log;

public class NabtoApi {
    private File nabtoHomeDirectory;
    private File nabtoResourceDirectory;

    private String email;
    private String password;

    private boolean initialised = false;
    
    public NabtoApi(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        
    	nabtoHomeDirectory = new File(context.getFilesDir(), "nabto");
    	nabtoResourceDirectory = new File(context.getFilesDir(), "share/nabto");
        
        copyDirContentsToLocation(context.getAssets(), "share", new File(
                context.getFilesDir() + "/share"), true);
    }

    private void copyDirContentsToLocation(AssetManager manager,
            String fileToCopy, File fileLocation, boolean overwrite) {
        try {
            String[] filesInDir = manager.list(fileToCopy);
            if (filesInDir.length == 0) {
                copyFromAssets(manager, fileToCopy, fileLocation, overwrite);
            } else {
                for (String fileInDir : filesInDir) {
                    copyDirContentsToLocation(manager, fileToCopy + "/"
                            + fileInDir, new File(fileLocation + "/"
                            + fileInDir), overwrite);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Could not get assets from directory " + fileToCopy, e);
        }
    }

    private void copyFromAssets(AssetManager manager, String asset,
            File fileLocation, boolean overwrite) {
        // Only write to file if it does not exist already
        if (fileLocation.exists() && !overwrite)
            return;

        // Create necessary directory structure
        fileLocation.getParentFile().mkdirs();
        Log.d(this.getClass().getSimpleName(), "Writing asset file: " + asset + " to "
              + fileLocation.getAbsolutePath());
        try {
            InputStream inStream = new BufferedInputStream(manager.open(asset,
                    AssetManager.ACCESS_STREAMING));
            OutputStream outStream = new BufferedOutputStream(
                    new FileOutputStream(fileLocation));
            byte[] buffer = new byte[10240]; // 10KB
            int length = 0;
            while ((length = inStream.read(buffer)) >= 0) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not write file to "
                    + fileLocation, e);
        }
    }

    public NabtoStatus startup() {
        return NabtoStatus.fromInteger(NabtoCApiWrapper
                .nabtoStartup(nabtoHomeDirectory.getAbsolutePath()));
    }
    
    public NabtoStatus setStaticResourceDir() {
        return NabtoStatus.fromInteger(NabtoCApiWrapper
                .nabtoSetStaticResourceDir(nabtoResourceDirectory.getAbsolutePath()));
    }

    public NabtoStatus shutdown() {
        return NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoShutdown());
    }

    public String lookupExistingProfile() {
        return NabtoCApiWrapper.nabtoLookupExistingProfile();
    }

    public NabtoStatus createProfile(String email, String password) {
        return NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoCreateProfile(
                email, password));
    }

    public Session openSession(String email, String password) {
    	Session session = NabtoCApiWrapper.nabtoOpenSession(email, password);
    	Log.d(this.getClass().getSimpleName(), "Open session: " + session.getStatus());
    	return session; 
    }
    
    public NabtoStatus init(String email, String password) {
        this.email = email;
        this.password = password;
        initialised = true;
        return NabtoStatus.OK;
    }

    public NabtoStatus close(Session session) {
        return closeSession(session);
    }

    private NabtoStatus closeSession(Session session) {
        return NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoCloseSession(session.getSession()));
    }

    public UrlFetchResult fetchUrl(String url, Session session) {
        return NabtoCApiWrapper.nabtoFetchUrl(url, session.getSession());
    }

    public NabtoStatus nabtoSignup(String email, String password) {
        return NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoSignup(email,
                password));
    }

    public NabtoStatus nabtoResetAccountPassword(String email) {
        return NabtoStatus.fromInteger(NabtoCApiWrapper
                .nabtoResetAccountPassword(email));
    }

    public NabtoStatus nabtoProbeNetwork(int timeoutMillis, String hostname) {
        return NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoProbeNetwork(
                timeoutMillis, hostname));
    }

    public UrlFetchResult nabtoSubmitPostData(String nabtoUrl, byte[] postData,
            String postMimeType, Session session) {
        return NabtoCApiWrapper.nabtoSubmitPostData(nabtoUrl, postData,
                postMimeType, session.getSession());
    }

    public Collection<String> getProtocolPrefixes() {

        ArrayList<String> prefixes = new ArrayList<String>();
        String[] res = NabtoCApiWrapper.nabtoGetProtocolPrefixes();
        if (res != null) {
            for (String s : res) {
                prefixes.add(s);
            }
        }
        return prefixes;
    }
    
    public String getSessionToken(Session session) {
    	return NabtoCApiWrapper.nabtoGetSessionToken(session.getSession());
    }

    static public boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return path.delete();
    }

    public void pause(Session session) {
        NabtoStatus s = closeSession(session);
        if (s != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to pause api");
        }
        shutdown();
    }

    public Session resume() {
        startup();
        Session s = null;
        if (initialised) {
             s = openSession(this.email, this.password);
            if (s.getStatus() != NabtoStatus.OK) {
                Log.d(this.getClass().getSimpleName(), "Failed to resume api");
            }
        }
        return s;
    }
    
    public Tunnel nabtoTunnelOpenTcp(int localport, String nabtoHost, String remoteHost, int remotePort, Object session)
    {
    	Tunnel t; 
    	t = NabtoCApiWrapper.nabtoTunnelOpenTcp(localport, nabtoHost, remoteHost, remotePort, session);
    	if (t.getStatus() != NabtoStatus.OK)
    		Log.d(this.getClass().getSimpleName(), "Failed to open a tunnel");
    	return t;
    }
    
    public NabtoStatus nabtoTunnelCloseTcp(Object tunnel)
    {
    	NabtoStatus s;
    	s = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoTunnelCloseTcp(tunnel));
    	if (s != NabtoStatus.OK)
    		Log.d(this.getClass().getSimpleName(), "Failed to close the tunnel");
    	return s;
    }
    
    public TunnelInfo nabtoTunnelInfo(Object tunnel)
    {
    	return NabtoCApiWrapper.nabtoTunnelInfo(tunnel);
    }
}