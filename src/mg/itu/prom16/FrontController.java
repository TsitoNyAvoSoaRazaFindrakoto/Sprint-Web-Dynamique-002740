package mg.itu.prom16;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.Annotations.AnnotationFinder;
import mg.itu.prom16.outputHandler.OutputManager;
import mg.itu.prom16.types.Mapping;
import mg.itu.prom16.types.ModelAndView;

public class FrontController extends HttpServlet {
    protected HashMap<String, Mapping> urlMapping;

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

    public void getRequestOutput(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!urlMapping.containsKey(req.getServletPath())) {
            resp.sendError(404, " Page not found  , url not found ");
        }
        try {
            ModelAndView v = OutputManager.getOuput(req,urlMapping.get(req.getServletPath()));

			for ( String key : v.getAttributeNames()) {
				req.setAttribute(key, v.getAttribute(key));
			}
			String header = v.getPage();
			if (header == null ) {
				header="/views/page.jsp";
			}
            req.getRequestDispatcher(header).forward(req, resp);

        } catch (Exception e) {
            resp.sendError(2, e.getMessage());
        }
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
			PrintWriter out = resp.getWriter();
        try {
			
            getRequestOutput(req, resp);
        } catch (Exception e) {
            for (StackTraceElement ste : e.getStackTrace()) {
                out.println(ste.toString());
            }
            out.println(e.getMessage());
        }
    }

}
