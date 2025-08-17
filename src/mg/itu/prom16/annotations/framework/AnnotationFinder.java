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
		if (sc == null) {
			throw new ServletException("ServletContext cannot be null");
		}

		String bin_path = "WEB-INF/classes/";
		String packagePath = packagename != null ? packagename.replace(".", "/") : "";

		String realPath = sc.getRealPath(bin_path);
		if (realPath == null) {
			throw new ServletException("Unable to resolve real path for: " + bin_path);
		}

		File b = new File(realPath);
		ArrayList<Class<?>> controllerArrayList = new ArrayList<Class<?>>();

		if (!b.exists() || !b.isDirectory()) {
			System.out
					.println("Warning: default package does not exist or is not accessible. Path: " + realPath);
			return controllerArrayList;
		}

		// Check if the specific package directory exists
		File packageDir = new File(b, packagePath);
		String actualPackageName = packagename;
		if (!packageDir.exists() || !packageDir.isDirectory()) {
			System.out
					.println("Warning: package directory '" + packageDir.getPath()
							+ "' does not exist or is not accessible. Switching to default package.");
			actualPackageName = null;
		} else {
			b = packageDir;
		}

		try {
			scanDirectory(b, actualPackageName, controllerArrayList);
		} catch (Exception e) {
			throw new ServletException("Error scanning directory: " + realPath, e);
		}

		return controllerArrayList;
	}

	private static void scanDirectory(File directory, String packagename, ArrayList<Class<?>> controllerArrayList) {
		File[] files = directory.listFiles();
		if (files == null)
			return;

		for (File onefile : files) {
			if (onefile.isFile() && onefile.getName().endsWith(".class")) {
				Class<?> clazz;
				try {
					String className = onefile.getName().replace(".class", "");
					String fullClassName = packagename != null && !packagename.isEmpty()
							? packagename + "." + className
							: className;
					clazz = Class.forName(fullClassName);
					if (clazz.isAnnotationPresent(mg.itu.prom16.annotations.request.Controller.class))
						controllerArrayList.add(clazz);
				} catch (ClassNotFoundException e) {
					System.err.println("Could not load classes in package: " + packagename);
					e.printStackTrace();
				}
			} else if (onefile.isDirectory()) {
				String newPackageName = packagename != null && !packagename.isEmpty()
						? packagename + "." + onefile.getName()
						: onefile.getName();
				scanDirectory(onefile, newPackageName, controllerArrayList);
			}
		}
	}

	private static HashMap<String, HashVerb> allGetMethods(Class<?> location)
			throws ServletException, NoSuchElementException, IllegalArgumentException {
		HashMap<String, HashVerb> map = new HashMap<String, HashVerb>();
		Method[] allMethods = location.getDeclaredMethods();

		for (Method method : allMethods) {
			if (method.isAnnotationPresent(mg.itu.prom16.annotations.request.RequestMapping.class)) {
				String verb = method.getAnnotation(mg.itu.prom16.annotations.request.RequestMapping.class).method();
				String urlValue = method.getAnnotation(mg.itu.prom16.annotations.request.RequestMapping.class).path();
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
			if (method.isAnnotationPresent(mg.itu.prom16.annotations.request.RequestMapping.class)) {
				String verb = method.getAnnotation(mg.itu.prom16.annotations.request.RequestMapping.class).method();
				String urlValue = method.getAnnotation(mg.itu.prom16.annotations.request.RequestMapping.class).path();
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
