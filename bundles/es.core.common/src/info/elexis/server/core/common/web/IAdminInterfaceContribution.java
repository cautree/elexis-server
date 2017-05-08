package info.elexis.server.core.common.web;

import java.util.List;

import org.thymeleaf.templateresolver.AbstractTemplateResolver;

public interface IAdminInterfaceContribution {

	public enum MenuCategory {
		CORE, PLUGINS
	};

	/**
	 * @return an {@link AbstractTemplateResolver} capable of resolving all templates
	 *         referenced within the {@link AIContribution}
	 */
	public AbstractTemplateResolver getTemplateResolver();

	/**
	 * 
	 * @return
	 */
	public List<AIContribution> getContributions();

	public static class AIContribution {
		private final MenuCategory contributionCategory;
		private final String localizedName;
		private final String templateName;

		public AIContribution(MenuCategory contributionCategory, String localizedName, String templateName) {
			this.contributionCategory = contributionCategory;
			this.localizedName = localizedName;
			this.templateName = templateName;
		}

		public MenuCategory getContributionCategory() {
			return contributionCategory;
		}

		public String getLocalizedName() {
			return localizedName;
		}

		public String getTemplateName() {
			return templateName;
		}
	}

}
