package mg.itu.prom16.outputHandler;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.prom16.annotations.request.Restapi;
import mg.itu.prom16.reflect.ClassIterator;
import mg.itu.prom16.request.ParameterCreator;
import mg.itu.prom16.request.ParameterFilter;
import mg.itu.prom16.types.mapping.HashVerb;
import mg.itu.prom16.types.returnType.ModelAndView;

public class OutputManager {

	private static Object callMethod(HttpServletRequest paramSource, Class<?> location, Method tocall)
			throws Exception {
		HashMap<String, List<String>> errors = new HashMap<>();

		Object caller = ClassIterator.instance(location);
		Parameter[] params = tocall.getParameters();
		Object[][] values = ParameterFilter.findParamValues(paramSource, params);
		Object[] paramValues = ParameterCreator.createParameters(values, params, errors);

		// validation echouee
		if (!errors.isEmpty()) {
			System.out.println("there are errors");
			for (String errorKey : errors.keySet()) {
				paramSource.setAttribute(errorKey, errors.get(errorKey));
			}
			ParameterFilter.transformToAttribute(paramSource, params);
			return new IllegalArgumentException("constraint");
		}

		// prendre la session , cookies , contexte , fichier,...
		for (int i = 0; i < paramValues.length; i++) {
			if (paramValues[i] == null) {
				paramValues[i] = ParameterFilter.getFromServlet(paramSource, params[i]);
			}
		}

		return tocall.invoke(caller, paramValues);
	}

	// reourne l'ouput
	public static Object getOuptut(HttpServletRequest paramSource, HttpServletResponse contentTarget,
			HashVerb map,String verb) throws Exception {
		Class<?> methodsource = map.getDeclaringClass(verb);
		Object result = OutputManager.callMethod(paramSource, methodsource, map.get(verb));
		
		if (map.get(verb).isAnnotationPresent(Restapi.class)) {
			Gson jsoner = new Gson();
			contentTarget.setContentType("text/json");
			PrintWriter out = contentTarget.getWriter();

			if ((result instanceof ModelAndView)) {
				out.print(jsoner.toJson(((ModelAndView) result).getAttributes()));
			} else {
				out.print(jsoner.toJson(result));
			}
			return null;
		}
		return result;
	}

	// responsible for parsing the output
	public static ModelAndView manageOuput(HttpServletRequest paramSource, HttpServletResponse contentTarget,
			HashVerb map, HashVerb oldMap) {
		ModelAndView v = new ModelAndView();
		try {
			Object result = OutputManager.getOuptut(paramSource, contentTarget, map,paramSource.getMethod());
			if (oldMap != null && result instanceof IllegalArgumentException
					&& ((IllegalArgumentException) result).getMessage() == "constraint") {
				System.out.println("rollback");
				result = getOuptut(paramSource, contentTarget, oldMap,"GET");
			}

			if (result == null) return null;
			v = ((ModelAndView) result);

		} catch (Exception e) {
			v.setView("/views/error.jsp");
			v.setAttribute("error", e);
		}
		return v;

	}
}
