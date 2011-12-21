package com.chinarewards.qqgbvpn.mgmtui.pos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.OptimisticLockException;

import org.junit.Test;

import com.chinarewards.qqgbvpn.common.SimpleDateTimeModule;
import com.chinarewards.qqgbvpn.logic.journal.DefaultJournalModule;
import com.chinarewards.qqgbvpn.mgmtui.logic.pos.PosLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.pos.PosVO;
import com.chinarewards.qqgbvpn.mgmtui.util.JPATestCase;
import com.google.inject.Module;

/**
 * pos logice test
 * 
 * @author huangwei
 * 
 */
public class PosLogicTest extends JPATestCase {

	public PosLogicTest() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinarewards.qqgbvpn.mgmtui.util.JPATestCase#getModules()
	 */
	protected Module[] getModules() {
		Module[] modules = super.getModules();

		List<Module> m = new ArrayList<Module>(Arrays.asList(modules));
		m.add(new DefaultJournalModule());
		m.add(new SimpleDateTimeModule());
		return m.toArray(new Module[0]);
	}
	
	/**
	 * description：测试Version
	 * @throws Exception
	 * @time 2011-9-13   下午03:52:22
	 * @author Seek
	 */
	@Test
	public void testOptimisticLock() throws Exception {
		PosLogic posLogic = injector.getInstance(PosLogic.class);
		PosVO posVO = new PosVO();
		posVO.setPosId("pos_001");
		posVO.setSimPhoneNo("13480009000");
		PosVO posVO1 = posLogic.savePos(posVO);
		
		
		System.out.println("ID:"+posVO1.getId());
		System.out.println("Version:"+posVO1.getVersion());
		
		
		PosVO posVO55 = posLogic.getPosById(posVO.getId());
		PosVO posVO66 = posLogic.getPosById(posVO.getId());
		
		
		System.out.println("Version:"+posVO55.getVersion());
		System.out.println("Version:"+posVO66.getVersion());
		
		posVO55.setSimPhoneNo("18112345678");
		posVO66.setSimPhoneNo("18112345677");
		
		try{
			posLogic.updatePos(posVO55);
			posLogic.updatePos(posVO66);
		}catch(OptimisticLockException e){
			assertTrue(true);
		}
		PosVO posVO77 = posLogic.getPosById(posVO.getId());
		PosVO posVO88 = posLogic.getPosById(posVO.getId());
		
		System.out.println("Version:"+posVO77.getVersion());
		System.out.println("Version:"+posVO88.getVersion());
	}
	
/*
	@Test
	//hibernate对虚拟数据的方言和mysql的方言类型支持有差异
	public void testPosLogic() throws Exception {
		PosLogic posLogic = injector.getInstance(PosLogic.class);
		PosVO posVO = new PosVO();
		posVO.setPosId("pos_001");
		posVO.setSimPhoneNo("13480009000");
		PosVO posVO1 = posLogic.savePos(posVO);
		assertNotNull(posVO1);
		assertNotNull(posVO1.getId());

		// get Pos by Id
		PosVO posVO2 = posLogic.getPosById(posVO1.getId());
		assertNotNull(posVO2);
		assertEquals(posVO2.getPosId(), posVO.getPosId());

		// update
		posVO2.setPosId("pos_002");
		posLogic.updatePos(posVO2);
		PosVO posVO3 = posLogic.getPosById(posVO2.getId());
		assertEquals("pos_002", posVO3.getPosId());

		// delete
		posLogic.deletePosById(posVO1.getId());
		posVO2 = posLogic.getPosById(posVO1.getId());
		assertNull(posVO2);

		// search
		{
			PosSearchVO posSearchVO = new PosSearchVO();
			posSearchVO.setPosId("pos");
			posSearchVO.setPage(1);
			posSearchVO.setSize(2);
			
			for (int i = 0; i < 10; i++) {
				PosVO posVOTmp = new PosVO();
				posVOTmp.setPosId("pos_" + i);
				posVOTmp.setSimPhoneNo("1348000910"+i);
				posLogic.savePos(posVOTmp);
			}
			PageInfo<PosVO> pageInfo = posLogic.queryPos(posSearchVO);
			assertNotNull(pageInfo);
			assertEquals(pageInfo.getRecordCount(), 10);
			assertEquals(pageInfo.getItems().size(), 2);
			
			pageInfo = posLogic.queryPos(posSearchVO);
			assertEquals(pageInfo.getRecordCount(), 10);
			assertEquals(pageInfo.getItems().size(), 2);
			
			pageInfo = posLogic.queryPos(posSearchVO);
			assertEquals(pageInfo.getRecordCount(), 10);
			assertEquals(pageInfo.getItems().size(), 2);
		}
	}
*/
}
