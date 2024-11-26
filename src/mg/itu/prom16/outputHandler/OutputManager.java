package mg.itu.prom16.outputHandler;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
		Object caller = ClassIterator.instance(location);
		Parameter[] params = tocall.getParameters();
		Object[][] values = ParameterFilter.findParamValues(paramSource, params);
		Object[] paramValues = ParameterCreator.createParameters(values, params);

		// prendre la session , cookies , contexte , fichier,...
		for (int i = 0; i < paramValues.length; i++) {
			if (paramValues[i] == null) {
				paramValues[i] = ParameterFilter.getFromServlet(paramSource, params[i]);
			}
		}

		return tocall.invoke(caller, paramValues);
	}

	private ModelAndView rollBack(HttpServletRequest paramSource,
			HashVerb map) {
		ModelAndView v = new ModelAndView();
		String verb = paramSource.getMethod();
		Class<?> methodsource = map.getDeclaringClass(verb);
		Object result = OutputManager.callMethod(paramSource, methodsource, map.get(verb));

		return v;
	}

	// responsible for parsing the output
	public static ModelAndView manageOuput(HttpServletRequest paramSource, HttpServletResponse contentTarget,
			HashVerb map) {
		ModelAndView v = new ModelAndView();
		try {
			String verb = paramSource.getMethod();
			Class<?> methodsource = map.getDeclaringClass(verb);
			Object result = null;
			try {
				result = OutputManager.callMethod(paramSource, methodsource, map.get(verb));
			} catch (Exception e) {
				// TODO: handle exception
			}

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
			v = ((ModelAndView) result);

		} catch (Exception e) {
			v.setView("/views/error.jsp");
			v.setAttribute("error", e);
		}
		return v;

	}
}
