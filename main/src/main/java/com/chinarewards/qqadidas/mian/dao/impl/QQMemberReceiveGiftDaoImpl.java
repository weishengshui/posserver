package com.chinarewards.qqadidas.mian.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.QQActivityMember;
import com.chinarewards.qqadidas.domain.status.GiftStatus;
import com.chinarewards.qqadidas.mian.dao.QQMemberReceiveGiftDao;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class QQMemberReceiveGiftDaoImpl implements QQMemberReceiveGiftDao {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Inject
	EntityManager em;

	public QQActivityMember getQQActivityMemberByCdkey(String cdkey) {
		try {
			Query q = em
					.createQuery("select qm from QQActivityMember qm where qm.cdkey=?1");
			q.setParameter(1, cdkey);
			QQActivityMember qm = (QQActivityMember)q.getSingleResult();
			return qm;
		} catch (Exception e) {
			log.debug("there is no QQActivityMember for cdkey={}",cdkey);
			return null;
		}
	}

	public boolean giftStatus(String cdkey) {

		try {
			Query q = em
					.createQuery("select count(qm.id) from QQActivityMember qm where qm.cdkey=?1 and qm.giftStatus=?2");
			q.setParameter(1, cdkey);
			q.setParameter(2, GiftStatus.NEW);
			long result = (Long) q.getSingleResult();
			if (result > 0) {
				return true;
			}
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Transactional
	public boolean updateQQActivityMember(QQActivityMember qqActivityMember) {
		try {
			em.merge(qqActivityMember);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addQQActivityHistory(QQActivityHistory qah) {
		try {
			em.persist(qah);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

}
