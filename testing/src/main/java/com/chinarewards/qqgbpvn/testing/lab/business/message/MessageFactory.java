package com.chinarewards.qqgbpvn.testing.lab.business.message;

import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosGetFirmwareFragmentBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosGetQQGroupBuyListBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosGetQQGroupBuyValidationBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosInitBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosLoginBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosRequestUpgradeBuildMessage;

public final class MessageFactory {
	
	/**
	 * description：get a BuildMessage
	 * @param businessType  a pos busniess type
	 * @return
	 * @time 2011-9-30   下午06:19:00
	 * @author Seek
	 */
	public static final BuildMessage getBuildMessage(BusinessType businessType){
		BuildMessage buildMessage = null;
		switch(businessType){
			case PosInit:
				buildMessage = new PosInitBuildMessage();	break;
			case PosLogin:
				buildMessage = new PosLoginBuildMessage();	break;
			case PosGetQQGroupBuyList:
				buildMessage = new PosGetQQGroupBuyListBuildMessage();  break;
			case PosGetQQGroupBuyValidation:
				buildMessage = new PosGetQQGroupBuyValidationBuildMessage();  break;
			case PosRequestUpgrade:
				buildMessage = new PosRequestUpgradeBuildMessage();	 break;
			case PosGetFirmwareFragment:
				buildMessage = new PosGetFirmwareFragmentBuildMessage();  break;
		}
		return buildMessage;
	}
	
}
