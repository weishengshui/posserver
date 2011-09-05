package com.chinarewards.qqgbvpn.mgmtui.agent;

import com.chinarewards.qqgbvpn.mgmtui.logic.agent.AgentLogic;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentSearchVO;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentStore;
import com.chinarewards.qqgbvpn.mgmtui.model.agent.AgentVO;
import com.chinarewards.qqgbvpn.mgmtui.util.JPATestCase;

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
			AgentStore agentStore = agentLogic.queryAgent(null);
			assertNotNull(agentStore);
			assertEquals(agentStore.getCountTotal(), 10);
			assertEquals(agentStore.getAgentVOList().size(), 10);
			
			
			AgentSearchVO agentSearchVO = new AgentSearchVO();
			agentSearchVO.setPage(1);
			agentSearchVO.setSize(3);
			AgentStore agentStore2 = agentLogic.queryAgent(agentSearchVO);
			assertNotNull(agentStore2);
			assertEquals(agentStore2.getCountTotal(), 10);
			assertEquals(agentStore2.getAgentVOList().size(), 3);
			
			
			
			AgentSearchVO agentSearchVO2 = new AgentSearchVO();
			agentSearchVO2.setPage(4);
			agentSearchVO2.setSize(3);
			AgentStore agentStore3 = agentLogic.queryAgent(agentSearchVO2);
			assertNotNull(agentStore3);
			assertEquals(agentStore3.getCountTotal(), 10);
			assertEquals(agentStore3.getAgentVOList().size(), 1);
			
			
			AgentSearchVO agentSearchVO3 = new AgentSearchVO();
			agentSearchVO3.setPage(1);
			agentSearchVO3.setSize(5);
			agentSearchVO3.setAgentName("8");
			AgentStore agentStore5 = agentLogic.queryAgent(agentSearchVO3);
			assertNotNull(agentStore5);
			assertEquals(agentStore5.getCountTotal(), 1);
			assertEquals(agentStore5.getAgentVOList().size(), 1);
		}
	}
	
}
