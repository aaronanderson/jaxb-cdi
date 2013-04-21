package jaxbcdi;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.xml.bind.Unmarshaller;

@Named
@BannerUIScope
public class Counter {
	
	AtomicLong counter;
	
	@PostConstruct
	public void init() {
		System.out.format("Counter init \n");
		counter = new AtomicLong();
	}

	@PreDestroy
	public void destroy() {
		System.out.format("Counter destroy \n");
		counter = null;
	}
	
	
	public long getIncrement(){
		return counter.incrementAndGet();
	}

}
