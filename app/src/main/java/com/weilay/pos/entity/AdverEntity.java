package com.weilay.pos.entity;

import java.io.Serializable;

import com.weilay.pos.app.UrlDefine;

/******
 * @detail 广告的实体
 * @author Administrator
 *
 */
public class AdverEntity implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	    private String picurl;
	    private String bgcolor;
	    private String txttitle;
	    private String txsubtitle;
	    private String adposition;

	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	    public String getPicurl() {
	        return UrlDefine.getRealUrl(picurl);
	    }

	    public void setPicurl(String picurl) {
	        this.picurl = picurl;
	    }

	    public String getBgcolor() {
	        return bgcolor;
	    }

	    public void setBgcolor(String bgcolor) {
	        this.bgcolor = bgcolor;
	    }

	    public String getTxttitle() {
	        return txttitle;
	    }

	    public void setTxttitle(String txttitle) {
	        this.txttitle = txttitle;
	    }

	    public String getTxsubtitle() {
	        return txsubtitle;
	    }

	    public void setTxsubtitle(String txsubtitle) {
	        this.txsubtitle = txsubtitle;
	    }

	    public String getAdposition() {
	        return adposition;
	    }

	    public void setAdposition(String adposition) {
	        this.adposition = adposition;
	    }
}
