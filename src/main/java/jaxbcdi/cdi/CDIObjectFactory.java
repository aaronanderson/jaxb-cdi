package jaxbcdi.cdi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRegistry;

import jaxbcdi.BannerUI;
import jaxbcdi.GlobalUI;

@XmlRegistry
public class CDIObjectFactory extends jaxbcdi.xml.ObjectFactory {
	BeanManager bm;
	Listener listener;
	GlobalUIContext globalCtx;
	Map<String, BannerUIContext> bannerCtxs;
	
	

	public GlobalUIContext getGlobalCtx() {
		return globalCtx;
	}

	public void setGlobalCtx(GlobalUIContext globalCtx) {
		this.globalCtx = globalCtx;
	}

	public Map<String, BannerUIContext> getBannerCtxs() {
		return bannerCtxs;
	}

	public void setBannerCtxs(Map<String, BannerUIContext> bannerCtxs) {
		this.bannerCtxs = bannerCtxs;
	}

	public Listener getListener() {
		return listener;
	}

	public CDIObjectFactory() {
		bm = CDI.current().getBeanManager();
		listener = new Listener();
		bannerCtxs = new HashMap<String, BannerUIContext>();
	}

	@Override
	public GlobalUI createGlobalUI() {
		//CDIExtension extension = bm.getExtension(CDIExtension.class);
		//InjectionTargetFactory<GlobalUI> globalTargetFactory = extension.getGlobalTargetFactory();

		Set<Bean<?>> beans = bm.getBeans(GlobalUI.class, new AnnotationLiteral<Any>() {
		});
		if (beans.size() > 0) {
			Bean<GlobalUI> bean = (Bean<GlobalUI>) beans.iterator().next();
			CreationalContext<GlobalUI> cCtx = bm.createCreationalContext(bean);
			//listener.it = globalTargetFactory.createInjectionTarget(bean);
			//listener.instance = listener.it.produce(cCtx);
			//listener.it.inject(listener.instance, cCtx);
			//return (GlobalUI) listener.instance;
			globalCtx = new GlobalUIContext();
			globalCtx.create();
			globalCtx.begin();
			GlobalUI instance = (GlobalUI) bm.getReference(bean, GlobalUI.class, cCtx);
			globalCtx.end();

			return (GlobalUI) instance;
		} else {
			System.err.format("Can't find class %s", GlobalUI.class);
			return null;
		}
	}

	@Override
	public BannerUI createBannerUI() {
		Set<Bean<?>> beans = bm.getBeans(BannerUI.class, new AnnotationLiteral<Any>() {
		});
		if (beans.size() > 0) {
			Bean<?> bean = beans.iterator().next();
			CreationalContext<?> cCtx = bm.createCreationalContext(bean);
			listener.bannerCtx = new BannerUIContext();
			listener.bannerCtx.create();
			listener.bannerCtx.begin();
			BannerUI instance = (BannerUI) bm.getReference(bean, BannerUI.class, cCtx);
			listener.bannerCtx.end();
			return instance;
		} else {
			System.err.format("Can't find class %s", BannerUI.class);
			return null;
		}
	}

	public class Listener extends Unmarshaller.Listener {
		//explicitly invoke the postconstruct method after unmarshall instead of construction
		//InjectionTarget it;
		//Object instance;
		BannerUIContext bannerCtx;

		@Override
		public void beforeUnmarshal(Object target, Object parent) {

		}

		@Override
		public void afterUnmarshal(Object target, Object parent) {
			if (bannerCtx != null && target instanceof BannerUI) {
				bannerCtxs.put(((BannerUI) target).getDomain(), bannerCtx);
				bannerCtx = null;
			}
			/*  The postConstruct could be deferred until after marshalling but post parse logic should go in the un
			if (target.equals(instance)) {
				it.postConstruct(instance);
				it = null;
				instance = null;

			}*/
		}

	}

}
