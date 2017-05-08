package info.elexis.server.core.web.internal;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.exceptions.TemplateInputException;

import info.elexis.server.core.common.web.IAdminInterfaceContribution.AIContribution;
import info.elexis.server.core.common.web.IAdminInterfaceContribution.MenuCategory;
import info.elexis.server.core.security.SystemLocalAuthorizingRealm;
import info.elexis.server.core.web.ElexisServerWebConstants;

@Component(service = Servlet.class, property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN + "=" + RootServlet.SERVLET_PATTERN,
		HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=" + "("
				+ HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=" + ElexisServerWebConstants.CONTEXT_NAME
				+ ")" })
public class RootServlet extends HttpServlet {

	static final String SERVLET_PATTERN = "/*";

	private static Logger log = LoggerFactory.getLogger(RootServlet.class);

	private static final long serialVersionUID = 7886363180446490218L;
	private TemplateEngine templateEngine;

	@Override
	public void init() throws ServletException {
		super.init();
		templateEngine = TLTemplateEngine.INSTANCE.getTemplateEngine();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());

		String requestUri = req.getRequestURI();

		if ("/".equals(requestUri)) {
			requestUri = "index.html";
		}
		if (!requestUri.endsWith(".html")) {
			resp.sendRedirect("/assets" + requestUri);
			return;
		}

		try {
			if (requestUri.contains("login.html")) {
				ctx.setVariable("EsafIsInitialized", SystemLocalAuthorizingRealm.localRealmIsInitialized());
			}

			List<AIContribution> coreUiContributions = AdminInterfaceContributions
					.getAllUiContributionsforMenuCategory(MenuCategory.CORE);
			ctx.setVariable("coreContributions", coreUiContributions);

			List<AIContribution> bundleUiContributions = AdminInterfaceContributions
					.getAllUiContributionsforMenuCategory(MenuCategory.PLUGINS);
			ctx.setVariable("pluginsContributions", bundleUiContributions);

			templateEngine.process(requestUri, ctx, resp.getWriter());
		} catch (TemplateInputException tie) {
			try {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (final IOException ignored) {
				// Just ignore this
			}
			tie.printStackTrace();
		}
	}

}