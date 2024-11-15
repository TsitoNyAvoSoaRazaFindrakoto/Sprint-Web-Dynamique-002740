package mg.itu.prom16.annotations.validation.logic;

import java.lang.reflect.Field;

import mg.itu.prom16.annotations.validation.Validation;
import mg.itu.prom16.annotations.validation.Validator;
import mg.itu.prom16.annotations.validation.constraints.Max;
import mg.itu.prom16.reflect.FieldIterator;

public class MaxLogic implements Validator {

	@Override
	public void isvalid(Field f, Object arg) throws IllegalStateException {
		Validation.assertNumberValidation(f, arg);
		double max = f.getDeclaredAnnotation(Max.class).value();
		double value = ((Number) arg).doubleValue();
		if (value > max) {
			throw new IllegalStateException(FieldIterator.describe(f) + ":" + f.getDeclaredAnnotation(Max.class).error());
		}
	}
	
}
