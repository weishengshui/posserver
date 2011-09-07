package com.chinarewards.qqgbvpn.mgmtui.struts.delivery;

import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.DeliveryLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.delivery.DeliveryNoteVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;
import com.chinarewards.qqgbvpn.mgmtui.struts.BasePagingToolBarAction;
import com.google.inject.Injector;

/**
 * delivery action
 * 
 * @author Seek	
 *
 */
public class DeliveryAction extends BasePagingToolBarAction {
	
	private static final long serialVersionUID = 6936323925311944355L;
	
	private DeliveryLogic deliveryLogic;
	
	private List<DeliveryNoteVO> deliveryNoteVOList;
	
	@Override
	public String execute(){
		if(super.getCurrentPage()==0){
			super.setCurrentPage(1);
		}
		return SUCCESS;
	}
	
	/**
	 * description：交付单列表
	 * @time 2011-9-7   上午10:40:00
	 * @author Seek
	 */
	public String deliveryList(){
		PaginationTools paginationTools = new PaginationTools();
		paginationTools.setCountOnEachPage(super.getPageSize());
		paginationTools.setStartIndex( (super.getCurrentPage()-1) * super.getPageSize() );
		
		try{
			PageInfo<DeliveryNoteVO> pageInfo = getDeliveryLogic().fetchDeliveryList(paginationTools);
			
			deliveryNoteVOList = pageInfo.getItems();
			super.setCountTotal(pageInfo.getRecordCount());
			
			//template
			final String urlMark = "{*}";
			String urlTemplate = super.buildURLTemplate(super.getCurrentPath(), "&", "currentPage=", urlMark);
			
			setUrlTemplate(urlTemplate);
			setUrlMark(urlMark);
		}catch(Throwable e){
			log.error(e.getMessage(), e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	//---------------------------------------------------//
	
	private DeliveryLogic getDeliveryLogic() {
		Injector injector = (Injector)ServletActionContext.getServletContext().getAttribute(Injector.class.getName());
		deliveryLogic = injector.getInstance(DeliveryLogic.class);
		return deliveryLogic;
	}
	
	public List<DeliveryNoteVO> getDeliveryNoteVOList() {
		return deliveryNoteVOList;
	}
	
	public void setDeliveryNoteVOList(List<DeliveryNoteVO> deliveryNoteVOList) {
		this.deliveryNoteVOList = deliveryNoteVOList;
	}
	
}
