package mg.itu.prom16.annotations.validation;

import java.lang.reflect.Field;

public interface Validator {
	public void isvalid(Field f, Object arg) throws IllegalStateException;
}
