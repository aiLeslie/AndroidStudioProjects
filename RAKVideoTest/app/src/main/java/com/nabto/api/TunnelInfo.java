package com.nabto.api;

public class TunnelInfo {
	private NabtoStatus nabtoStatus;
	private NabtoState nabtoState;
	public int nabtoStateInt;
	public int nabtoStatusInt;
    public int nabtoPort;
	
	public TunnelInfo(int nabtoStatus, int nabtoState, int port)
	{
		this.nabtoStatus = NabtoStatus.fromInteger(nabtoStatus);
		this.nabtoState = NabtoState.fromInteger(nabtoState);
		this.nabtoStateInt = nabtoState;
		this.nabtoStatusInt = nabtoStatus;
        this.nabtoPort = port;
	}
	
	public NabtoStatus getNabtoStatus()
	{
		return nabtoStatus;
	}

	public NabtoState getNabtoState()
	{
		return nabtoState;
	}

    public int getNabtoPort() {
        return nabtoPort;
    }
}