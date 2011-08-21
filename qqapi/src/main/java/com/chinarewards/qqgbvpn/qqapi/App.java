package com.chinarewards.qqgbvpn.qqapi;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import com.chinarewards.qqgbvpn.qqapi.util.GroupBuyingUtil;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingSearchListVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingUnbindVO;
import com.chinarewards.qqgbvpn.qqapi.vo.GroupBuyingValidateResultVO;

/**
 * Hello world!
 *
 */
public class App  {
	
	
    public static void main( String[] args ) {
    	try {
    		File xmlFile = new File("E:/testdata/TuanValidateResultVO.xml");
    		HashMap<String,Object> map = GroupBuyingUtil.parseXML(new FileInputStream(xmlFile), "//groupon", GroupBuyingValidateResultVO.class);
    		//HashMap<String,Object> map = TuanUtil.parseXML(TuanUtil.sendPost("http://tuan.qq.com/api/pos/query", null));
    		System.out.println(map.get("resultCode"));
    		List<GroupBuyingValidateResultVO> itemList = (List<GroupBuyingValidateResultVO>) map.get("items");
    		for (GroupBuyingValidateResultVO item : itemList) {
    			/*System.out.println(item.getGrouponId());
    			System.out.println(item.getGrouponName());
    			System.out.println(item.getMercName());
    			System.out.println(item.getListName());
    			System.out.println(item.getDetailName());*/
    			/*System.out.println(item.getPosId());
    			System.out.println(item.getResultStatus());*/
    			System.out.println(item.getResultName());
    			System.out.println(item.getResultExplain());
    			System.out.println(item.getCurrentTime());
    			System.out.println(item.getUseTime());
    			System.out.println(item.getValidTime());
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
