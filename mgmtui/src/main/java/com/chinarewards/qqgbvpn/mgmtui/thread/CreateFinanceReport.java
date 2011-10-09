package com.chinarewards.qqgbvpn.mgmtui.thread;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import com.chinarewards.qqgbvpn.domain.FinanceReportHistory;
import com.chinarewards.qqgbvpn.domain.status.FinanceReportHistoryStatus;
import com.chinarewards.qqgbvpn.mgmtui.logic.finance.FinanceManager;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.vo.FinanceReportVO;
import com.chinarewards.utils.StringUtil;

public class CreateFinanceReport extends Thread {
	
	/**
	 * 默认超时时间10分钟
	 */
	private long timeout = 600000;
	
	private FinanceManager financeMgr;
	
	private FinanceReportSearchVO searchVO;
	
	private String financeReportId;
	
	private Integer baseAmountSum = 0;
	
	private Long actuallyValCountSum = 0L;
	
	private Long beyondValCountSum = 0L;
	
	private Double beyondAmountSum = 0.0;
	
	private Double amountSum = 0.0;
	
	public CreateFinanceReport() {}
	
	public CreateFinanceReport(FinanceManager financeMgr,
			FinanceReportSearchVO searchVO,
			String financeReportId) {
		super();
		this.financeMgr = financeMgr;
		this.searchVO = searchVO;
		this.financeReportId = financeReportId;
	}
	
	public CreateFinanceReport(FinanceManager financeMgr,
			FinanceReportSearchVO searchVO,
			String financeReportId, long timeout) {
		super();
		this.financeMgr = financeMgr;
		this.searchVO = searchVO;
		this.financeReportId = financeReportId;
		this.timeout = timeout;
	}

	public void run() {
		if (!StringUtil.isEmptyString(financeReportId)) {
			FinanceReportHistory history = financeMgr.getFinanceReportHistoryById(financeReportId);
			if (history != null ) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				FutureTask<String> future = new FutureTask<String>(
						new Callable<String>() {
							public String call() {
								StringBuffer sb = new StringBuffer("");
								sb.append(this.getReportTitle());
								List<FinanceReportVO> financeReportVOList = financeMgr
										.searchFinanceReport(searchVO);
								if (financeReportVOList != null
										&& financeReportVOList.size() > 0) {
									sb.append(this
											.getReportData(financeReportVOList));
								}
								sb.append(getReportSum());
								return sb.toString();
							}

							private StringBuffer getReportTitle() {
								StringBuffer sb = new StringBuffer();
								sb.append("腾讯终端验证机帐单\r\n");
								sb.append("月份");
								sb.append(",");
								sb.append("代理商名称");
								sb.append(",");
								sb.append("POS机编号");
								sb.append(",");
								sb.append("基本运营费a");
								sb.append(",");
								sb.append("实际验证个数");
								sb.append(",");
								sb.append("超额验证个数b");
								sb.append(",");
								sb.append("单价c");
								sb.append(",");
								sb.append("超额运营费d=b*c");
								sb.append(",");
								sb.append("运营费用e=a+d");
								sb.append(",");
								sb.append("\r\n");
								return sb;
							}

							private StringBuffer getReportData(
									List<FinanceReportVO> financeReportVOList) {
								StringBuffer sb = new StringBuffer();
								for (FinanceReportVO vo : financeReportVOList) {
									sb.append(vo.getReportMonth());
									sb.append(",");
									sb.append(vo.getAgentName());
									sb.append(",");
									sb.append(vo.getPosId());
									sb.append(",");
									sb.append(vo.getBaseAmount());
									sb.append(",");
									sb.append(vo.getActuallyValCount());
									sb.append(",");
									sb.append(vo.getBeyondValCount());
									sb.append(",");
									sb.append(vo.getUnitPrice());
									sb.append(",");
									sb.append(vo.getBeyondAmount());
									sb.append(",");
									sb.append(vo.getAmount());
									sb.append(",");
									sb.append("\r\n");

									baseAmountSum += vo.getBaseAmount();
									actuallyValCountSum += vo
											.getActuallyValCount();
									beyondValCountSum += vo.getBeyondValCount();
									beyondAmountSum = add(beyondAmountSum,vo.getBeyondAmount());
									amountSum = add(amountSum,vo.getAmount());
								}
								return sb;
							}
							
							private double add(double v1, double v2) {
								BigDecimal b1 = new BigDecimal(Double.toString(v1));
								BigDecimal b2 = new BigDecimal(Double.toString(v2));
								return b1.add(b2).doubleValue();
							}

							private StringBuffer getReportSum() {
								StringBuffer sb = new StringBuffer();
								sb.append("合计");
								sb.append(",");
								sb.append(",");
								sb.append(",");
								sb.append(baseAmountSum);
								sb.append(",");
								sb.append(actuallyValCountSum);
								sb.append(",");
								sb.append(beyondValCountSum);
								sb.append(",");
								sb.append(",");
								sb.append(beyondAmountSum);
								sb.append(",");
								sb.append(amountSum);
								sb.append(",");
								sb.append("\r\n");
								return sb;
							}
						});
				executor.execute(future);
				try {
					String str = future.get(timeout, TimeUnit.MILLISECONDS);
					history.setReportDetail(str);
					history.setModifyDate(new Date());
					history.setStatus(FinanceReportHistoryStatus.COMPLETION);
				} catch (Exception e) {
					future.cancel(true);
					e.printStackTrace();
					history.setReportDetail("超时." + e.toString());
					history.setModifyDate(new Date());
					history.setStatus(FinanceReportHistoryStatus.FAILED);
				} finally {
					executor.shutdown();
				}

				financeMgr.saveFinanceReportHistory(history);
			}
		}
	}
	
}
