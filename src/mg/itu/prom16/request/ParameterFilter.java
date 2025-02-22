package mg.itu.prom16.request;

import java.lang.reflect.Parameter;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.prom16.annotations.models.FieldAnnotationManager;
import mg.itu.prom16.annotations.parameter.Param;
import mg.itu.prom16.embed.EmbedSession;

public class ParameterFilter {

	public static Object getFromServlet(HttpServletRequest req, Parameter param) throws Exception {
		switch (param.getType().getName()) {
			case "mg.itu.prom16.embed.EmbedSession":
				return new EmbedSession(req.getSession());
			case "jakarta.servlet.http.Part":
				return req.getPart(param.getName());
			default:
				return null;
		}
	}

	public static void transformToAttribute(HttpServletRequest req, Parameter[] m) throws Exception {
		String[][] params = ParameterFilter.findAllRequestParams(req, m);
		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				for (int j = 0; j < params[i].length; j++) {
					if (params[i][j] != null) {
						req.setAttribute(params[i][j], req.getParameter(params[i][j]));
					}
				}
			}
		}
	}

	public static String[][] findParamValues(HttpServletRequest req, Parameter[] m) throws Exception {
		String[][] params = ParameterFilter.findAllRequestParams(req, m);
		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				for (int j = 0; j < params[i].length; j++) {
					if (params[i][j] != null) {
						params[i][j] = req.getParameter(params[i][j]);
					}
				}
			}
		}
		return params;
	}

	public static String[][] findAllRequestParams(HttpServletRequest req, Parameter[] params) throws Exception {
		String[][] atrname = new String[params.length][];
		for (int i = 0; i < atrname.length; i++) {
			atrname[i] = getParametersNames(req, params[i]);
		}
		return atrname;
	}

	public static String[] getParametersNames(HttpServletRequest req, Parameter param) throws Exception {
		switch (param.getType().getPackageName()) {
			case "mg.itu.prom16.embed":
			case "jakarta.servlet.http":
				return null;
			default:
				return findParameterNames(req, param);
		}
	}

	public static String[] findParameterNames(HttpServletRequest req, Parameter m) throws Exception {
		if (m.isAnnotationPresent(Param.class)) {
			return new String[] { findAttribute(req, m) };
		}
		return FieldAnnotationManager.getFieldsName(m.getType());
	}

	// Method to find the name of the parameter of the function in the request
	public static String findAttribute(HttpServletRequest req, Parameter m) throws Exception {
		if (m.isAnnotationPresent(Param.class)) {
			String attributeName = m.getAnnotation(Param.class).name();
			String name = attributeName.isBlank() ? m.getName() : attributeName;
			return name;
		}
		throw new Exception("ETU002740 : no annotation present for " + m.getName());
	}

}
