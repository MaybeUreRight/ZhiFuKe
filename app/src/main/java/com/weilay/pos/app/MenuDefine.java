package com.weilay.pos.app;

import com.weilay.pos.R;

public enum MenuDefine {
	//首页
	//更多
	MORE_MANAGE(11,"设备管理",R.drawable.icon_manage),
	MORE_REFUND(12,"退款",R.drawable.icon_refund),
	MORE_CHARGE(13,"找零",R.drawable.icon_find),
	MORE_SHIFT(14,"交班",R.drawable.icon_shift),
	MORE_PRINT(15,"打印",R.drawable.icon_print),
	MORE_SHUTDOWN(16,"关机",R.drawable.icon_closeboot);
	
	  // 成员变量
    private int id;
    private String title;
    private int icon;

    

    // 构造方法
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	MenuDefine(int id, String title,int icon) {
        this.id = id;
        this.title = title;
        this.icon=icon;
    }
}

