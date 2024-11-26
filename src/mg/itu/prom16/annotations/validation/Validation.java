package mg.itu.prom16.annotations.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import mg.itu.prom16.reflect.FieldIterator;

public class Validation {

	public static void assertNumberValidation(Field f, Object arg) throws IllegalArgumentException {
		if (!(arg instanceof Number)) {
			throw new IllegalArgumentException(FieldIterator.describe(f) + " is not a number");
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

	public static void assertObject(Class<?> c, Field[] fields, Object[] args) throws Exception {
		String errorMessage = "";
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			Object arg = args[i];
			try {
				assertValidation(f, arg);
			} catch (IllegalStateException assertException) {
				errorMessage += assertException.getMessage() + "\n";
			} catch (IllegalArgumentException typeException) {
				errorMessage += typeException.getMessage() + "\n";
			}
		}
		if (!errorMessage.isEmpty()) {
			throw new IllegalArgumentException(errorMessage.trim());
		}
	}
}
