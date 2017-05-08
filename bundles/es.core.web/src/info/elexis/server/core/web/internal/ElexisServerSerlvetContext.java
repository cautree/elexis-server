package info.elexis.server.core.web.internal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import info.elexis.server.core.web.ElexisServerWebConstants;

@Component(service = ServletContextHelper.class, property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=" + ElexisServerWebConstants.CONTEXT_NAME,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH + "=" + ElexisServerWebConstants.CONTEXT_PATH })
public class ElexisServerSerlvetContext extends ServletContextHelper {
}
