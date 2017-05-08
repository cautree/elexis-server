package info.elexis.server.core.web.security.internal.jaxrs;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.secnod.shiro.jersey.AuthInjectionBinder;
import org.secnod.shiro.jersey.AuthorizationFilterFeature;
import org.secnod.shiro.jersey.SubjectFactory;

import com.eclipsesource.jaxrs.publisher.ApplicationConfiguration;

@Component(service = ApplicationConfiguration.class)
public class JaxRsApplicationConfiguration implements ApplicationConfiguration {

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public Set<Object> getSingletons() {
		// https://github.com/silb/shiro-jersey
		Set<Object> singletons = new HashSet<>();
		singletons.add(new AuthorizationFilterFeature());
		singletons.add(new SubjectFactory());
		singletons.add(new AuthInjectionBinder());
		return singletons;
	}

}
