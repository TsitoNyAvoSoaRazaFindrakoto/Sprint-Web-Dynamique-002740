package mg.itu.prom16.annotations.validation.logic;

import java.lang.reflect.Field;

import mg.itu.prom16.annotations.validation.Validator;
import mg.itu.prom16.annotations.validation.constraints.Length;
import mg.itu.prom16.reflect.FieldIterator;

public class LengthLogic implements Validator {

	@Override
	public void isvalid(Field f, Object arg) throws IllegalArgumentException {
		if (!(arg instanceof String)) {
			throw new IllegalArgumentException(FieldIterator.describe(f) + " is not a string");
		}
		String value = (String) arg;
		int min = (int) f.getDeclaredAnnotation(Length.class).min();
		int max = (int) f.getDeclaredAnnotation(Length.class).max();
		if (value.length() < min || value.length() > max) {
			throw new IllegalArgumentException(f.getDeclaredAnnotation(Length.class).error() + " " + min + "-" + max);
		}
	}
	
}
