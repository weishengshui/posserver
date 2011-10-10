package com.chinarewards.qqgbvpn.mgmtui.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chinarewards.qqgbvpn.common.SimpleDateTimeModule;
import com.chinarewards.qqgbvpn.domain.PageInfo;
import com.chinarewards.qqgbvpn.logic.journal.DefaultJournalModule;
import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentStore;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.util.JPATestCase;
import com.google.inject.Module;

/**
 * agent logic test
 * 
 * @author Seek
 *
 */
public class AgentLogicTest extends JPATestCase{

	public AgentLogicTest(){
		super();
	}
	

	/* (non-Javadoc)
	 * @see com.chinarewards.qqgbvpn.mgmtui.util.JPATestCase#getModules()
	 */
	protected Module[] getModules() {
		Module[] modules = super.getModules();
		
		List<Module> m = new ArrayList<Module>(Arrays.asList(modules));
		m.add(new DefaultJournalModule());
		m.add(new SimpleDateTimeModule());
		return m.toArray(new Module[0]);
	}




	public void testPosLogic() throws Exception{
		AgentLogic agentLogic = injector.getInstance(AgentLogic.class);
		AgentVO agentVO = new AgentVO();
		agentVO.setName("agent_001");
		agentVO.setEmail("agent_email");
		//save
		agentVO = agentLogic.save(agentVO);
		
		assertNotNull(agentVO);
		assertNotNull(agentVO.getId());
		
		//get Pos by Id
		AgentVO agentVO2  = agentLogic.findById(agentVO.getId());
		assertNotNull(agentVO2);
		assertEquals(agentVO2.getId(), agentVO.getId());
		
		//update
		agentVO2.setName("agent_002");
		agentLogic.update(agentVO2);
		AgentVO agentVO3 = agentLogic.findById(agentVO2.getId());
		assertEquals("agent_002", agentVO3.getName());
		
		//delete
		agentLogic.delete(agentVO.getId());
		agentVO = agentLogic.findById(agentVO.getId());
		assertNull(agentVO);
		
		//search
		{
			for(int i=0;i<10;i++){
				AgentVO agentVOTemp = new AgentVO();
				agentVOTemp.setName("pos_"+i);
				agentLogic.save(agentVOTemp);
			}
			
			AgentSearchVO agentSearchVO = new AgentSearchVO();
			agentSearchVO.setPage(1);
			agentSearchVO.setSize(3);
			PageInfo<AgentVO> pageInfo2 = agentLogic.queryAgent(agentSearchVO);
			assertNotNull(pageInfo2);
			assertEquals(pageInfo2.getRecordCount(), 10);
			assertEquals(pageInfo2.getItems().size(), 3);
			
			
			
			AgentSearchVO agentSearchVO2 = new AgentSearchVO();
			agentSearchVO2.setPage(4);
			agentSearchVO2.setSize(3);
			PageInfo<AgentVO> pageInfo3 = agentLogic.queryAgent(agentSearchVO2);
			assertNotNull(pageInfo3);
			assertEquals(pageInfo3.getRecordCount(), 10);
			assertEquals(pageInfo3.getItems().size(), 1);
			
			
			AgentSearchVO agentSearchVO3 = new AgentSearchVO();
			agentSearchVO3.setPage(1);
			agentSearchVO3.setSize(5);
			agentSearchVO3.setAgentName("8");
			PageInfo<AgentVO> pageInfo5 = agentLogic.queryAgent(agentSearchVO3);
			assertNotNull(pageInfo5);
			assertEquals(pageInfo5.getRecordCount(), 1);
			assertEquals(pageInfo5.getItems().size(), 1);
		}
	}
	
}
