package mg.itu.prom16.annotations.parameter;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParamObject {
	public String name() default "";
}
