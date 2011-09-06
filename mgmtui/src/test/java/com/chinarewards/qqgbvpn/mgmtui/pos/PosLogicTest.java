package com.chinarewards.qqgbvpn.mgmtui.pos;

import org.junit.Test;

import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.model.util.PaginationTools;
import com.chinarewards.qqgbvpn.mgmtui.util.JPATestCase;

/**
 * pos logice test
 * 
 * @author huangwei
 *
 */
public class PosLogicTest extends JPATestCase{

	public PosLogicTest(){
		super();
	}
	
	
	@Test
	public void testPosLogic() throws Exception{
		PosLogic posLogic = injector.getInstance(PosLogic.class);
		PosVO posVO = new PosVO();
		posVO.setPosId("pos_001");
		posVO.setSimPhoneNo("13480009000");
		//save
		PosVO posVO1 = posLogic.savePos(posVO);
		assertNotNull(posVO1);
		assertNotNull(posVO1.getId());
		
		//get Pos by Id
		PosVO posVO2 = posLogic.getPosById(posVO1.getId());
		assertNotNull(posVO2);
		assertEquals(posVO2.getPosId(), posVO.getPosId());
		
		//update
		posVO2.setPosId("pos_002");
		posLogic.updatePos(posVO2);
		PosVO posVO3 = posLogic.getPosById(posVO2.getId());
		assertEquals("pos_002", posVO3.getPosId());
		
		//delete
		posLogic.deletePosById(posVO1.getId());
		posVO2 = posLogic.getPosById(posVO1.getId());
		assertNull(posVO2);
		
		//search
		{
			for(int i=0;i<10;i++){
				PosVO posVOTmp = new PosVO();
				posVOTmp.setPosId("pos_"+i);
				posVOTmp.setSimPhoneNo("1348000910"+i);
				posLogic.savePos(posVOTmp);
			}
			PageInfo<PosVO>  pageInfo = posLogic.queryPos(null, null);
			assertNotNull(pageInfo);
			assertEquals(pageInfo.getRecordCount(), 10);
			assertEquals(pageInfo.getItems().size(), 10);
			
			PaginationTools paginationTools = new PaginationTools();
			paginationTools.setCountOnEachPage(6);
			paginationTools.setStartIndex(0);
			PosSearchVO posSearchVO = new PosSearchVO();
			posSearchVO.setPosId("pos");
			pageInfo = posLogic.queryPos(null, paginationTools);
			assertEquals(pageInfo.getRecordCount(), 10);
			assertEquals(pageInfo.getItems().size(), 6);

			paginationTools.setCountOnEachPage(6);
			paginationTools.setStartIndex(8);
			pageInfo = posLogic.queryPos(null, paginationTools);
			assertEquals(pageInfo.getRecordCount(), 10);
			assertEquals(pageInfo.getItems().size(), 2);
		}
	}
	
	
	
}
