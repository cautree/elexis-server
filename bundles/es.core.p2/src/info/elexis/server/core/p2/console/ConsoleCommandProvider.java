package info.elexis.server.core.p2.console;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.component.annotations.Component;

import ch.elexis.core.status.StatusUtil;
import info.elexis.server.core.console.AbstractConsoleCommandProvider;
import info.elexis.server.core.p2.P2Util;
import info.elexis.server.core.p2.dto.RepoElement;
import info.elexis.server.core.p2.dto.RepoInfo;
import info.elexis.server.core.p2.internal.ProvisioningHelper;

@Component(service = CommandProvider.class, immediate = true)
public class ConsoleCommandProvider extends AbstractConsoleCommandProvider {

	public void _es_p2(CommandInterpreter ci) {
		executeCommand(ci);
	}

	public String __executeUpdate() {
		return ProvisioningHelper.updateAllFeatures().getMessage();
	}

	public String __features() {
		return getHelp(1);
	}

	public String __features_listLocal() {
		return ProvisioningHelper.getAllInstalledFeatures().stream().map(i -> i.getId() + " (" + i.getVersion() + ")")
				.reduce((u, t) -> u + "\n" + t).get();
	}

	public String __features_install(Iterator<String> args) {
		if (args.hasNext()) {
			return ProvisioningHelper.unInstallFeature(args.next(), true);
		}
		return missingArgument("featureName");
	}

	public String __features_uninstall(Iterator<String> args) {
		if (args.hasNext()) {
			return ProvisioningHelper.unInstallFeature(args.next(), false);
		}
		return missingArgument("featureName");
	}

	public String __repo() {
		return getHelp(1);
	}

	public void __repo_list() {
		P2Util p2Util = new P2Util();
		RepoInfo repoInfo = p2Util.getRepoInfo();
		List<RepoElement> repositories = repoInfo.getRepositories();
		for (RepoElement repoElement : repositories) {
			ci.println(repoElement);
			IStatus repositoryStatus = p2Util.getRepositoryStatus(repoElement.getId());
			String printStatus = StatusUtil.printStatus(repositoryStatus);
			ci.println("\t" + printStatus);
		}
	}

	public String __repo_add(Iterator<String> args) {
		if (args.hasNext()) {
			final String url = args.next();
			String user = null;
			String password = null;
			if (args.hasNext()) {
				user = args.next();
			}
			if (args.hasNext()) {
				password = args.next();
			}
			return new P2Util().doRepositoryAdd(url, user, password).toString();
		}
		return missingArgument("url [user] [password]");
	}

	public String __repo_remove(Iterator<String> args) {
		if (args.hasNext()) {
			final String url = args.next();
			return new P2Util().doRepositoryRemove(url).toString();
		}
		return missingArgument("url");
	}

}
