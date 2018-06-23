package com.weilay.pos.listener;

import java.util.List;

import com.weilay.pos.entity.AdverEntity;

public interface CashPayListener {
	public void onSuc(List<AdverEntity> adverEntities);
	public void onErr();
		
	
}
