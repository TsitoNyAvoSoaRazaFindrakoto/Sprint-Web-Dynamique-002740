package mg.itu.prom16.annotations.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Fallback {
	public String verb();
	public String method() default "GET";
	public String[] parameters() default {};
}
