package mg.itu.prom16.annotations.framework;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

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


	private static HashMap<String, HashVerb> allGetMethods(Class<?> location)
			throws ServletException, NoSuchElementException, IllegalArgumentException {
		HashMap<String, HashVerb> map = new HashMap<String, HashVerb>();
		Method[] allMethods = location.getDeclaredMethods();

		for (Method method : allMethods) {
			if (method.isAnnotationPresent(mg.itu.prom16.annotations.request.URLMap.class)) {
				String verb = method.getAnnotation(mg.itu.prom16.annotations.request.URLMap.class).method();
				String urlValue = method.getAnnotation(mg.itu.prom16.annotations.request.URLMap.class).path();
				HashVerb verbHash = map.containsKey(urlValue) ? map.get(urlValue) : new HashVerb();
				verbHash.put(verb, method);
				map.put(urlValue, verbHash);
			}
		}
		return map;
	}

	public static HashMap<String, HashVerb> urlMapping(ServletContext sc, String initparameter)
			throws ServletException, IllegalArgumentException, NoSuchElementException {
		ArrayList<Class<?>> clazzList = AnnotationFinder.getControllerList(sc, sc.getInitParameter(initparameter));
		HashMap<String, HashVerb> urlMap = new HashMap<String, HashVerb>();
		for (Class<?> clazz : clazzList) {
			HashMap<String, HashVerb> clazzMap = AnnotationFinder.allGetMethods(clazz);
			for (String urlkey : clazzMap.keySet()) {
				HashVerb v = clazzMap.get(urlkey);
				if (urlMap.containsKey(urlkey)) {
					HashVerb verbMethod = urlMap.get(urlkey);
					verbMethod.putAll(v);
					urlMap.put(urlkey, verbMethod);
				} else {
					urlMap.put(urlkey, v);
				}
			}
		}
		return urlMap;
	}

	private static HashMap<String, HashVerb> allGetMethodstest(Class<?> location, PrintWriter out)
			throws ServletException, NoSuchElementException, IllegalArgumentException {
		HashMap<String, HashVerb> map = new HashMap<String, HashVerb>();
		Method[] allMethods = location.getDeclaredMethods();

		for (Method method : allMethods) {
			if (method.isAnnotationPresent(mg.itu.prom16.annotations.request.URLMap.class)) {
				String verb = method.getAnnotation(mg.itu.prom16.annotations.request.URLMap.class).method();
				String urlValue = method.getAnnotation(mg.itu.prom16.annotations.request.URLMap.class).path();
				HashVerb verbHash = map.containsKey(urlValue) ? map.get(urlValue) : new HashVerb();
				verbHash.put(verb, method);
				map.put(urlValue, verbHash);
			}
		}

		for (String urlkey : map.keySet()) {
			HashVerb urlMap = map.get(urlkey);
			for (String verb : urlMap.keySet()) {
				out.println(urlkey + " " + verb + " " + urlMap.get(verb).getName());
			}
		}
		return map;
	}

	public static HashMap<String, HashVerb> urlMappingtest(ServletContext sc, String initparameter, PrintWriter out)
			throws ServletException, IllegalArgumentException, NoSuchElementException {
		ArrayList<Class<?>> clazzList = AnnotationFinder.getControllerList(sc, sc.getInitParameter(initparameter));
		HashMap<String, HashVerb> urlMap = new HashMap<String, HashVerb>();
		for (Class<?> clazz : clazzList) {
			HashMap<String, HashVerb> clazzMap = AnnotationFinder.allGetMethodstest(clazz, out);
			for (String urlkey : clazzMap.keySet()) {
				HashVerb v = clazzMap.get(urlkey);
				if (urlMap.containsKey(urlkey)) {
					HashVerb verbMethod = urlMap.get(urlkey);
					verbMethod.putAll(v);
					urlMap.put(urlkey, verbMethod);
				} else {
					urlMap.put(urlkey, v);
				}
			}
		}
		return urlMap;
	}

}
