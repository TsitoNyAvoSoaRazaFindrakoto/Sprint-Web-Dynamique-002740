package mg.itu.prom16.request;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.prom16.Annotations.parameter.ParamObject;

public class AttributeFilter {


	public static String[] getObjectParameters(HttpServletRequest req , Parameter m) throws Exception {
		ArrayList<String> params = new ArrayList<String>();
		if (!m.isAnnotationPresent(ParamObject.class)) {
			params.add(findAttribute(req, m));
			return params.toArray(new String[0]);
		}
		
	}

	// Method to find the name of the parameter of the function in the request
	public static String findAttribute(HttpServletRequest req, Parameter m) throws Exception {
		if (m.isAnnotationPresent(mg.itu.prom16.Annotations.parameter.Param.class)) {
			return m.getAnnotation(mg.itu.prom16.Annotations.parameter.Param.class).name();
		}
		throw new Exception("ETU002740 : no annotation present for " + m.getName() );

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
