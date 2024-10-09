package mg.itu.prom16;

import java.io.IOException;
import java.io.PrintWriter;
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
		super.init();
		urlMapping = AnnotationFinder.urlMapping(getServletContext(), "controllerPackage");

	}

	public Mapping manageError(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (!urlMapping.containsKey(req.getServletPath())) {
			resp.sendError(404, "ETU0002740 : url not found" + req.getServletPath());
			return null;
		}
		Mapping m = urlMapping.get(req.getServletPath());

		if (m.getVerb().equalsIgnoreCase(req.getMethod())) {
			return m;
		}
		resp.sendError(500, "ETU002740 : method unothaurized");
		return null;
	}

	public void getRequestOutput(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (!urlMapping.containsKey(req.getServletPath())) {
			resp.sendError(404, "ETU0002740 : url not found" + req.getServletPath());
		}

		ModelAndView v = OutputManager.manageOuput(req, resp, manageError(req, resp));
		if (v != null) {
			for (String key : v.getAttributeNames()) {
				req.setAttribute(key, v.getAttribute(key));
			}
			String header = v.getView();
			if (header == null) {
				header = "/views/page.jsp";
			}
			req.getRequestDispatcher(header).forward(req, resp);
		}
	}

	protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		try {
			getRequestOutput(req, resp);
		} catch (Exception e) {
			display_urls(resp);
			for (StackTraceElement ste : e.getStackTrace()) {
				out.println(ste.toString());
			}
			out.println(e.getMessage());
		}
	}

	void display_urls(HttpServletResponse resp) throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("URLs : ");
		for (String url : urlMapping.keySet()) {
			out.println(url);
		}
	}

}
