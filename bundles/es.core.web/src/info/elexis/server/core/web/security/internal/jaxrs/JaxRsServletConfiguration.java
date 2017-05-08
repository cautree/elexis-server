package info.elexis.server.core.web.security.internal.jaxrs;

import java.io.IOException;
import java.util.Dictionary;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.web.servlet.IniShiroFilter;
import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.eclipsesource.jaxrs.publisher.ServletConfiguration;

import info.elexis.server.core.web.security.internal.ShiroFilter;

/**
 * Register the {@link ShiroFilter} with OSGI Jax RS in order to enforce our
 * security requirements
 */
@Component(service = ServletConfiguration.class)
public class JaxRsServletConfiguration implements ServletConfiguration {

	@Override
	public HttpContext getHttpContext(HttpService httpService, String rootPath) {
		Thread.currentThread().setContextClassLoader(JaxRsServletConfiguration.class.getClassLoader());
		ExtendedHttpService extHttpService = (ExtendedHttpService) httpService;
		try {
			// https://issues.apache.org/jira/browse/SHIRO-617?filter=-2
			String config = IOUtils.toString(this.getClass().getResourceAsStream("shiro-jaxrs.ini"));
			IniShiroFilter isf = new IniShiroFilter();
			isf.setConfig(config);
			// TODO fetch configured root path
			extHttpService.registerFilter("/services", isf, getInitParams(extHttpService, rootPath), null);
		} catch (ServletException | NamespaceException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Dictionary<String, String> getInitParams(HttpService httpService, String rootPath) {
		return null;
	}

}
