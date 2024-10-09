package mg.itu.prom16.annotations.framework;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import mg.itu.prom16.types.mapping.HashVerb;

public class AnnotationFinder {

	private static ArrayList<Class<?>> getControllerList(ServletContext sc, String packagename) throws ServletException {
		String bin_path = "WEB-INF/classes/" + packagename.replace(".", "/");

		bin_path = sc.getRealPath(bin_path);

		File b = new File(bin_path);

		if (!b.exists()) {
			throw new ServletException(" package '" + packagename + "' does not exist ");
		}

		ArrayList<Class<?>> controllerArrayList = new ArrayList<Class<?>>();

		for (File onefile : b.listFiles()) {
			if (onefile.isFile() && onefile.getName().endsWith(".class")) {
				Class<?> clazz;
				try {
					clazz = Class.forName(packagename + "." + onefile.getName().split(".class")[0]);
					if (clazz.isAnnotationPresent(mg.itu.prom16.annotations.request.Controller.class))
						controllerArrayList.add(clazz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		// if (controllerArrayList.isEmpty()) {
		// throw new ServletException("Package vide");
		// }
		return controllerArrayList;
	}

	private static HashMap<String, HashVerb> allGetMethods(Class<?> location) throws ServletException{
        HashMap<String,HashVerb> map = new HashMap<String,HashVerb>();
        Method[] allMethods = location.getDeclaredMethods();

        for (Method method : allMethods) {
            if(method.isAnnotationPresent(mg.itu.prom16.annotations.request.URLMap.class)){
                String urlValue = method.getAnnotation(mg.itu.prom16.annotations.request.URLMap.class).path();
                if(map.containsKey(urlValue)){
                  if (map.get(urlValue).containsKey(allMethods)) {
										throw new ServletException(urlValue + " already exists");
									}
									map.
                }
            }
        }

        return map;  
    }

	public static HashMap<String, HashVerb> urlMapping(ServletContext sc, String initparameter)
			throws ServletException, IllegalArgumentException {
		ArrayList<Class<?>> clazzList = AnnotationFinder.getControllerList(sc, sc.getInitParameter(initparameter));
		HashMap<String, HashVerb> urlMap = new HashMap<String, HashVerb>();
		for (Class<?> clazz : clazzList) {
			urlMap.putAll(AnnotationFinder.allGetMethods(clazz));
		}
		return urlMap;
	}

}
