package com.chinarewards.qqgbvpn.qqapi;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

import com.chinarewards.qqgbvpn.qqapi.util.TuanUtil;
import com.chinarewards.qqgbvpn.qqapi.vo.TuanSearchListVO;

/**
 * Hello world!
 *
 */
public class App  {
	
	
    public static void main( String[] args ) {
    	try {
    		File xmlFile = new File("E:/testdata/TuanSearchListVO.xml");
    		HashMap<String,Object> map = TuanUtil.parseXML(new FileInputStream(xmlFile), "//groupon/item");
    		//HashMap<String,Object> map = TuanUtil.parseXML(TuanUtil.sendPost("http://tuan.qq.com/api/pos/query", null));
    		System.out.println(map.get("resultCode"));
    		List<TuanSearchListVO> itemList = (List<TuanSearchListVO>) map.get("items");
    		for (TuanSearchListVO item : itemList) {
    			System.out.println(item.getGrouponId());
    			System.out.println(item.getGrouponName());
    			System.out.println(item.getMercName());
    			System.out.println(item.getListName());
    			System.out.println(item.getDetailName());
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
