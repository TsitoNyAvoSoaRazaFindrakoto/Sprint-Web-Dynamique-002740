package mg.itu.prom16.annotations.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Validation {

	public static void assertNumberValidation(Field f, Object arg) throws IllegalArgumentException {
		if (!(arg instanceof Number)) {
			throw new IllegalArgumentException(f.getName() + " is not a number");
		}
	}

	public static void assertValidation(Field f, Object arg) throws Exception {
		Annotation[] annotations = f.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
				Class<? extends Validator> c = annotation.annotationType().getAnnotation(Constraint.class).validator();
					Validator validator = c.getDeclaredConstructor().newInstance();
					validator.isvalid(f, arg);
			}
		}
	}

	public static HashMap<String,List<String>> assertObject(Class<?> c, Field[] fields, Object[] args) throws Exception {
		HashMap<String,List<String>> errors = new HashMap<>();
		for (int i = 0; i < fields.length; i++) {
			Field  f   = fields[i];
			Object arg = args[i];
			try {
				assertValidation(f, arg);
			} catch (IllegalArgumentException typeException) {
				List<String> fieldErrors = errors.get("error_"+f.getName()) == null ? new ArrayList<>() :  errors.get(f.getName());
				fieldErrors.add(typeException.getMessage());
				errors.put(f.getName(), fieldErrors);
			}
		}
		if (!errors.isEmpty()) {
			return errors;
		}
		return null;
	}
}
