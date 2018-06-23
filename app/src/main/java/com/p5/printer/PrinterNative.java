package com.p5.printer;

public class PrinterNative {
	public native byte write(byte[] buf, int len);

	public native int read(byte[] buf, int len);

	public native int poll(byte[] buf);
	
	public native int finishpoll();
	

	public native int openforread();

	public native int openSS();

	public native int openUS();

	public native int openUE();

	public native int close();

	static {
		System.loadLibrary("serialPrinter");
	}

}
