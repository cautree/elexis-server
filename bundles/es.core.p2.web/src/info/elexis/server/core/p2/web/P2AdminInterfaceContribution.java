package info.elexis.server.core.p2.web;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import info.elexis.server.core.common.web.IAdminInterfaceContribution;

@Component
public class P2AdminInterfaceContribution implements IAdminInterfaceContribution {

	@Override
	public AbstractTemplateResolver getTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver(this.getClass().getClassLoader());
		templateResolver.setPrefix("/WEB-INF/templates/");
		return templateResolver;
	}

	@Override
	public List<AIContribution> getContributions() {
		AIContribution p2Contribution = new AIContribution(MenuCategory.CORE, "p2", "p2.html");
		return Collections.singletonList(p2Contribution);
	}

}
