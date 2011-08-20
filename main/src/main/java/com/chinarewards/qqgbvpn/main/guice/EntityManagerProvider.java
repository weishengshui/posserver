package com.chinarewards.qqgbvpn.main.guice;

import javax.persistence.EntityManager;

import com.google.inject.Provider;

public class EntityManagerProvider implements Provider<EntityManager> {

	static ThreadLocal<EntityManager> tl = new ThreadLocal<EntityManager>();

	public EntityManager get() {
		return tl.get();
	}

	public void set(EntityManager em) {
		tl.set(em);
	}
}
