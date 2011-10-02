package com.chinarewards.qqgbpvn.testing.prepare;

import java.io.File;
import java.util.UUID;
import com.chinarewards.qqgbpvn.testing.prepare.util.DataBase;

public final class InitPosData {
	
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/mock_test_1";
	
	private static final String MYSQL_USERNAME = "root";
	
	private static final String MYSQL_PASSWORD = "123456";
	
	private static final String CSV_FILE_ABSOLUTE_PATH = "d:/pos_data.csv";
	
	public static void main(String[] args) {
//		initPosData(1L, 10000L);
		
		exportCsvFile();
	}
	
	/**
	 * description：export csv data file
	 * @time 2011-9-28   上午09:49:52
	 * @author Seek
	 */
	private static final void exportCsvFile(){
		File file = new File(CSV_FILE_ABSOLUTE_PATH);
		if(file.exists()){
			file.delete();
		}
		
		DataBase db = DataBase.getInstance(DataBase.MYSQL, MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
		try {
			//set @x 是connection级别的		同一个connection生效
			db.excuteSql("set @x=0;");
			db.excuteSql("select @x:=ifnull(@x,0)+1 as rownum,posId,secret from pos into outfile '" + 
					CSV_FILE_ABSOLUTE_PATH+"' fields terminated by ',' optionally enclosed by '' " +
					"lines terminated by '\\n'; ");
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			db.close();
		}
	}
	
	/**
	 * description：初始化POS表数据
	 * @param begin
	 * @param end
	 * @time 2011-9-28   上午09:48:50
	 * @author Seek
	 */
	private static final void initPosData(long begin, long end){
		DataBase db = DataBase.getInstance(DataBase.MYSQL, MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
		try {
			for(long i=begin; i<=end; i++){
				db.addBatch("insert into pos(id, dstatus, istatus, ostatus, posId, secret, " +
						"upgradeRequired, version) values('"+UUID.randomUUID().toString()+"', " +
						"'RETURNED', 'INITED', 'ALLOWED', '"+buildPosId(i)+"', '12345678', 0, 0); ");
			}
			db.commit();
		}catch(Throwable e){
			db.rollBack();
			e.printStackTrace();
		}finally{
			db.close();
		}
	}
	
	/**
	 * description：根据x序号，创建posId
	 * @param factor
	 * @return
	 * @time 2011-9-28   上午09:49:00
	 * @author Seek
	 */
	private static final String buildPosId(long factor){
		final int POS_ID_MAX_LEN = 12;
		
		StringBuffer posId = new StringBuffer("T-");
		
		int srcLen = String.valueOf(factor).length();
		if(srcLen > 10){
			return posId.toString();
		}
		
		int surplusLen = POS_ID_MAX_LEN - posId.length();
		for(int i=surplusLen-srcLen;i>0;i--){
			posId.append("0");
		}
		posId.append(factor);
		
		return posId.toString();
	}
	
}
