package jaxbcdi.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTargetFactory;

import jaxbcdi.BannerUI;
import jaxbcdi.BannerUIScope;
import jaxbcdi.Counter;
import jaxbcdi.GlobalUI;
import jaxbcdi.GlobalUIScope;

public class CDIExtension implements Extension {
	
	protected InjectionTargetFactory<GlobalUI> globalTargetFactory;
	protected InjectionTargetFactory<BannerUI> bannerTargetFactory;
	
	
	
	
	public InjectionTargetFactory<GlobalUI> getGlobalTargetFactory() {
		return globalTargetFactory;
	}

	public InjectionTargetFactory<BannerUI> getBannerTargetFactory() {
		return bannerTargetFactory;
	}

	public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {

		bbd.addScope(GlobalUIScope.class, false, false);
		bbd.addScope(BannerUIScope.class, false, false);
		
		AnnotatedType globalType =bm.createAnnotatedType(GlobalUI.class); 
		bbd.addAnnotatedType(globalType);
		globalTargetFactory = bm.getInjectionTargetFactory(globalType);
		
		AnnotatedType bannerType =bm.createAnnotatedType(BannerUI.class); 
		bbd.addAnnotatedType(bannerType);		
		bannerTargetFactory = bm.getInjectionTargetFactory(bannerType);
		
		bbd.addAnnotatedType(bm.createAnnotatedType(Counter.class));
		
	}
	
	public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		abd.addContext(new GlobalUIContext.CDIContext(bm));
		abd.addContext(new BannerUIContext.CDIContext(bm));
		
	}
	
	/*public static class GlobalUIScopeLiteral extends AnnotationLiteral<GlobalUIScope> implements GlobalUIScope {

		@Override
		public boolean singleton() {
			return false;
		}
	};
	
	public static class BannerUIScopeLiteral extends AnnotationLiteral<BannerUIScope> implements BannerUIScope {

		@Override
		public boolean singleton() {
			return false;
		}
	};
	
	public static class AnnotatedTypeWrapper<T> implements AnnotatedType<T>{
		
		AnnotatedType<T> wrapped;
		Class<? extends Annotation> newAnnotation;
		
		public AnnotatedTypeWrapper(AnnotatedType<T> wrapped, Class<? extends Annotation> newAnnotation){
			this.wrapped=wrapped;			
				this.newAnnotation = newAnnotation;
			
		}
		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotation) {
			if (annotation.equals(wrapped)){
				try {
					return (T)newAnnotation.newInstance();
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
			
			return wrapped.getAnnotation(annotation);
		}

		@Override
		public Set<Annotation> getAnnotations() {
			Set<Annotation> an = new HashSet<Annotation>(wrapped.getAnnotations());
			try {
				an.add(newAnnotation.newInstance());
			} catch (Exception e) {					
				e.printStackTrace();
			}
			return an;
		}

		@Override
		public Type getBaseType() {
			return wrapped.getBaseType();
		}

		@Override
		public Set<Type> getTypeClosure() {
			return wrapped.getTypeClosure();
		}

		@Override
		public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
			if (annotation.equals(newAnnotation)){
				return true;
			}
			return wrapped.isAnnotationPresent(annotation);
		}

		@Override
		public Set<AnnotatedConstructor<T>> getConstructors() {
			return wrapped.getConstructors();
		}

		@Override
		public Set<AnnotatedField<? super T>> getFields() {
			return wrapped.getFields();
		}

		@Override
		public Class<T> getJavaClass() {
			return wrapped.getJavaClass();
		}

		@Override
		public Set<AnnotatedMethod<? super T>> getMethods() {
			return wrapped.getMethods();
		}
		
	}*/
	
	
}
