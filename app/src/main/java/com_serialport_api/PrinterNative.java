package com_serialport_api;

public class PrinterNative {

	public native byte write(byte[] buf, int len);

	public native int read(byte[] buf, int len);

	public native int openforread();

	public native int openSS();

	public native int openUS();

	public native int close();

	public native int reopenU();
	static {
		System.loadLibrary("serial_port");
	}
}
