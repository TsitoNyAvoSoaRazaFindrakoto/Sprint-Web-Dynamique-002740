package mg.itu.prom16;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.utils.AnnotationFinder;
import mg.itu.prom16.utils.Mapping;

public class FrontController extends HttpServlet {
    protected HashMap<String,Mapping> urlMapping = new HashMap<String,Mapping>();

    public  ArrayList<Class<?>> getControllerList(String packagename) {
        String bin_path = "WEB-INF/classes/" + packagename.replace(".", "/");

        bin_path = getServletContext().getRealPath(bin_path);
        
        File b = new File(bin_path);

        ArrayList<Class<?>> controllerArrayList = new ArrayList<Class<?>>();

        for (File onefile : b.listFiles()) {
            if (onefile.isFile() && onefile.getName().endsWith(".class")) {
                Class<?> clazz;
                try {
                    clazz = Class.forName(packagename + "." + onefile.getName().split(".class")[0]);
                    if (clazz.isAnnotationPresent(mg.itu.prom16.Annotations.Controller.class))
                        controllerArrayList.add(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return controllerArrayList;
    }

    public void urlMapping(String packageName){
        ArrayList<Class<?>> clazzList = getControllerList(packageName);
        for(Class<?> clazz : clazzList){
            urlMapping.putAll(AnnotationFinder.allGetMethods(clazz));
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        urlMapping(getServletContext().getInitParameter("controllerPackage"));

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
        out.println(urlMapping.get(req.getServletPath()).urlmethod(null));
    }
}
