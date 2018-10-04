package com.nabto.api;

public class Session {
	private Object session;
	private NabtoStatus nabtoStatus;
	
	public Session(Object _session, int _nabtoStatus)
	{
		session = _session;
		nabtoStatus = NabtoStatus.fromInteger(_nabtoStatus);
	}
	
	public Object getSession()
	{
		return session;
	}
	
	public NabtoStatus getStatus()
	{
		return nabtoStatus;
	}
	
}
