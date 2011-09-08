package com.chinarewards.qqgbvpn.mgmtui.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Tools {
	/**
	 * 打印实体信息
	 * 
	 * @param vName
	 * @param obj
	 * @return
	 */
	public static String objToString(Object obj) {
		StringBuffer termStr = new StringBuffer();
		if (null == obj)
			return "";
		try {
			// 调用反射方法
			Field[] fi = getFields(obj);
			termStr.append(obj.getClass().getSimpleName());
			termStr.append(" [ ");
			for (int i = 0; i < fi.length; i++) {
				fi[i].setAccessible(true);
				if ("".equals(fi[i].get(obj)) || null == fi[i].get(obj)) {
					termStr.append(" , ");
					termStr.append(fi[i].getName());
					termStr.append(" = null ");
				} else {
					termStr.append(" , ");
					termStr.append(fi[i].getName());
					termStr.append(" = " + fi[i].get(obj));
				}
			}
			termStr.append(" ] ");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return termStr.toString();
	}

	/**
	 * 实现对具体类的反射操作
	 */
	private static Field[] getFields(Object obj) {
		try {
			Class<?> c = Class.forName(obj.getClass().getName());
			Field[] field = c.getDeclaredFields();
			return field;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 判断对象是否为空，或者对象toString后长度是否为0，或者内容为"null". 如满足任意一项内容，返回TRUE，否则返回 false
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmptyString(Object s) {
		return s == null || s.toString().trim().length() == 0
				|| s.toString().trim().equalsIgnoreCase("null");
	}
	
	/**
	 * 取唯一nubmer(用于生成回收单)
	 * @return
	 */
	public static String getOnlyNumber(String prefix) {
		Random rd = new Random();
		StringBuffer sb = new StringBuffer(prefix);
		sb.append("-");
		sb.append(getNow());
		sb.append("-");
		sb.append(String.format("%06d", rd.nextInt(999999)));
		return sb.toString();
	}
	
	/**
	 * 当前时间（毫秒级）
	 * @return
	 */
	public static String getNow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.format(new Date());
		return sdf.format(new Date());
	}
}
