package mg.itu.prom16.request;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import jakarta.servlet.http.HttpServletRequest;

public class AttributeFilter {
	public static String findAttribute(HttpServletRequest req, Parameter m) {
		if (req.getAttribute(m.getName()) != null) {
			return m.getName();
		}

		String paramAtServlet = null;
		if (m.isAnnotationPresent(mg.itu.prom16.Annotations.Param.class)) {
			paramAtServlet = m.getAnnotation(mg.itu.prom16.Annotations.Param.class).name();
		}

		return paramAtServlet;
	}

	public static String[] findAllAttributes(HttpServletRequest req, Parameter[] params) {
		String[] atrname = new String[params.length];

		for (int i = 0; i < atrname.length; i++) {
			atrname[i] = AttributeFilter.findAttribute(req, params[i]);
		}
		return atrname;
	}

	public static Object[] findParamValues(HttpServletRequest req, Method m) {
		String[] atrnames = AttributeFilter.findAllAttributes(req, m.getParameters());
		Object[] params = new Object[atrnames.length];
		for (int i = 0; i < params.length; i++) {
			if (atrnames[i] == null) {
				params[i] = null;
			}
			params[i] = req.getAttribute(atrnames[i]);
		}
		return params;
	}
}
