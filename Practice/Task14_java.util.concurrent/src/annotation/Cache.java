package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Кеширование результата выполнения метода
 * Параметры:
 * cacheType() - тип памяти, перечисление Cache {IN_MEMORY, FILE};
 * maxItemsAmountInList() - максимальное количество элементов в списке, если возвращаемый тип это List;
 * zip() - true - файл надо сжимать в zip архив, faulse - не надо сжимать;
 * fileNamePrefix() - название файла/ключа по которому будем храниться значение,
 * по умолчанию (при пустом значении "") используется имя метода;
 * identityBy() - указывает, какие аргументы метода учитывать при определении уникальности результата.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {
    CacheType cacheType() default CacheType.FILE;
    int maxItemsAmountInList() default -1;
    boolean zip() default false;
    String fileNamePrefix() default "";
    Class<?>[] identityBy() default {};
}
