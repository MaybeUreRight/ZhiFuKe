package com.weilay.pos.listener;

/*******
 * @detail 定义请求的状态
 * @author rxwu
 *
 */
public enum NetCodeEnum {

	NOJSON(-100, "不是标准的json格式"), FAILED(-101, "请求失败"), TIMEOUT(-102, "请求超时"), NETWORK_UNABLE(-103,
			"网络不可用"), UNKNOWHOST(-104, "找不到请求地址"), OTHERCODE(-105, "请求错误"), NOIMAGE(-106, "不是图片");
	private int code;
	private String str;

	NetCodeEnum(int code, String str) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.str = str;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
