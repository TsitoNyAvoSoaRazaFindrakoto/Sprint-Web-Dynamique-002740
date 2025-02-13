package mg.itu.prom16.annotations.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mg.itu.prom16.annotations.validation.Constraint;
import mg.itu.prom16.annotations.validation.logic.MinLogic;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validator = MinLogic.class)
public @interface Min {
	double value() default Double.MIN_VALUE;
	String error() default "value too low";

}
