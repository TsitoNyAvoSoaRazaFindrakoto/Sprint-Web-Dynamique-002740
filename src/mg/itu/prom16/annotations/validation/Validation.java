package mg.itu.prom16.annotations.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mg.itu.prom16.reflect.TypeUtility;

public class Validation {

	public static Object assertNumberValidation(Field f, Object arg) throws Exception {
		if (!(arg instanceof Number)) {
			return TypeUtility.castStringToType(arg.toString(), f.getType());
		}
		return arg;
	}

	public static List<String> assertValidation(Field f, Object arg) throws Exception {
		Annotation[] annotations = f.getDeclaredAnnotations();
		List<String> errors = new ArrayList<String>();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
				Class<? extends Validator> c = annotation.annotationType().getAnnotation(Constraint.class).validator();
				Validator validator = c.getDeclaredConstructor().newInstance();
				String result = validator.isvalid(f, arg);
				if (result != null) {
					errors.add(result);
				}
			}
		}
		return errors;
	}

	public static HashMap<String, List<String>> assertObject(Class<?> c, Field[] fields, Object[] args) throws Exception {
		HashMap<String, List<String>> errors = new HashMap<>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Object argument = args[i];
			try {
				List<String> argsValidation = assertValidation(field, argument);
				if (!argsValidation.isEmpty()) {
					errors.put(field.getName(), argsValidation);
				}
			} catch (IllegalArgumentException typeException) {
				List<String> fieldErrors = errors.get(field.getName()) == null ? new ArrayList<>()
						: errors.get(field.getName());
				fieldErrors.add(typeException.getMessage());
				errors.put(field.getName(), fieldErrors);
			}
		}
		if (!errors.isEmpty()) {
			System.out.println("there were errors");
			return errors;
		}
		return null;
	}
}
