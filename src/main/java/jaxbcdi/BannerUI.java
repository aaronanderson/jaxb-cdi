package jaxbcdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.xml.bind.Unmarshaller;

@Named
@BannerUIScope
public class BannerUI extends jaxbcdi.xml.BannerUI {

	@PostConstruct
	public void init() {
		System.out.format("BannerUI init \n");
	}

	@PreDestroy
	public void destroy() {
		System.out.format("BannerUI destroy \n");
	}
	
	
	//TODO would be nice if these were annotations. These methods are not called using an objectfactory
    void beforeUnmarshal(Unmarshaller u, Object parent){
    	System.out.format("BannerUI beforeUnmarshal \n");
    }
    
   void afterUnmarshal(Unmarshaller u, Object parent){
    	System.out.format("BannerUI afterUnmarshal %s %s %s %s\n", name, domain, slogan, theme);
    }

}
