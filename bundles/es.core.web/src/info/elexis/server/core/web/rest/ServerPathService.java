package info.elexis.server.core.web.rest;

import static info.elexis.server.core.constants.RestPathConstants.BASE_URL_CORE;
import static info.elexis.server.core.constants.RestPathConstants.HALT;
import static info.elexis.server.core.constants.RestPathConstants.RESTART;
import static info.elexis.server.core.constants.RestPathConstants.SCHEDULER;
import static info.elexis.server.core.constants.RestPathConstants.SCHEDULER_LAUNCH;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;

import info.elexis.server.core.Application;
import info.elexis.server.core.scheduler.SchedulerService;
import info.elexis.server.core.scheduler.SchedulerStatus;
import info.elexis.server.core.security.SystemLocalAuthorizingRealm;
import info.elexis.server.core.web.ElexisServerWebConstants;

@Component(service = RootService.class, immediate = true)
@Path(BASE_URL_CORE)
public class RootService {

	@POST
	@Path("/setInitialPassword")
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

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getStatus() {
		return Response.ok(Application.getStatus()).build();
	}

	@GET
	@Path(HALT)
	@RolesAllowed(ElexisServerWebConstants.ES_ADMIN)
	public Response haltApplication() {
		String msg = Application.shutdown(false);
		return Response.ok(msg).build();
	}

	@GET
	@Path(RESTART)
	@RolesAllowed(ElexisServerWebConstants.ES_ADMIN)
	public Response restartApplication() {
		String msg = Application.restart(false);
		return Response.ok(msg).build();
	}

	@GET
	@Path(SCHEDULER)
	@Produces(MediaType.APPLICATION_XML)
	public Response getSchedulerStatus() {
		SchedulerStatus schedulerStatus = SchedulerService.getSchedulerStatus();
		return Response.ok(schedulerStatus).build();
	}

	@GET
	@Path(SCHEDULER_LAUNCH + "/{taskId}")
	@RolesAllowed("admin")
	public Response startScheduledTask(@PathParam("taskId") String taskId) {
		boolean launched = SchedulerService.launchTask(taskId);
		if (launched) {
			return Response.ok().build();
		}
		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
