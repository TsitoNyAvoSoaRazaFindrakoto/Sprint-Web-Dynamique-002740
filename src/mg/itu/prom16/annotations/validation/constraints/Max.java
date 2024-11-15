package mg.itu.prom16.annotations.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mg.itu.prom16.annotations.validation.Constraint;
import mg.itu.prom16.annotations.validation.logic.MaxLogic;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validator = MaxLogic.class)
public @interface Max {
	double value() default Double.MAX_VALUE;
	String error() default "value too high";
}
