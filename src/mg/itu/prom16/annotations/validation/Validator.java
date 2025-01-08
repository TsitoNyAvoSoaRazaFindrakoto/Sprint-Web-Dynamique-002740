package mg.itu.prom16.annotations.validation;

import java.lang.reflect.Field;

public interface Validator {
	public String isvalid(Field f, Object arg) throws Exception;
}
