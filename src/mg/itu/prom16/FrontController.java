package mg.itu.prom16;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.annotations.framework.AnnotationFinder;
import mg.itu.prom16.annotations.validation.Fallback;
import mg.itu.prom16.exception.ValidationException;
import mg.itu.prom16.outputHandler.OutputManager;
import mg.itu.prom16.types.mapping.HashVerb;
import mg.itu.prom16.types.returnType.ModelAndView;

public class FrontController extends HttpServlet {
	public static String roleMapping;
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
			roleMapping = getServletContext().getInitParameter("roleMapping");
			super.init();
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	public String getUrlMapping(HttpServletRequest request) {
		String fullURI = request.getRequestURI(); // Full URI, e.g., /rootProject/mapping/endpoint
		String contextPath = request.getContextPath(); // Context path, e.g., /rootProject
		String urlMapping = fullURI.replaceFirst(request.getServerName() + ":" + request.getServerPort() + contextPath, "");
		System.out.println("URL Mapping: " + urlMapping);
		return urlMapping;
	}

	public String getRefererPath(HttpServletRequest request) {
		String referer = request.getHeader("Referer"); // Full Referer URL
		if (referer != null) {
			String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

			if (referer.startsWith(baseUrl)) {
				referer = referer.substring(baseUrl.length()); // Get only the part after /rootProject
				System.out.println("URL Mapping from Referer: " + referer);
				if (urlMapping.get(referer) != null) {
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
		HashVerb urlMethod = manageError(request, resp);
		try {
			v = OutputManager.manageOuput(request, resp, urlMethod);
			if (v != null) {
				for (String key : v.getAttributeNames()) {
					request.setAttribute(key, v.getAttribute(key));
				}
				forwardRequest(v, request, resp);
			}
		} catch (ValidationException e) {
			forwarToFallBack(urlMethod, request, resp);
		}
	}

	public void forwarToFallBack(HashVerb urlMethod, HttpServletRequest request, HttpServletResponse resp)
			throws Exception {
		Fallback annot = urlMethod.get(request.getMethod().toUpperCase()).getAnnotation(Fallback.class);
		HttpServletRequestWrapper newRequest = new HttpServletRequestWrapper(request) {
			public String getMethod() {
				return annot.method();
			}
		};
		String verb = annot.verb();
		for (int i = 0 ; i<annot.parameters().length ;i++) {
			verb += annot.parameters()[i] + "=" + request.getAttribute(annot.parameters()[i]);
		}
		request.getRequestDispatcher(verb).forward(newRequest, resp);
	}

	public void forwardRequest(ModelAndView v, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (v.getView() == null || v.getView().isBlank()) {
			v.setView("/index.jsp");
		}

		if (v.getView().toLowerCase().endsWith(".jsp")) {
			request.getRequestDispatcher(v.getView()).forward(request, response);
			return;
		}
		if (v.redirect) {
			response.sendRedirect(v.getView());
			return;
		}
		final String method = v.getMethod(); // Get method only once
		HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
			@Override
			public String getMethod() {
				return method;
			}
		};
		wrappedRequest.getRequestDispatcher(v.getView()).forward(wrappedRequest, response);

	}

	protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		try {
			goToRequestOutput(req, resp);
		} catch (Exception e) {
			display_urls(resp);
			for (StackTraceElement ste : e.getStackTrace()) {
				out.println("<p style='color:red;'>" + ste.toString() + "</p>");
				System.out.println(ste.toString());
			}
			out.println("<p style='font-weight: bold;'>" + e.getLocalizedMessage() + "</p>");
			System.out.println(e.getLocalizedMessage());
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
