package mg.itu.prom16.annotations.validation.logic;

import java.lang.reflect.Field;

import mg.itu.prom16.annotations.validation.Validation;
import mg.itu.prom16.annotations.validation.Validator;
import mg.itu.prom16.annotations.validation.constraints.Min;

public class MinLogic implements Validator {

	@Override
	public String isvalid(Field f, Object arg) throws Exception {
		arg = Validation.assertNumberValidation(f, arg);
		double min = f.getDeclaredAnnotation(Min.class).value();
		double value = ((Number) arg).doubleValue();
		if (value < min) {
			System.out.println(f.getName() + f.getDeclaredAnnotation(Min.class).error() + " " + min);
			return f.getName() + f.getDeclaredAnnotation(Min.class).error() + " " + min;
		}
		return null;
	}
	
}
