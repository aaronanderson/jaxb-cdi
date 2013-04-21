package jaxbcdi.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.BeanManager;

import jaxbcdi.GlobalUIScope;




public class GlobalUIContext extends Context{
	
	
	static ThreadLocal<ThreadLocalState> globalUIScopeState = new ThreadLocal<ThreadLocalState>();

	@Override
	public ThreadLocal<ThreadLocalState> getThreadLocal() {
		return globalUIScopeState;
	}
	
	
	public static class CDIContext extends CDIContextImpl{
		
		public CDIContext(BeanManager bm){
			super(bm);
		}

		@Override
		public ThreadLocal<ThreadLocalState> getThreadLocal() {
			return globalUIScopeState;
		}


		@Override
		public Class<? extends Annotation> getScope() {
			return GlobalUIScope.class;
		}
	}

	
}
