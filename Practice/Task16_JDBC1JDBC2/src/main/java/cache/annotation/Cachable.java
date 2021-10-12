package cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Осуществление кеширования результата
 * Параметр:
 *   true - кеширование проводится с помощью записи в файл
 *   false - кеширование проводится с помощью оперативной памяти
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cachable {
    Class<?> value();
}
