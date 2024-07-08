package mg.itu.prom16.request;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.prom16.Annotations.models.FieldAnnotationManager;
import mg.itu.prom16.Annotations.parameter.ParamObject;
import mg.itu.prom16.embed.EmbedSession;

public class ParameterFilter {

	public static Object getFromServlet(HttpServletRequest req, Parameter param) {
		switch (param.getType().getName()) {
			case "mg.itu.prom16.embed.EmbedSession":
				return new EmbedSession(req.getSession());
			default:
				return null;
		}
	}

	public static Object[] getParameters(HttpServletRequest req, Parameter param) throws Exception {
		switch (param.getType().getPackageName()) {
			case "mg.itu.prom16.embed":
				return null;
			default:
				return getObjectParameters(req, param);
		}
	}

	public static Object[] getObjectParameters(HttpServletRequest req, Parameter m) throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		if (!m.isAnnotationPresent(ParamObject.class)) {
			params.add(findAttribute(req, m));
			return params.toArray(new Object[0]);
		}
		return FieldAnnotationManager.getAlternateFieldName(m.getType());
	}

	// Method to find the name of the parameter of the function in the request
	public static String findAttribute(HttpServletRequest req, Parameter m) throws Exception {
		if (m.isAnnotationPresent(mg.itu.prom16.Annotations.parameter.Param.class)) {
			String annotName = m.getAnnotation(mg.itu.prom16.Annotations.parameter.Param.class).name();
			return annotName.isBlank() ? m.getName() : annotName;
		}
		throw new Exception("ETU002740 : no annotation present for " + m.getName());
	}

	public static Object[][] findAllRequestParams(HttpServletRequest req, Parameter[] params) throws Exception {
		Object[][] atrname = new Object[params.length][];
		for (int i = 0; i < atrname.length; i++) {
			atrname[i] = getParameters(req, params[i]);
		}
		return atrname;
	}

	public static Object[][] findParamValues(HttpServletRequest req, Parameter[] m) throws Exception {
		Object[][] params = ParameterFilter.findAllRequestParams(req, m);
		for (int i = 0; i < params.length; i++) {
			if (params[i] == null) {
				continue;
			}
			for (int j = 0; j < params[i].length; j++) {
				if (params[i][j] == null) {
					continue;
				}
				params[i][j] = req.getParameter(((String) params[i][j]));
			}
		}
		return params;
	}

	public static void main(String[] args) {
		EmbedSession session = new EmbedSession();
		System.out.println(session.getClass().getPackageName());
	}

}
