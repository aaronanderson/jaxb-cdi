package jaxbcdi.cdi;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

public abstract class Context {

	@Inject
	BeanManager bm;

	ThreadLocalState scope;

	public abstract ThreadLocal<ThreadLocalState> getThreadLocal();

	public void create() {
		scope = new ThreadLocalState();
		scope.scopes = new HashMap<Class<?>, Map<String, ScopedInstance<?>>>();
		scope.instances = new HashSet<ScopedInstance<?>>();
	}

	public void begin() {
		getThreadLocal().set(scope);
	}

	public void end() {
		getThreadLocal().remove();
	}

	public void destroy() {
		//Since this is not a CDI NormalScope we are responsible for managing the entire lifecycle, including
		//destroying the beans
		for (ScopedInstance entry2 : scope.instances) {
			entry2.bean.destroy(entry2.instance, entry2.ctx);
		}
		scope = null;
	}

	static class ThreadLocalState {
		Map<Class<?>, Map<String, ScopedInstance<?>>> scopes;
		Set<ScopedInstance<?>> instances;

	}

	public static class ScopedInstance<T> {
		Bean<T> bean;
		CreationalContext<T> ctx;
		T instance;
	}

	public static abstract class CDIContextImpl implements javax.enterprise.context.spi.Context {
		
		BeanManager bm;
		
		public CDIContextImpl(BeanManager bm){
			this.bm=bm;
		}
		
		public abstract ThreadLocal<ThreadLocalState> getThreadLocal();

		@Override
		public abstract Class<? extends Annotation> getScope();

		@Override
		public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
			ScopedInstance si = null;
			Bean bean = (Bean) contextual;
			//System.out.println("bean " + bean);
			ThreadLocalState tscope = getThreadLocal().get();
			if (tscope == null) {
				throw new ContextNotActiveException();
			}
			//this happens for the JSF EL bean creation
			if (creationalContext == null) {
				creationalContext = bm.createCreationalContext(bean);
			}
			if (bean.getName() != null) {
				Map<Class<?>, Map<String, ScopedInstance<?>>> scopes = tscope.scopes;
				Map<String, ScopedInstance<?>> scoped = scopes.get(bean.getBeanClass());
				if (scoped == null) {
					scoped = new HashMap<String, ScopedInstance<?>>();
					scopes.put(bean.getBeanClass(), scoped);
				}
				si = scoped.get(bean.getName());
				if (si == null) {
					si = new ScopedInstance();
					si.bean = bean;
					si.ctx = creationalContext;
					si.instance = bean.create(creationalContext);
					tscope.instances.add(si);
					scoped.put(bean.getName(), si);
				}
			} else {
				si = new ScopedInstance();
				si.bean = bean;
				si.ctx = creationalContext;
				tscope.instances.add(si);
				si.instance = bean.create(creationalContext);
			}
			//System.out.println("returning1 " + si.instance + " cc " + creationalContext + " bean " + bean);
			return (T) si.instance;
		}

		@Override
		public <T> T get(Contextual<T> contextual) {
			return get(contextual, null);
		}

		@Override
		public boolean isActive() {
			return getThreadLocal().get() != null ? true : false;
		}

	}

}