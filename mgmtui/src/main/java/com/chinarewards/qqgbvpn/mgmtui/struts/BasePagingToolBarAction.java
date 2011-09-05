package com.chinarewards.qqgbvpn.mgmtui.struts;

/**
 * description：分页条父类
 * @copyright binfen.cc
 * @projectName b2c-war
 * @time 2011-6-7   上午11:45:32
 * @author Seek
 */
public abstract class BasePagingToolBarAction extends BaseAction {
	
	private static final long serialVersionUID = 8039536369926880055L;
	
	/**
	 * 当前页号
	 */
	private int currentPage;
	
	/**
	 * 每页显示条数
	 */
	private int pageSize = 10;	//TODO  Struts2 孙子不让用size，  不知为何.		一旦用size属性值永远都是10
	
	/**
	 * 总数据量
	 */
	private int countTotal;
	
	/**
	 * 分页条中按钮数量
	 */
	private int butNum;
	
	/**
	 * URL模版   /WebContext/.../xx.html
	 */
	private String urlTemplate;
	
	/**
	 * 标记
	 */
	private String urlMark;
	
	/**
	 * 调用者传入的回调函数名
	 */
	private String setPageFunName;
	
	/**
	 * description：生成URL模版	只针对于常规的               &key=value形式
	   example：/search/queryShopList?a=1&shopSearchForm.page=37&shopSearchForm.size=3&shopSearchForm.regionNum=1
	   
	 * @param path 请包含URI+参数
	 * @param delimiter 分隔符
	 * @param key 请包含=号     例如：page=50    那么key等于 key=
	 * @param urlMark URL标记
	 * @return
	 * @throws Exception
	 * @time 2011-6-10   下午01:44:48
	 * @author Seek
	 */
	protected String buildURLTemplate(String path, String delimiter, String key, String urlMark) throws Exception {
		log.debug("getURLTemplate() invoke...");
		log.debug("path:"+path);
		log.debug("delimiter:"+delimiter);
		log.debug("key:"+key);
		log.debug("urlMark:"+urlMark);
		
		int beginIndex;
		int endIndex;
		if((beginIndex = path.lastIndexOf(key)) == -1){
			throw new Exception("URI is format error!");
		}
		
		String currentGroupParm = null;
		if((endIndex = path.indexOf(delimiter, beginIndex)) == -1){		//delimiter一般是&
			endIndex = path.length();
		}
		
		currentGroupParm = path.substring(beginIndex, endIndex);
		log.debug("currentGroupParm:"+currentGroupParm);
		
		String URLTemplate = path.replace(currentGroupParm, key+urlMark);
		log.debug("URLTemplate:"+URLTemplate);
		return URLTemplate;
	}

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	public int getButNum() {
		return butNum;
	}

	public void setButNum(int butNum) {
		this.butNum = butNum;
	}

	public String getUrlTemplate() {
		return urlTemplate;
	}

	public void setUrlTemplate(String urlTemplate) {
		this.urlTemplate = urlTemplate;
	}

	public String getUrlMark() {
		return urlMark;
	}

	public void setUrlMark(String urlMark) {
		this.urlMark = urlMark;
	}

	public String getSetPageFunName() {
		return setPageFunName;
	}

	public void setSetPageFunName(String setPageFunName) {
		this.setPageFunName = setPageFunName;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
