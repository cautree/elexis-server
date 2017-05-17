package es.core.connector.elexis.web;

import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import info.elexis.server.core.common.web.IAdminInterfaceContribution;

@Component
public class ElexisConnectorAdminInterfaceContribution implements IAdminInterfaceContribution {

	@Override
	public List<AIContribution> getContributions() {
		AIContribution p2Contribution = new AIContribution(MenuCategory.PLUGINS, "Elexis-Connector", "connector.html");
		return Collections.singletonList(p2Contribution);
	}

}
