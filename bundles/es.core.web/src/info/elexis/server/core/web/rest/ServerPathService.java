package info.elexis.server.core.web.rest;

import static info.elexis.server.core.constants.RestPathConstants.HALT;
import static info.elexis.server.core.constants.RestPathConstants.RESTART;
import static info.elexis.server.core.constants.RestPathConstants.SCHEDULER;
import static info.elexis.server.core.constants.RestPathConstants.SCHEDULER_LAUNCH;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;

import info.elexis.server.core.Application;
import info.elexis.server.core.scheduler.SchedulerService;
import info.elexis.server.core.scheduler.SchedulerStatus;
import info.elexis.server.core.web.ElexisServerWebConstants;

@Component(service = ServerPathService.class, immediate = true)
@Path("server")
public class ServerPathService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("status")
	public String getStatus() {
		return Application.getStatus();
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
