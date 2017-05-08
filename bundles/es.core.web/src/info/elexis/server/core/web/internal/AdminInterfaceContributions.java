package info.elexis.server.core.web.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import info.elexis.server.core.common.web.IAdminInterfaceContribution;
import info.elexis.server.core.common.web.IAdminInterfaceContribution.AIContribution;
import info.elexis.server.core.common.web.IAdminInterfaceContribution.MenuCategory;

@Component
public class AdminInterfaceContributions {

	private static List<IAdminInterfaceContribution> contributions = Collections.synchronizedList(new ArrayList<>());

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
	public synchronized void bind(IAdminInterfaceContribution contribution) {
		contributions.add(contribution);

		TLTemplateEngine.INSTANCE.addTemplateResolver(contribution.getTemplateResolver());
	}

	public synchronized void unbind(IAdminInterfaceContribution contribution) {
		contributions.remove(contribution);
	}

	public static List<IAdminInterfaceContribution> getContributions() {
		return contributions;
	}

	public static List<AIContribution> getAllUiContributions() {
		return getContributions().stream().flatMap(c -> c.getContributions().stream()).collect(Collectors.toList());
	}

	public static List<AIContribution> getAllUiContributionsforMenuCategory(MenuCategory category) {
		return getAllUiContributions().stream().filter(c -> (category == c.getContributionCategory()))
				.collect(Collectors.toList());
	}

}
