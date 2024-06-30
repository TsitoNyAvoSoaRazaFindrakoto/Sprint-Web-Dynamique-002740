package mg.itu.prom16.outputHandler;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.prom16.reflect.ClassIterator;
import mg.itu.prom16.request.ParameterCreator;
import mg.itu.prom16.request.ParameterFilter;
import mg.itu.prom16.types.Mapping;
import mg.itu.prom16.types.ModelAndView;

public class OutputManager {
	private static Object callMethod(HttpServletRequest paramSource, Class<?> location, Method tocall)
			throws Exception {
		Object caller = ClassIterator.instance(location);

		Object[][] values = ParameterFilter.findParamValues(paramSource, tocall.getParameters());
		Object[] params = ParameterCreator.createParameters(values, tocall.getParameters());

		return tocall.invoke(caller, params);
	}

	public static Object output(HttpServletRequest paramSource, Mapping map) throws Exception {
		Class<?> methodsource = Class.forName(map.caller(null));
		Object result = OutputManager.callMethod(paramSource, methodsource, map.urlmethod(null));
		return result;
	}

	public static ModelAndView getOuput(HttpServletRequest paramSource, Mapping map) {
		ModelAndView v = new ModelAndView();
		try {
			Object result = OutputManager.output(paramSource, map);

			if (result instanceof ModelAndView) {
				v = ((ModelAndView) result);
			} else if (result instanceof String) {
				v.setPage(result.toString());
			}
		} catch (Exception e) {
			v.setPage("/views/error.jsp");
			v.setAttribute("error", e);
		}
		return v;

	}
}
