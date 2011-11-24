package com.chinarewards.qqgbvpn.mgmtui.excel.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.mgmtui.excel.ExcelService;

public class ExcelServiceImpl implements ExcelService {

	@Override
	public InputStream getExcelInputStreamByCsv(FinanceReportHistory data)
			throws RowsExceededException, WriteException, IOException {
		//将OutputStream转化为InputStream  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        putData2OutputStream(out, data);  
        return new ByteArrayInputStream(out.toByteArray()); 
	}
	
	private void putData2OutputStream(OutputStream out,
			FinanceReportHistory data) throws IOException,
			RowsExceededException, WriteException {
		jxl.write.Label label;  
        WritableWorkbook workbook;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        workbook = Workbook.createWorkbook(out);  
        WritableSheet sheet = workbook.createSheet("Sheet1", 0);
        //设置列的宽度
        sheet.setColumnView(0, 18);
        sheet.setColumnView(1, 18);
        sheet.setColumnView(2, 18);
        sheet.setColumnView(3, 18);
        sheet.setColumnView(4, 18);
        sheet.setColumnView(5, 18);
        sheet.setColumnView(6, 18);
        sheet.setColumnView(7, 18);
        
        //设置字体   
        jxl.write.WritableFont wfont_Title = new jxl.write.WritableFont(WritableFont.createFont("宋体"),14);   
        WritableCellFormat s_Title = new WritableCellFormat(wfont_Title);
        // 设置居中   
        s_Title.setAlignment(Alignment.CENTRE);
        // 设置单元格的背景颜色   
        s_Title.setBackground(jxl.format.Colour.WHITE);
        
        jxl.write.WritableFont wfont_12 = new jxl.write.WritableFont(WritableFont.createFont("宋体"),12);   
        WritableCellFormat font_12 = new WritableCellFormat(wfont_12);
        font_12.setBackground(jxl.format.Colour.WHITE);
        
        jxl.write.WritableFont wfont_11 = new jxl.write.WritableFont(WritableFont.createFont("宋体"),11);   
        WritableCellFormat font_11 = new WritableCellFormat(wfont_11);
        font_11.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
        
        int row = 0;
        label = new jxl.write.Label(0, row, "对帐单", s_Title);  
        sheet.addCell(label);
        //合并单元格
        sheet.mergeCells(0, row, 7, row);
        row++;
        label = new jxl.write.Label(0, row, "", font_12);
        sheet.addCell(label);
        sheet.mergeCells(0, row, 7, row);
        row++;
        if (data.getStartDate() != null) {
        	label = new jxl.write.Label(0, row, "账单周期：" + sdf.format(data.getStartDate()) + "至" + sdf.format(data.getEndDate()), font_12);
        } else {
        	label = new jxl.write.Label(0, row, "账单周期：截止于" + sdf.format(data.getEndDate()), font_12);
        }
        sheet.addCell(label);
        sheet.mergeCells(0, row, 7, row);
        row++;
        String agentName = data.getAgentName() != null ? data.getAgentName() : "";
        label = new jxl.write.Label(0, row, "客户名称：" + agentName, font_12);
        sheet.addCell(label);
        sheet.mergeCells(0, row, 5, row);
        label = new jxl.write.Label(6, row, "单位：人民币，元", font_12);
        sheet.addCell(label);
        sheet.mergeCells(6, row, 7, row);
        row++;
        
        label = new jxl.write.Label(0, row, "代理商名称", font_11);  
        sheet.addCell(label);
        label = new jxl.write.Label(1, row, "POS机编号", font_11);  
        sheet.addCell(label);
        label = new jxl.write.Label(2, row, "基本运营费a", font_11);  
        sheet.addCell(label);
        label = new jxl.write.Label(3, row, "实际验证个数", font_11);  
        sheet.addCell(label);
        label = new jxl.write.Label(4, row, "超额验证个数b", font_11);  
        sheet.addCell(label);
        label = new jxl.write.Label(5, row, "单价c", font_11);  
        sheet.addCell(label);
        label = new jxl.write.Label(6, row, "超额运营费d=b*c", font_11);  
        sheet.addCell(label);
        label = new jxl.write.Label(7, row, "运营费用e=a+d", font_11);  
        sheet.addCell(label);
        row++;
        
        String detail = data.getReportDetail();
        String[] dataLength = detail.split("\r\n");
        if (detail != null && !"".equals(detail)) {
        	if (dataLength.length > 3) {
        		for (int i = 2; i < (dataLength.length - 1); i++) {
        			String[] tempData = dataLength[i].split(",");
        			label = new jxl.write.Label(0, row, tempData[1], font_11);
        			sheet.addCell(label);
        			label = new jxl.write.Label(1, row, tempData[2], font_11);
        			sheet.addCell(label);
        			label = new jxl.write.Label(2, row, tempData[3], font_11);
        			sheet.addCell(label);
        			label = new jxl.write.Label(3, row, tempData[4], font_11);
        			sheet.addCell(label);
        			label = new jxl.write.Label(4, row, tempData[5], font_11);
        			sheet.addCell(label);
        			label = new jxl.write.Label(5, row, tempData[6], font_11);
        			sheet.addCell(label);
        			label = new jxl.write.Label(6, row, tempData[7], font_11);
        			sheet.addCell(label);
        			label = new jxl.write.Label(7, row, tempData[8], font_11);
        			sheet.addCell(label);
        			row++;
        		}
        	}
        }
        
        label = new jxl.write.Label(0, row, "合计", font_11);
        sheet.addCell(label);
        label = new jxl.write.Label(1, row, "", font_11);
        sheet.addCell(label);
        String[] s = dataLength[dataLength.length - 1].split(",");
        label = new jxl.write.Label(2, row, s[3], font_11);
        sheet.addCell(label);
        label = new jxl.write.Label(3, row, s[4], font_11);
        sheet.addCell(label);
        label = new jxl.write.Label(4, row, s[5], font_11);
        sheet.addCell(label);
        label = new jxl.write.Label(5, row, "", font_11);
        sheet.addCell(label);
        label = new jxl.write.Label(6, row, s[7], font_11);
        sheet.addCell(label);
        label = new jxl.write.Label(7, row, s[8], font_11);
        sheet.addCell(label);
        row++;
        
        label = new jxl.write.Label(0, row, "财务提示：请及时安排付款。付款信息：", font_12);  
        sheet.addCell(label);
        sheet.mergeCells(0, row, 7, row);
        row++;
        label = new jxl.write.Label(0, row, "收款单位：深圳积享通信息技术有限公司", font_12);  
        sheet.addCell(label);
        sheet.mergeCells(0, row, 7, row);
        row++;
        label = new jxl.write.Label(0, row, "开户行：中国招商银行深圳分行南山支行，银行帐号：755916320610902", font_12);  
        sheet.addCell(label);
        sheet.mergeCells(0, row, 7, row);
        
        workbook.write();
        workbook.close();
	}

}
