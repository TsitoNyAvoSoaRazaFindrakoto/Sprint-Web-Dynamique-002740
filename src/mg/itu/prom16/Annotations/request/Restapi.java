package mg.itu.prom16.Annotations.request;

/**
 * Restapi
 */
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Restapi {
	boolean isRestapi() default true;
}