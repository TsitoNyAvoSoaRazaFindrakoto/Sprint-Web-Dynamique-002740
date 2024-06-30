package mg.itu.prom16.Annotations.request;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldAlternate {
	String name() default "";
	boolean exclude() default false;
}
