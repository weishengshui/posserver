package com.chinarewards.qq.meishi.guice;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

/**
 * @time 2012-3-5   下午04:24:38
 * @author Seek
 */
public class TestAppModule extends AbstractModule {
	
	private List<Module> list = new ArrayList<Module>();

	
	public TestAppModule(){
		
		/* pos QQ meishi service */
		list.add(new TestQQMeishiModule());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		for(Module module:list){
			install(module);
		}
	}

	public List<Module> getList() {
		return list;
	}

	public void setList(List<Module> list) {
		this.list = list;
	}

}
