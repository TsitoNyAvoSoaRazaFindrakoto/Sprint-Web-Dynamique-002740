package mg.itu.prom16.request;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.prom16.Annotations.models.FieldAnnotationManager;
import mg.itu.prom16.Annotations.parameter.ParamObject;

public class ParameterFilter {


	public static String[] getObjectParameters(HttpServletRequest req , Parameter m) throws Exception {
		ArrayList<String> params = new ArrayList<String>();
		if (!m.isAnnotationPresent(ParamObject.class)) {
			params.add(findAttribute(req, m));
			return params.toArray(new String[0]);
		}
		return FieldAnnotationManager.getAlternateFieldName(m.getType());
	}

	// Method to find the name of the parameter of the function in the request
	public static String findAttribute(HttpServletRequest req, Parameter m) throws Exception {
		if (m.isAnnotationPresent(mg.itu.prom16.Annotations.parameter.Param.class)) {
			return m.getAnnotation(mg.itu.prom16.Annotations.parameter.Param.class).name();
		}
		throw new Exception("ETU002740 : no annotation present for " + m.getName() );
	}

	public static String[][] findAllRequestParams(HttpServletRequest req, Parameter[] params) throws Exception{
		String[][] atrname = new String[params.length][];
		for (int i = 0; i < atrname.length; i++) {
			atrname[i] = ParameterFilter.getObjectParameters(req, params[i]);
		}
		return atrname;
	}

	public static String[][] findParamValues(HttpServletRequest req, Parameter[] m) throws Exception{
		String[][] params = ParameterFilter.findAllRequestParams(req, m);
		for (int i = 0; i < params.length; i++) {
			for (int j = 0; j < params[i].length; j++) {
				if (params[i][j] == null) {
					continue;
				} 
				params[i][j] = req.getParameter(params[i][j]);
			}
		}
		return params;
	}

}
