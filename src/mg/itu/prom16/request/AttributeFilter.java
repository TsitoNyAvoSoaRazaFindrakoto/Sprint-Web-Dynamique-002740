package mg.itu.prom16.request;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import jakarta.servlet.http.HttpServletRequest;

public class AttributeFilter {
	public static String findAttribute(HttpServletRequest req, Parameter m) throws Exception {
		if (req.getParameter(m.getName()) != null) {
			return m.getName();
		} else if (m.isAnnotationPresent(mg.itu.prom16.Annotations.Param.class)) {
			return m.getAnnotation(mg.itu.prom16.Annotations.Param.class).name();
		}
		throw new Exception("param not found :" + m.getName());

	}

	public static String[] findAllAttributes(HttpServletRequest req, Parameter[] params) throws Exception{
		String[] atrname = new String[params.length];

		for (int i = 0; i < atrname.length; i++) {
			atrname[i] = AttributeFilter.findAttribute(req, params[i]);
		}
		return atrname;
	}

	public static Object[] findParamValues(HttpServletRequest req, Method m) throws Exception{
		String[] atrnames = AttributeFilter.findAllAttributes(req, m.getParameters());
		Object[] params = new Object[atrnames.length];
		for (int i = 0; i < params.length; i++) {
			if (atrnames[i] == null) {
				params[i] = m.getParameters()[i].getName() + " is param ";
			} else params[i] = req.getParameter(atrnames[i]);
		}
		return params;
	}
}
