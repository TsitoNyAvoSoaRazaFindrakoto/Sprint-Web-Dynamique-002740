package mg.itu.prom16.Annotations.parameter;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParamObject {
	public String name();
}
