package info.elexis.server.core.p2.console;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.component.annotations.Component;

import info.elexis.server.core.p2.internal.HTTPServiceHelper;
import info.elexis.server.core.p2.internal.ProvisioningHelper;

@Component(service = CommandProvider.class, immediate = true)
public class ConsoleCommandProvider implements CommandProvider {

	public static final String MAIN_CMD = "es_p2";
	public static final String HELP_PREPEND = "Usage: " + MAIN_CMD + " ";

	public void _es_p2(CommandInterpreter ci) throws Exception {
		final String argument = ci.nextArgument();
		if (argument == null) {
			System.out.println(getHelp());
			return;
		}

		switch (argument.toLowerCase()) {
		case "repositories":
			System.out.println(repositories(ci));
			return;
		case "system":
			System.out.println(system(ci.nextArgument()));
			return;
		default:
			System.out.println(getHelp());
			return;
		}
	}

	private String system(String argument) {
		if (argument == null) {
			return HELP_PREPEND + "system (executeUpdate | listFeatures)";
		}

		switch (argument) {
		case "executeUpdate":
			return ProvisioningHelper.updateAllFeatures().getMessage();
		case "listFeatures":
			return ProvisioningHelper.getAllInstalledFeatures().stream()
					.map(i -> i.getId() + " (" + i.getVersion() + ")").reduce((u, t) -> u + "\n" + t).get();
		}

		return getHelp();
	}

	private String repositories(final CommandInterpreter ci) {
		final String argument = ci.nextArgument();
		if (argument == null) {
			return HELP_PREPEND + "repositories (list | add | remove )";
		}
		switch (argument) {
		case "list":
			return HTTPServiceHelper.getRepoInfo(null).toString();
		case "add":
			return repoManage(true, ci);
		case "remove":
			return repoManage(false, ci);
		default:
			break;
		}
		return getHelp();
	}

	private String repoManage(boolean b, CommandInterpreter ci) {
		final String argument = ci.nextArgument();
		if (argument == null) {
			return HELP_PREPEND + "repositories (list | add ) repo_url";
		}
		if (b) {
			return HTTPServiceHelper.doRepositoryAdd(argument).getStatusInfo().toString();
		} else {
			return HTTPServiceHelper.doRepositoryRemove(argument).getStatusInfo().toString();
		}
	}

	@Override
	public String getHelp() {
		return HELP_PREPEND + "(system | repositories)";
	}

}
