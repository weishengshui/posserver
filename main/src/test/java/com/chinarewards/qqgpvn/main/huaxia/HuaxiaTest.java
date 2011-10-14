package com.chinarewards.qqgpvn.main.huaxia;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import com.chinarewards.qqgbpvn.main.CommonTestConfigModule;
import com.chinarewards.qqgbpvn.main.test.JpaGuiceTest;
import com.chinarewards.qqgbvpn.core.jpa.JpaPersistModuleBuilder;
import com.chinarewards.qqgbvpn.domain.Agent;
import com.chinarewards.qqgbvpn.domain.HuaxiaRedeem;
import com.chinarewards.qqgbvpn.domain.Pos;
import com.chinarewards.qqgbvpn.domain.PosAssignment;
import com.chinarewards.qqgbvpn.domain.event.DomainEvent;
import com.chinarewards.qqgbvpn.domain.event.Journal;
import com.chinarewards.qqgbvpn.domain.status.RedeemStatus;
import com.chinarewards.qqgbvpn.main.guice.AppModule;
import com.chinarewards.qqgbvpn.main.logic.huaxia.HuaxiaRedeemManager;
import com.chinarewards.qqgbvpn.main.vo.HuaxiaRedeemVO;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class HuaxiaTest extends JpaGuiceTest {
	
	@Override
	protected Module[] getModules() {

		CommonTestConfigModule confModule = new CommonTestConfigModule();
		Configuration configuration = confModule.getConfiguration();

		JpaPersistModule jpaModule = new JpaPersistModule("posnet");
		JpaPersistModuleBuilder builder = new JpaPersistModuleBuilder();
		builder.configModule(jpaModule, configuration, "db");

		return new Module[] { confModule, jpaModule, new AppModule() };
	}

	@Test
	public void testHuaxiaRedeemSearch() {
		String cardNum = "5767";
		
		HuaxiaRedeem redeem = new HuaxiaRedeem();
		redeem.setCardNum(cardNum);
		redeem.setStatus(RedeemStatus.UNUSED);
		redeem.setCreateDate(new Date());
		this.getEm().persist(redeem);
		
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum(cardNum);
		
		
		HuaxiaRedeemManager gbm = getInjector().getInstance(HuaxiaRedeemManager.class);
		HuaxiaRedeemVO vo = gbm.huaxiaRedeemSearch(params);
		log.debug("redeemCount : {}",vo.getRedeemCount());
		log.debug("result : {}",vo.getResult());
		assertEquals(1, vo.getRedeemCount().intValue());
		assertEquals(HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS, vo.getResult().intValue());
	}
	
	@Test
	public void testHuaxiaRedeemConfirm() {
		String cardNum = "5767";
		String posId = "REWARDS-0001";
		String agentName = "agentName";
		
		Pos pos = new Pos();
		pos.setPosId(posId);
		this.getEm().persist(pos);
		Agent agent = new Agent();
		agent.setName(agentName);
		this.getEm().persist(agent);
		PosAssignment pa = new PosAssignment();
		pa.setAgent(agent);
		pa.setPos(pos);
		this.getEm().persist(pa);
		
		HuaxiaRedeem redeem = new HuaxiaRedeem();
		redeem.setCardNum(cardNum);
		redeem.setStatus(RedeemStatus.UNUSED);
		redeem.setCreateDate(new Date());
		this.getEm().persist(redeem);
		
		HuaxiaRedeemVO params = new HuaxiaRedeemVO();
		params.setCardNum(cardNum);
		params.setPosId(posId);
		
		HuaxiaRedeemManager gbm = getInjector().getInstance(HuaxiaRedeemManager.class);
		HuaxiaRedeemVO vo = gbm.huaxiaRedeemConfirm(params);
		log.debug("result : {}",vo.getResult());
		assertEquals(HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS, vo.getResult().intValue());
	}
	
	@Test
	public void testHuaxiaRedeemAck() {
		String cardNum = "5767";
		String posId = "REWARDS-0001";
		String agentName = "agentName";
		String ackId = "";
		String chanceId = "";
		
		Pos pos = new Pos();
		pos.setPosId(posId);
		this.getEm().persist(pos);
		Agent agent = new Agent();
		agent.setName(agentName);
		this.getEm().persist(agent);
		PosAssignment pa = new PosAssignment();
		pa.setAgent(agent);
		pa.setPos(pos);
		this.getEm().persist(pa);
		
		HuaxiaRedeem redeem = new HuaxiaRedeem();
		redeem.setCardNum(cardNum);
		redeem.setStatus(RedeemStatus.UNUSED);
		redeem.setCreateDate(new Date());
		this.getEm().persist(redeem);
		
		HuaxiaRedeemManager gbm = getInjector().getInstance(HuaxiaRedeemManager.class);
		
		HuaxiaRedeemVO paramsV = new HuaxiaRedeemVO();
		paramsV.setCardNum(cardNum);
		paramsV.setPosId(posId);
		
		HuaxiaRedeemVO voV = gbm.huaxiaRedeemConfirm(paramsV);
		assertEquals(HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS, voV.getResult().intValue());
		
		ackId = voV.getAckId();
		chanceId = voV.getChanceId();
		
		HuaxiaRedeemVO paramsA = new HuaxiaRedeemVO();
		paramsA.setCardNum(cardNum);
		paramsA.setPosId(posId);
		paramsA.setChanceId(chanceId);
		paramsA.setAckId(ackId);
		
		HuaxiaRedeemVO voA = gbm.huaxiaRedeemAck(paramsA);
		log.debug("result : {}",voA.getResult());
		assertEquals(HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS, voA.getResult().intValue());
	}
	
	@Test
	public void testHuaxiaRedeemAckFailed() {
		//模拟POS1验证成功后，POS2直接ACK，此时ACK失败，没有兑换机会
		String cardNum = "1111";
		String posId1 = "REWARDS-0001";
		String posId2 = "REWARDS-0002";
		String agentName = "agentName";
		String ackId = "";
		String chanceId = "";
		
		Pos pos = new Pos();
		pos.setPosId(posId1);
		this.getEm().persist(pos);
		Pos pos2 = new Pos();
		pos2.setPosId(posId2);
		this.getEm().persist(pos2);
		Agent agent = new Agent();
		agent.setName(agentName);
		this.getEm().persist(agent);
		PosAssignment pa = new PosAssignment();
		pa.setAgent(agent);
		pa.setPos(pos);
		this.getEm().persist(pa);
		PosAssignment pa2 = new PosAssignment();
		pa2.setAgent(agent);
		pa2.setPos(pos2);
		this.getEm().persist(pa2);
		
		HuaxiaRedeem redeem = new HuaxiaRedeem();
		redeem.setCardNum(cardNum);
		redeem.setStatus(RedeemStatus.UNUSED);
		redeem.setCreateDate(new Date());
		this.getEm().persist(redeem);
		
		HuaxiaRedeemManager gbm = getInjector().getInstance(HuaxiaRedeemManager.class);
		
		HuaxiaRedeemVO paramsV = new HuaxiaRedeemVO();
		paramsV.setCardNum(cardNum);
		paramsV.setPosId(posId1);
		
		HuaxiaRedeemVO voV = gbm.huaxiaRedeemConfirm(paramsV);
		assertEquals(HuaxiaRedeemVO.REDEEM_RESULT_SUCCESS, voV.getResult().intValue());
		
		ackId = voV.getAckId();
		chanceId = voV.getChanceId();
		
		HuaxiaRedeemVO paramsA = new HuaxiaRedeemVO();
		paramsA.setCardNum(cardNum);
		paramsA.setPosId(posId2);
		paramsA.setChanceId(chanceId);
		paramsA.setAckId(ackId);
		
		HuaxiaRedeemVO voA = gbm.huaxiaRedeemAck(paramsA);
		log.debug("result : {}",voA.getResult());
		assertEquals(HuaxiaRedeemVO.REDEEM_RESULT_NONE, voA.getResult().intValue());
		
		Journal j1 = (Journal) this.getEm().createQuery("select j from Journal j where j.entityId = '" + chanceId + "'").getSingleResult();
		log.debug("POS1 event : {}",j1.getEvent());
		assertEquals(DomainEvent.HUAXIA_REDEEM_COMFIRM_OK.toString(), j1.getEvent());
		
		Journal j2 = (Journal) this.getEm().createQuery("select j from Journal j where j.entityId = '" + posId2 + "'").getSingleResult();
		log.debug("POS2 event : {}",j2.getEvent());
		assertEquals(DomainEvent.HUAXIA_REDEEM_ACK_FAILED.toString(), j2.getEvent());
	}


}
