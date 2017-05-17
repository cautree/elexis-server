package info.elexis.server.core.web.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.component.annotations.Component;

@Component(service = UserService.class, immediate = true)
@Path("user")
public class UserService {

	@POST
	@Path("loginJWT")
	public String serverRealmIsInitialized() {
		return "";
	}

	@GET
	@Path("logoff")
	public void getAdminInterfaceContributions() {

	}

	@GET
	@Path("whoami")
	public String whoami() {
		String principal = (String) SecurityUtils.getSubject().getPrincipal();
		return (principal != null) ? principal : "anonymous";
	}
}
