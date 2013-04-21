package jaxbcdi;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import jaxbcdi.cdi.BannerUIContext;
import jaxbcdi.cdi.CDIObjectFactory;
import jaxbcdi.cdi.GlobalUIContext;

@WebFilter("/*")
public class UIFilter implements Filter {
	GlobalUIContext globalCtx;
	Map<String, BannerUIContext> bannerCtxs;

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		try {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new Source[] { new StreamSource(filterConfig.getServletContext().getResourceAsStream("/WEB-INF/xsd/ui-config.xsd")) });
			JAXBContext ctx = JAXBContext.newInstance("jaxbcdi.xml");
			Unmarshaller um = ctx.createUnmarshaller();
			um.setSchema(schema);
			CDIObjectFactory factory = new CDIObjectFactory();
			um.setProperty("com.sun.xml.internal.bind.ObjectFactory", new Object[] { factory });
			um.setListener(factory.getListener());
			um.unmarshal(filterConfig.getServletContext().getResourceAsStream("/WEB-INF/ui-config.xml"));
			globalCtx = factory.getGlobalCtx();
			bannerCtxs = factory.getBannerCtxs();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {
		globalCtx.destroy();
		for (BannerUIContext ctx : bannerCtxs.values()) {
			ctx.destroy();
		}

	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

		//instead of using the request servername to determine the banner a request parameter will be used instead
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		String domain = "companya.com";
		if ("b".equals(req.getParameter("c"))) {
			domain = "companyb.com";
		}
		BannerUIContext bctx = bannerCtxs.get(domain);

		globalCtx.begin();
		try {
			bctx.begin();
			try {
				filterChain.doFilter(servletRequest, servletResponse);
			} finally {
				bctx.end();
			}
		} finally {
			globalCtx.end();
		}
	}

}
