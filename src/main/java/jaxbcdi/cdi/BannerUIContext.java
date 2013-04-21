package jaxbcdi.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.BeanManager;

import jaxbcdi.BannerUIScope;



public class BannerUIContext extends Context{
	
	
	static ThreadLocal<ThreadLocalState> bannerUIScopeState = new ThreadLocal<ThreadLocalState>();

	@Override
	public ThreadLocal<ThreadLocalState> getThreadLocal() {
		return bannerUIScopeState;
	}
	
	
	public static class CDIContext extends CDIContextImpl{
		
		public CDIContext(BeanManager bm){
			super(bm);
		}

		@Override
		public ThreadLocal<ThreadLocalState> getThreadLocal() {
			return bannerUIScopeState;
		}


		@Override
		public Class<? extends Annotation> getScope() {
			return BannerUIScope.class;
		}
	}
	
}
