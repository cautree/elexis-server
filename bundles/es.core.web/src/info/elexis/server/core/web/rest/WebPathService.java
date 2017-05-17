package info.elexis.server.core.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;

import info.elexis.server.core.common.web.IAdminInterfaceContribution;
import info.elexis.server.core.security.SystemLocalAuthorizingRealm;
import info.elexis.server.core.web.internal.AdminInterfaceContributions;

@Component(service = WebPathService.class, immediate = true)
@Path("web")
public class WebPathService {

	@GET
	@Path("esafIsInitialized")
	public boolean serverRealmIsInitialized() {
		return SystemLocalAuthorizingRealm.localRealmIsInitialized();
	}
	
	@GET
	@Path("getAdminInterfaceContributions")
	public List<IAdminInterfaceContribution> getAdminInterfaceContributions() {
		return AdminInterfaceContributions.getContributions();
	}
	
	@POST
	@Path("setInitialPassword")
	public Response setPassword(@FormParam("password") String password,
			@FormParam("confirmPassword") String confirmPassword) {

		if (password.equals(confirmPassword)) {
			try {
				SystemLocalAuthorizingRealm.setInitialEsAdminPassword(password);
				return Response.seeOther(new URI("/login.html")).build();
			} catch (SecurityException | URISyntaxException se) {
				return Response.serverError().build();
			}
		}

		return Response.serverError().build();
	}
}
