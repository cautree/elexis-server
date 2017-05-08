package info.elexis.server.core.web.security.internal;

import javax.servlet.Filter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.elexis.server.core.web.ElexisServerWebConstants;

@Component(service = Filter.class, property = { HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_PATTERN + "=/*",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_ASYNC_SUPPORTED + "=true",
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_REQUEST,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_ASYNC,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_FORWARD,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_INCLUDE,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_DISPATCHER + "=" + HttpWhiteboardConstants.DISPATCHER_ERROR,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=" + "("
				+ HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=" + ElexisServerWebConstants.CONTEXT_NAME + ")"})
public class ShiroFilter extends org.apache.shiro.web.servlet.ShiroFilter {
	
	private static Logger log = LoggerFactory.getLogger(ShiroFilter.class);
	
	// to set the declarative start order right
	@Reference(service = ShiroEnvironmentLoaderListener.class, cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
	protected synchronized void bind(ShiroEnvironmentLoaderListener realm) {
	}

	protected synchronized void unbind(ShiroEnvironmentLoaderListener realm) {
	}

}
