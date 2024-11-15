package mg.itu.prom16.annotations.validation.logic;

import java.lang.reflect.Field;

import mg.itu.prom16.annotations.validation.Validation;
import mg.itu.prom16.annotations.validation.Validator;
import mg.itu.prom16.annotations.validation.constraints.Min;
import mg.itu.prom16.reflect.FieldIterator;

public class MinLogic implements Validator {

	@Override
	public void isvalid(Field f, Object arg) throws IllegalStateException {
		Validation.assertNumberValidation(f, arg);
		double min = f.getDeclaredAnnotation(Min.class).value();
		double value = ((Number) arg).doubleValue();
		if (value < min) {
			throw new IllegalStateException(FieldIterator.describe(f) + ":" + f.getDeclaredAnnotation(Min.class).error());
		}
	}
	
}
