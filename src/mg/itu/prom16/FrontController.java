package mg.itu.prom16;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.utils.AnnotationFinder;
import mg.itu.prom16.utils.Mapping;
import mg.itu.prom16.utils.OutputManager;

public class FrontController extends HttpServlet {
    protected HashMap<String,Mapping> urlMapping ;

    

    @Override
    public void init() throws ServletException {
        super.init();
        urlMapping = AnnotationFinder.urlMapping(getServletContext(),"controllerPackage");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(req.getServletPath());
        out.println(OutputManager.returnString(urlMapping.get(req.getServletPath())));
    }
}
