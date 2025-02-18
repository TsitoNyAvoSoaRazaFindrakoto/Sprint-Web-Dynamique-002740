package mg.itu.prom16.annotations.validation.logic;

import java.lang.reflect.Field;

import mg.itu.prom16.annotations.validation.Validator;
import mg.itu.prom16.annotations.validation.constraints.Required;

public class RequiredLogic implements Validator {

	@Override
	public String isvalid(Field f, Object arg) throws Exception{
		if (arg == null || (arg instanceof String && ((String) arg).isEmpty())) {
			System.out.println(f.getName() + f.getDeclaredAnnotation(Required.class).error());
			return f.getName() + f.getDeclaredAnnotation(Required.class).error();
		}
		return null;
	}

}
