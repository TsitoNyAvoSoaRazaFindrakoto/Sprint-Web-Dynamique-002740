package mg.itu.prom16.annotations.validation.logic;

import java.lang.reflect.Field;

import mg.itu.prom16.annotations.validation.Validator;
import mg.itu.prom16.annotations.validation.constraints.Required;
import mg.itu.prom16.reflect.FieldIterator;

public class RequiredLogic implements Validator {

	@Override
	public void isvalid(Field f, Object arg) throws IllegalArgumentException {
		if (arg == null || (arg instanceof String && ((String) arg).isEmpty())) {
			throw new IllegalArgumentException(
					FieldIterator.describe(f) + ":" + f.getDeclaredAnnotation(Required.class).error());
		}
	}
	
}
