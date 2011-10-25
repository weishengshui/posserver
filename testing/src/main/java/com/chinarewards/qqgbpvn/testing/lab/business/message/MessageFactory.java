package com.chinarewards.qqgbpvn.testing.lab.business.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosEchoBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosGetFirmwareFragmentBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosGetQQGroupBuyListBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosGetQQGroupBuyValidationBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosInitBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosLoginBuildMessage;
import com.chinarewards.qqgbpvn.testing.lab.business.message.impl.PosRequestUpgradeBuildMessage;

/**
 * description：a Message factory
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-10-21   上午11:14:24
 * @author Seek
 */
public final class MessageFactory {
	
	private static final Map<BusinessType, BuildMessage> BUILD_MESSAGE_CACHE = 
			Collections.synchronizedMap(new HashMap<BusinessType, BuildMessage>());
	
	/**
	 * description：get a BuildMessage
	 * @param businessType  a pos busniess type
	 * @return
	 * @time 2011-9-30   下午06:19:00
	 * @author Seek
	 */
	public static final BuildMessage getBuildMessage(BusinessType businessType){
		BuildMessage buildMessage = null;
		
		if(BUILD_MESSAGE_CACHE.get(businessType) == null){
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
				case PosEcho:
					buildMessage = new PosEchoBuildMessage();  break;
			}
			
			BUILD_MESSAGE_CACHE.put(businessType, buildMessage);
		}else {
			buildMessage = BUILD_MESSAGE_CACHE.get(businessType);
		}
		return buildMessage;
	}
	
}
