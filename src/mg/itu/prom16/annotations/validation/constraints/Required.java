package mg.itu.prom16.annotations.validation.constraints;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mg.itu.prom16.annotations.validation.Constraint;
import mg.itu.prom16.annotations.validation.logic.RequiredLogic;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validator = RequiredLogic.class)
public @interface Required {
	String error() default "required";
}