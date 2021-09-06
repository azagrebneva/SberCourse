package reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Тема 5. Аннотации, рефлексия, прокси
 * 5.2 Вывести на консоль все методы класса, включая все родительские методы
 *      (включая приватные).
 * 5.3 Вывести все геттеры класса.
 * 5.4 Проверить что все String константы имеют значение = их имени
 *      public static final String MONDAY = "MONDAY".
 * 5.7 Реализовать класс BeanUtils по документации
 */

public class MainReflection {

    /**
     * Возвращает название модификатора доступа
     * @param modifiers модификаторы
     * @return название модификатора доступа
     */
    private static String getAccessModifier(int modifiers){
        if (Modifier.isPublic(modifiers)) return "public";
        if (Modifier.isPrivate(modifiers)) return "private";
        if (Modifier.isProtected(modifiers)) return "protected";
        return ""; // default
    }

    public static String methodToString(Method method){
        if (method == null) {
            return "";
        }

        int modifiers = method.getModifiers();
        String strModifiers = getAccessModifier(modifiers);
        Class[] parameterTypes = method.getParameterTypes();
        Class returnType = method.getReturnType();

        String parameters = "";
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters += ((!"".equals(parameters)) ? ", " : "") +
                    parameterTypes[i].getSimpleName() + " p" + (i + 1);
        }

        String methodName = strModifiers + " " + returnType.getSimpleName()
                + " " + method.getName() + "(" + parameters + ")";

        return methodName.trim();
    }

    /**
     * Возвращает true, если метод является геттером, в противном случае - false.
     * @param method метод
     * @return true, если метод является истинным; false - в противном случае.
     */
    public static boolean isGetter(Method method){
        if (!method.getName().startsWith("get")) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (void.class.equals(method.getReturnType())) {
            return false;
        }
        return true;
    }

    /**
     * Задание 5.2 Выводит на консоль все методы класса, включая все
     * родительские методы (включая приватные)
     * @param object объект класса, все методы которого выводятся
     */
    private static void showAllMethods(Object object){
        int methodCount = 0;
        Class currentClass = object.getClass();
        while (currentClass!= null) {
            System.out.println("----------"+ currentClass.getSimpleName() +"-------------------");
            Method[] declaredMethods = currentClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                System.out.println(++methodCount + " " + methodToString(method));
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    /**
     * Задание 5.3 Выводит на консоль геттеры класса
     * @param object объект класса, все методы которого выводятся
     */
    private static void showAllGetters(Object object) {
        int methodCount = 0;
        Method[] publicMethods = object.getClass().getMethods();
        for (Method method : publicMethods) {
            if (isGetter(method)) {
                System.out.println(++methodCount + " " + methodToString(method));
            }
        }
    }

    /**
     * Задание 5.4 Возвращает true, если все String константы указанного класса.
     * имеют значение = их имени, например, public static final String MONDAY = "MONDAY".
     * @param object объект класса, все методы которого выводятся.
     * @return true названия всех String констант равны их значениям, false в противном случае.
     * @throws IllegalAccessException выбрасывается при методе рефлексии get.
     */

    private static boolean isAllStringConstantNamesEqualToValues(Object object) throws IllegalAccessException {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field: declaredFields) {
            // String type
            String fieldType = field.getType().getSimpleName();
            if (fieldType.equals("String")) {
                // static, final
                int modifiers =  field.getModifiers();
                if (Modifier.isFinal(modifiers) &&
                        Modifier.isStatic(modifiers)) {

                    // constant name = constant value
                    field.setAccessible(true);
                    String name = field.getName();
                    Object value = field.get(object);
                    // System.out.println(getAccessModifier(modifiers) + " static final " + name + " "+ value);
                    if (!name.equals(value)) return false;
                }
            }
        }
        return true;
    }


    public static void main(String[] args) {

        Cat cat = new Cat("Vasia",3,"Siberian cat");

        System.out.println("\n5.2 Вывести на консоль все методы класса, включая все родительские методы");
        showAllMethods(cat);

        System.out.println("\n5.3 Вывести на консоль геттеры класса");
        showAllGetters(cat);

        System.out.println("\n5.4 Проверить все ли String константы указанного класса имеют значение = их имени");
        try {
            System.out.println("Ответ: " +
                    isAllStringConstantNamesEqualToValues(new StringConstantsClass()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\n5.7 Реализовать класс BeanUtils по документации");
        Cat catTo = new Cat("Vasia",3,"Siamese cat");
        Pet petTo = new Pet("Polly",1);
        Cat catFrom = new Cat("Murzik",1,"Siberian cat");
        System.out.println("Перед копированием: ");
        System.out.println("To: " + catTo);
        System.out.println("From: " + catFrom);
        try {
            BeanUtils.assign(catTo, petTo);
        } catch (InvocationTargetException | IllegalAccessException e ) {
            e.printStackTrace();
        }
        System.out.println("После копирования: ");
        System.out.println("To: " + catTo);
        System.out.println("From: " + catFrom);
    }
}
