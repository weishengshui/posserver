package com.chinarewards.qqgbvpn.core.excel;

import java.io.IOException;
import java.io.InputStream;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;

public interface ExcelService {

	/**
	 * 根据CSV的数据得到excel的流
	 * @param data
	 * @return
	 */
	public InputStream getExcelInputStreamByCsv(FinanceReportHistory data)
			throws RowsExceededException, WriteException, IOException;
}
