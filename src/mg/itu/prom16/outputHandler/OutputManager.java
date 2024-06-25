package mg.itu.prom16.outputHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import mg.itu.prom16.request.AttributeFilter;
import mg.itu.prom16.types.Mapping;
import mg.itu.prom16.types.ModelAndView;

public class OutputManager {
	private static Object getOneInstance(Class<?> toinstance) {
		try {
			return toinstance.getConstructor(new Class<?>[0]).newInstance(new Object[0]);
		} catch (Exception e) {
			return e;
		}
	}

	private static Object callMethod(HttpServletRequest paramSource, Class<?> location, Method tocall) {
		Object callerObject = OutputManager.getOneInstance(location);
		if (callerObject instanceof Exception) {
			return callerObject;
		}
		try {
			return tocall.invoke(callerObject, AttributeFilter.findParamValues(paramSource, tocall));
		} catch (Exception e) {
			return e;
		}
	}

	public static Object output(HttpServletRequest paramSource , Mapping map) {
		Class<?> methodsource;
		try {
			methodsource = Class.forName(map.caller(null));
		} catch (ClassNotFoundException e) {
			return e;
		}
		Object result = OutputManager.callMethod(paramSource ,methodsource, map.urlmethod(null));
		
		return result;
	}

	public static ModelAndView getOuput(HttpServletRequest paramSource , Mapping map){
		Object result = OutputManager.output(paramSource ,map);
		ModelAndView v = new ModelAndView();

		if (result instanceof ModelAndView) {
			v = ((ModelAndView) result);
		} else if (result instanceof Exception) {
			v.setPage("/views/error.jsp");
			v.setAttribute("error", result);
		} else if (result instanceof String) {
			v.setPage(result.toString());
		}
		return v;
	}
}
