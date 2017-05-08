package info.elexis.server.core.web;

import javax.ws.rs.core.Response;

import org.eclipse.core.runtime.IStatus;
import org.slf4j.Logger;

import ch.elexis.core.status.ObjectStatus;

public class WebHelper {

	public static Response createResponseFromStatus(String op, IStatus stat, Logger log) {
		if (stat.isOK()) {
			if (stat instanceof ObjectStatus) {
				ObjectStatus os = (ObjectStatus) stat;
				return Response.ok(os.getObject()).build();
			}

			return Response.ok().build();
		}
		if (log != null) {
			log.warn("Error performing operation [{}] : Code {} / {}", op, stat.getCode(), stat.getMessage());
		}

		return Response.serverError().build();
	}
}
