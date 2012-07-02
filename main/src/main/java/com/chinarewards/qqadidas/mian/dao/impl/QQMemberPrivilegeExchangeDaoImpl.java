package com.chinarewards.qqadidas.mian.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.chinarewards.qqadidas.domain.QQActivityHistory;
import com.chinarewards.qqadidas.domain.status.GiftType;
import com.chinarewards.qqadidas.domain.status.PrivilegeStatus;
import com.chinarewards.qqadidas.mian.dao.QQMemberPrivilegeExchangeDao;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class QQMemberPrivilegeExchangeDaoImpl implements
		QQMemberPrivilegeExchangeDao {
	@Inject
	EntityManager em;

	@Override
	public boolean cdkeyExists(String cdkey) {
		try {
			Query q = em
					.createQuery("select count(qm.id) from QQActivityMember qm where qm.cdkey=?1");
			q.setParameter(1, cdkey);
			System.out.println(q.getSingleResult());
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

	@Override
	public PrivilegeStatus getPrivilegeStatusByCdkey(String cdkey) {
		try {
			if(!cdkeyExists(cdkey)){
				return null;
			}
			Query q = em
					.createQuery("select qm.privilegeStatus from QQActivityMember qm where qm.cdkey=?1 ");
			q.setParameter(1, cdkey);
			PrivilegeStatus ps = (PrivilegeStatus) q.getSingleResult();
			if (ps==PrivilegeStatus.NEW) {
				return PrivilegeStatus.NEW;
			}
			if (ps==PrivilegeStatus.HALF) {
				return PrivilegeStatus.HALF;
			}
			if (ps==PrivilegeStatus.DONE) {
				return PrivilegeStatus.DONE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public void setPrivilegeStatus(String cdkey, PrivilegeStatus privilegeStatus) {
		try {
			Query q = em
					.createQuery("update QQActivityMember qm set qm.privilegeStatus =?1 where qm.cdkey=?2");
			q.setParameter(1, privilegeStatus);
			q.setParameter(2, cdkey);
			q.executeUpdate();
			System.out.println("setPrivilegeStatus success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public QQActivityHistory getActivityHistoryByCdkeyAType(String cdkey, GiftType type) {
		try {
			QQActivityHistory qqActivityHistory;
			Query q = em.createQuery("select qh from QQActivityHistory qh where qh.cdkey=?1 and qh.aType=?2");
			q.setParameter(1, cdkey);
			q.setParameter(2, type);
			qqActivityHistory = (QQActivityHistory)q.getSingleResult();
			return qqActivityHistory;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void addQQActivityHistory(QQActivityHistory qqActivityHistory) {
		try{
			em.persist(qqActivityHistory);
			List<QQActivityHistory> qhs = em.createQuery("from QQActivityHistory")
					.getResultList();
			System.out.println(qhs);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateQQActivityHistory(QQActivityHistory qqActivityHistory) {
		try {
			em.merge(qqActivityHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
