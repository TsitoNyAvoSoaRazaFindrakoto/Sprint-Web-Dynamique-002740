package mg.itu.prom16.outputHandler;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mg.itu.prom16.FrontController;
import mg.itu.prom16.annotations.auth.Auth;
import mg.itu.prom16.annotations.auth.Public;
import mg.itu.prom16.annotations.request.RequestMapping;
import mg.itu.prom16.reflect.ClassIterator;
import mg.itu.prom16.request.ParameterCreator;
import mg.itu.prom16.request.ParameterFilter;
import mg.itu.prom16.types.mapping.HashVerb;
import mg.itu.prom16.types.returnType.ModelAndView;

public class OutputManager {

	private static void checkAuthorization(HttpServletRequest paramSource, Class<?> location, Method tocall) {
		HttpSession session = paramSource.getSession(false); // Don't create a new session if it doesn't exist

		if ((!location.isAnnotationPresent(Auth.class) && !tocall.isAnnotationPresent(Auth.class))
				|| tocall.isAnnotationPresent(Public.class))
			return;

		if (session == null)
			throw new IllegalAccessError("Session not found");

		String sessionRole = (String) session.getAttribute(FrontController.roleMapping);
		if (sessionRole == null)
			throw new IllegalAccessError("Role not found in session");

		boolean isIllegal = false;

		// Check the class-level @Auth annotation, if present
		if (location.isAnnotationPresent(Auth.class)) {
			String classRole = location.getAnnotation(Auth.class).role();
			if (!classRole.equalsIgnoreCase(sessionRole)) {
				isIllegal = true; // Class-level @Auth role mismatch
			}
		}

		// Check the method-level @Auth annotation, if present
		if (tocall.isAnnotationPresent(Auth.class)) {
			String methodRole = tocall.getAnnotation(Auth.class).role();
			if (!methodRole.equalsIgnoreCase(sessionRole)) {
				isIllegal = true; // Method-level @Auth role mismatch
			}
		}

		// If authorization fails, throw an error
		if (isIllegal) {
			throw new IllegalAccessError("You do not have access to the URL");
		}
	}

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
				paramSource.setAttribute("validation_" + errorKey, errors.get(errorKey));
			}
			ParameterFilter.transformToAttribute(paramSource, params);
			throw new IllegalArgumentException("constraint");
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
			HashVerb map, String verb) throws Exception {
		Class<?> methodsource = map.getDeclaringClass(verb);

		//
		checkAuthorization(paramSource, methodsource, map.get(verb));

		Object result = OutputManager.callMethod(paramSource, methodsource, map.get(verb));

		if (map.get(verb).getAnnotation(RequestMapping.class).rest()) {
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
			HashVerb map) {
		ModelAndView v = new ModelAndView();
		try {
			Object result = OutputManager.getOuptut(paramSource, contentTarget, map, paramSource.getMethod());
			if (result == null)
				return null;
			v = ((ModelAndView) result);
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException
					&& ((IllegalArgumentException) e).getMessage() == "constraint") {
				System.out.println("need to rollback");
				throw ((IllegalArgumentException) e);
			}
			System.out.println("error in method " + map.get(paramSource.getMethod()));
			if (e instanceof InvocationTargetException) {
				Throwable cause = e.getCause();
				v.setAttribute("error", cause);
				System.out.println(cause.getMessage());
				for (StackTraceElement ste : cause.getStackTrace()) {
					System.out.println(ste.toString());
				}
			} else {
				v.setAttribute("error", e);
				System.out.println(e.getMessage());
				for (StackTraceElement ste : e.getStackTrace()) {
					System.out.println(ste.toString());
				}
			}
			v.setView("views/error.jsp");

		}
		return v;

	}
}
