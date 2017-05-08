package info.elexis.server.core.web.internal;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public enum TLTemplateEngine {

	INSTANCE;

	private TemplateEngine templateEngine;

	private TLTemplateEngine() {
		templateEngine = new TemplateEngine();
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver(this.getClass().getClassLoader());
		templateResolver.setPrefix("/WEB-INF/templates/");
		// Template cache TTL=1h. If not set, entries would be cached until
		// expelled by LRU
		templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
		addTemplateResolver(templateResolver);
	}

	public void addTemplateResolver(AbstractTemplateResolver templateResolver) {
		// Cache is set to true by default. Set to false if you want templates
		// to be automatically updated when modified.
		// development only
		((AbstractConfigurableTemplateResolver) templateResolver).setCacheable(false);
		// TODO performance impact, remove on later versions
		templateResolver.setCheckExistence(true);
		templateEngine.addTemplateResolver(templateResolver);
	}


	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

}
