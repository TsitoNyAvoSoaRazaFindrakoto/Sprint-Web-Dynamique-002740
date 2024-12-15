package mg.itu.prom16.annotations.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mg.itu.prom16.annotations.validation.Constraint;
import mg.itu.prom16.annotations.validation.logic.LengthLogic;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validator = LengthLogic.class)
public @interface Length {
	double min() default 0;
	double max() default Integer.MAX_VALUE;
	String error() default "length not valid";
}
