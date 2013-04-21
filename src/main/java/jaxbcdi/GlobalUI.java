package jaxbcdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.xml.bind.Unmarshaller;

@Named
@GlobalUIScope
public class GlobalUI extends jaxbcdi.xml.GlobalUI {

	@PostConstruct
	public void init() {
		System.out.format("GlobalUI init \n");
	}

	@PreDestroy
	public void destroy() {
		System.out.format("GlobalUI destroy \n");
	}

	//TODO would be nice if these were annotations
	void beforeUnmarshal(Unmarshaller u, Object parent) {
		System.out.format("GlobalUI beforeUnmarshal \n");
	}

	void afterUnmarshal(Unmarshaller u, Object parent) {
		System.out.format("GlobalUI afterUnmarshal %s %s\n", headerHeightSize, headerWidthSize, footerHeightSize, footerWidthSize, masterTemplate);
	}

}
