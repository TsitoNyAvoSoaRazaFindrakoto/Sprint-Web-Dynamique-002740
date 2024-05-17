package mg.itu.prom16;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.Annotations.Controller;

public class FrontController extends HttpServlet {
    protected ArrayList<Class<?>> controllerArrayList = new ArrayList<Class<?>>();

    public void getControllerList(String packagename) {
        String bin_path = "WEB-INF/classes/" + packagename.replace(".", "/");

        bin_path = getServletContext().getRealPath(bin_path);

        File b = new File(bin_path);
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
    }


    @Override
    public void init() throws ServletException {
        super.init();
        getControllerList(getServletContext().getInitParameter("controllerPackage"));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {

            for (Class<?> contr : controllerArrayList) {
                out.println(contr.getName());
            }
        } catch (Exception e) {
            out.println(e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                out.println(ste);
            }
        }
    }
}
