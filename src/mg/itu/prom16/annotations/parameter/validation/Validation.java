package mg.itu.prom16.annotations.parameter.validation;

import java.lang.reflect.Field;

import mg.itu.prom16.reflect.FieldIterator;
import mg.itu.prom16.reflect.TypeUtility;

public class Validation {

	public static void assertNumberValidation(Field f, Object arg) throws IllegalStateException {
		if (!f.isAnnotationPresent(Min.class)) {
			return;
		}
		if (!(arg instanceof Number)) {
			throw new IllegalArgumentException(FieldIterator.describe(f) + " is not a number");
		}
	}

	public static void assertMin(Field f, Object arg) throws IllegalStateException {
		Validation.assertNumberValidation(f, arg);
		double min = f.getDeclaredAnnotation(Min.class).value();
		double value = ((Number) arg).doubleValue();
		if (value < min) {
			throw new IllegalStateException(FieldIterator.describe(f) + ":" + f.getDeclaredAnnotation(Min.class).error());
		}
	}

	public static void assertMax(Field f, Object arg) throws IllegalStateException {
		Validation.assertNumberValidation(f, arg);
		double max = f.getDeclaredAnnotation(Max.class).value();
		double value = ((Number) arg).doubleValue();
		if (value > max) {
			throw new IllegalStateException(FieldIterator.describe(f) + ":" + f.getDeclaredAnnotation(Max.class).error());
		}
	}

	public static void assertLength(Field f, Object arg) throws IllegalStateException {
		if (!f.isAnnotationPresent(Length.class)) {
			return;
		}
		if (!(arg instanceof String)) {
			throw new IllegalArgumentException(FieldIterator.describe(f) + " is not a string");
		}
		String value = (String) arg;
		int min = (int) f.getDeclaredAnnotation(Length.class).min();
		int max = (int) f.getDeclaredAnnotation(Length.class).max();
		if (value.length() < min || value.length() > max) {
			throw new IllegalStateException(FieldIterator.describe(f) + ":" + f.getDeclaredAnnotation(Length.class).error());
		}
	}

	public static void assertRequired(Field f, Object arg) throws IllegalStateException {
		if (!f.isAnnotationPresent(Required.class)) {
			return;
		}
		if (arg == null || (arg instanceof String && ((String) arg).isEmpty())) {
			throw new IllegalStateException(
					FieldIterator.describe(f) + ":" + f.getDeclaredAnnotation(Required.class).error());
		}
	}

	public static void assertValidation(Field f, Object arg) throws IllegalStateException {
		assertRequired(f, arg);
		assertMin(f, arg);
		assertMax(f, arg);
		assertLength(f, arg);
	}

	public static void assertObject(Class<?> c, Field[] fields,Object[] args) throws IllegalStateException {
		String errorMessage = "";
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			Object arg = args[i];
			try {
				assertValidation(f, arg);
			} catch (IllegalStateException assertException) {
				errorMessage += assertException.getMessage() + "\n";
			} catch (  IllegalArgumentException typeException){
				errorMessage += typeException.getMessage() + "\n";
			}
		}
		if (!errorMessage.isEmpty()) {
			throw new IllegalArgumentException(errorMessage.trim());
		}
	}
}
