package com.weilay.pos.entity;

import com.weilay.pos.app.TicketColor;

import android.os.Parcel;
import android.os.Parcelable;

public class SendTicketInfo implements Parcelable {
	private String id;
	private String merchantlogo;
	private String stock;
	private String cardinfo;
	private String deadline;
	private String merchentname;
	private String url2qrcode;
	private String type;
	private String color;

	public static final Parcelable.Creator<SendTicketInfo> CREATOR = new Creator<SendTicketInfo>() {

		@Override
		public SendTicketInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SendTicketInfo[size];
		}

		@Override
		public SendTicketInfo createFromParcel(Parcel parcel) {
			// TODO Auto-generated method stub
			SendTicketInfo sti = new SendTicketInfo();
			sti.setMerchantlogo(parcel.readString());
			sti.setStock(parcel.readString());
			sti.setCardinfo(parcel.readString());
			sti.setDeadline(parcel.readString());
			sti.setMerchentname(parcel.readString());
			sti.setUrl2qrcode(parcel.readString());
			sti.setType(parcel.readString());
			sti.setColor(parcel.readString());
			sti.setId(parcel.readString());
			return sti;
		}
	};
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getColor() {
		return TicketColor.getcolor(color);
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl2qrcode() {
		return url2qrcode;
	}

	public void setUrl2qrcode(String url2qrcode) {
		this.url2qrcode = url2qrcode;
	}

	public String getMerchantlogo() {
		return merchantlogo;
	}

	public void setMerchantlogo(String merchantlogo) {
		this.merchantlogo = merchantlogo;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getCardinfo() {
		return cardinfo;
	}

	public void setCardinfo(String cardinfo) {
		this.cardinfo = cardinfo;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getMerchentname() {
		return merchentname;
	}

	public void setMerchentname(String merchentname) {
		this.merchentname = merchentname;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		// TODO Auto-generated method stub
		parcel.writeString(merchantlogo);
		parcel.writeString(stock);
		parcel.writeString(cardinfo);
		parcel.writeString(deadline);
		parcel.writeString(merchentname);
		parcel.writeString(url2qrcode);
		parcel.writeString(type);
		parcel.writeString(color);
		parcel.writeString(id);
	}

}
