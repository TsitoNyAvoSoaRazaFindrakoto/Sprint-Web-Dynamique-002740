package mg.itu.prom16.annotations.request;

/**
 * Restapi
 */
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Restapi {
	boolean isRestapi() default true;
}