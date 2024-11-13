package mg.itu.prom16.annotations.parameter.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)

public @interface Min {
	double value() default Double.MIN_VALUE;
	String error() default "value too low";

}
