package mg.itu.prom16;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.annotations.framework.AnnotationFinder;
import mg.itu.prom16.outputHandler.OutputManager;
import mg.itu.prom16.types.mapping.HashVerb;
import mg.itu.prom16.types.returnType.ModelAndView;

public class FrontController extends HttpServlet {
	protected HashMap<String, HashVerb> urlMapping;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	public void init() throws ServletException {
		try {
			urlMapping = AnnotationFinder.urlMapping(getServletContext(), "controllerPackage");
			super.init();
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	public String getUrlMapping(HttpServletRequest request) {
		String fullURI = request.getRequestURI(); // Full URI, e.g., /rootProject/mapping/endpoint
		String contextPath = request.getContextPath(); // Context path, e.g., /rootProject
		String urlMapping = fullURI.substring(contextPath.length()); // Removes the context path
		System.out.println("URL Mapping: " + urlMapping);
		return urlMapping;
	}

	public String getReferer(HttpServletRequest request) {
		String referer = request.getHeader("Referer"); // Full Referer URL
		if (referer != null) {
			String contextPath = request.getContextPath(); // e.g., /rootProject
			String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ contextPath;

			if (referer.startsWith(baseUrl)) {
				referer =  referer.substring(baseUrl.length()); // Get only the part after /rootProject
				System.out.println("URL Mapping from Referer: " + referer);
				if (urlMapping.get(referer)!=null) {
					System.out.println("referer url mapping found");
				}
			} else {
				System.out.println("Referer is not from the same project.");
			}
		} else {
			System.out.println("No Referer header provided.");
		}
		return referer;
	}

	public HashVerb manageError(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		String url = getUrlMapping(request);

		// Output: /mapping/endpoint;
		if (!urlMapping.containsKey(url)) {
			resp.sendError(404, "ETU0002740 : url not found " + url);
			return null;
		}
		HashVerb m = urlMapping.get(url);

		if (m.containsKey(request.getMethod().toUpperCase())) {
			return m;
		}
		resp.sendError(500, "ETU002740 : method unothaurized");
		return null;
	}

	public void goToRequestOutput(HttpServletRequest request, HttpServletResponse resp) throws Exception {
		ModelAndView v = null;

		// output prend toujours l'url appelant et le referer
		v = OutputManager.manageOuput(request, resp, manageError(request, resp), urlMapping.get(getReferer(request)));
		if (v != null) {
			for (String key : v.getAttributeNames()) {
				request.setAttribute(key, v.getAttribute(key));
			}
			String header = v.getView();
			if (header == null) {
				header = "/views/page.jsp";
			}
			request.getRequestDispatcher(header).forward(request, resp);
		}
	}

	protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		try {
			goToRequestOutput(req, resp);
		} catch (Exception e) {
			// inutile si on a une page d'erreur
			display_urls(resp);
			for (StackTraceElement ste : e.getStackTrace()) {
				out.println(ste.toString());
			}
			out.println(e.getMessage());
		}
	}

	void display_urls(HttpServletResponse resp) throws IOException, ServletException {
		PrintWriter out = resp.getWriter();
		urlMapping = AnnotationFinder.urlMappingtest(getServletContext(), "controllerPackage", out);
		out.println("URLs : ");
		for (String url : urlMapping.keySet()) {
			out.println(url);
		}
	}

}
