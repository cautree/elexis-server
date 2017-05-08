package info.elexis.server.core.web.internal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(service = AssetsServlet.class, property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PATTERN + "=" + AssetsServlet.RESOURCE_PATTERN,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX + "=" + AssetsServlet.RESOURCE_PREFIX })
public class AssetsServlet {
	// The pattern the resources are reachable from via the web interface
	public static final String RESOURCE_PATTERN = "/assets/*";
	// The bundle local prefix to look-up the resource files
	public static final String RESOURCE_PREFIX = "/WEB-INF/assets";

}
