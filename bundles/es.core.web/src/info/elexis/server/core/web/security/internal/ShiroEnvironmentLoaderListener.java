package info.elexis.server.core.web.security.internal;

import javax.servlet.ServletContextListener;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import info.elexis.server.core.web.ElexisServerWebConstants;

@Component(service = ServletContextListener.class, property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_LISTENER + "=true",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=" + "("
				+ HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=" + ElexisServerWebConstants.CONTEXT_NAME
				+ ")" }, immediate = true)
public class ShiroEnvironmentLoaderListener extends EnvironmentLoaderListener {
}
