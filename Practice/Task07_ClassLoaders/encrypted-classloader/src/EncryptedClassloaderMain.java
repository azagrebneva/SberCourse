
/*
  Задание 7.2 Написать EncryptedClassloader
  Данный класслоадер умеет загружать классы из файлов дешифрую их. Ваша задача переопределить метод findClass(). В нем лоадер считывает зашифрованный массив байт, дешифрует его и превращает в класс (с помощью метода defineClass).
  На вход класслодер принимает ключ шифрования, рутовую папку, в которой будет искать классы, родительский класслодер. Логика шифрования/дешифрования с использованием ключа может быть любой на ваш вкус (например, каждый считаный байт класса увеличить на определение число).
*/

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EncryptedClassloaderMain {

    public static void main(String[] args) {

        byte[] keys = {-77, 50, -115, 63, 109, -13, 126, -25, 101, 34, -19, 56, 61, -11, -75, 103};

        String originalPath = "D:\\Java\\SberCourse\\Practice\\Task07_ClassLoaders\\encrypted-classloader\\plugin\\original";
        String encryptedPath = "D:\\Java\\SberCourse\\Practice\\Task07_ClassLoaders\\encrypted-classloader\\plugin\\encrypted";
        String classname = "GoodLuck";

        // кодировка файла
        EncriptionUtil.encrypt(originalPath, encryptedPath, classname, keys);

        // декодирование класса с помощью classloader'a
        File file = new File(encryptedPath);
        EncryptedClassLoader loader = new EncryptedClassLoader(keys, file, ClassLoader.getSystemClassLoader());
        try {
            Class<?> foundClass = loader.findClass("GoodLuck");
            Object object = foundClass.getConstructor().newInstance();
            Method method = foundClass.getMethod("printPhrase", null);
            method.invoke(object, null);
        } catch ( NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибки при создании объекта класса с помощью рефлексии.", e);
        }
    }
}
