package info.elexis.server.core.p2.web.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.core.runtime.IStatus;
import org.osgi.service.component.annotations.Component;

import info.elexis.server.core.p2.P2Util;
import info.elexis.server.core.p2.dto.RepoElement;
import info.elexis.server.core.p2.dto.RepoInfo;
import info.elexis.server.core.web.WebHelper;

@Component(service = P2RestService.class, immediate = true)
@Path("/p2")
public class P2RestService {

	@GET
	@Path("/repositories")
	public RepoInfo getRepoInfo() {
		return new P2Util().getRepoInfo();
	}

	@POST
	@Path("/repositories")
	public Response addRepository(RepoElement repoElement) {
		IStatus stat = new P2Util().doRepositoryAdd(repoElement.getLocation(), repoElement.getUsername(),
				repoElement.getPassword());
		return WebHelper.createResponseFromStatus(null, stat, null);
	}

	@GET
	@Path("/repositories/{id}")
	public Response getRepositoryStatus(@PathParam("id") String id) {
		IStatus stat = new P2Util().getRepositoryStatus(id);
		return WebHelper.createResponseFromStatus(null, stat, null);
	}

	@DELETE
	@Path("/repositories/{id}")
	public Response deleteRepository(@PathParam("id") String id) {
		IStatus status = new P2Util().doRepositoryRemove(id);
		return WebHelper.createResponseFromStatus(null, status, null);
	}

	@POST
	@Path("/features")
	public Response installFeature(String featureName) {
		IStatus status = new P2Util().doFeatureInstall(featureName);
		return WebHelper.createResponseFromStatus(null, status, null);
	}

	@DELETE
	@Path("/features")
	public Response uninstallFeature(String featureName) {
		IStatus status = new P2Util().doFeatureUninstall(featureName);
		return WebHelper.createResponseFromStatus(null, status, null);
	}

	@GET
	@Path("/update")
	public Response performUpdate() {
		IStatus status = new P2Util().doUpdateAllFeatures();
		return WebHelper.createResponseFromStatus(null, status, null);
	}

}
