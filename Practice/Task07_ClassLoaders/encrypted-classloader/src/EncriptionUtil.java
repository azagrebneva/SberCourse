import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncriptionUtil {

    public static void encrypt(String pathFrom, String pathTo, String classname, byte[] keys) {

        // Проверка, что переданные пути являются траекториями
        File fileFrom = new File(pathFrom);
        File fileTo = new File(pathTo);
        if (!fileFrom.isDirectory() || !fileTo.isDirectory())  {
            throw new IllegalArgumentException("Переданные пути не являются директориями.");
        }

        try {
            // чтение из файла
            Path file = Paths.get(pathFrom+"\\"+classname+".class");
            byte[] content = Files.readAllBytes(file);

            // кодировка
            Cipher encryption = Cipher.getInstance("AES");
            encryption.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keys, 0, keys.length, "AES"));
            byte[] encryptedContent = encryption.doFinal(content);

            // запись в файл
            FileOutputStream out = new FileOutputStream(pathTo+"\\"+classname+".class");
            out.write(encryptedContent);
            out.close();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибки при декодировании файла.", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибки при работе с файлами.", e);
        }
    }

    public static void main(String[] args) throws Exception{

        byte[] keys = {-77, 50, -115, 63, 109, -13, 126, -25, 101, 34, -19, 56, 61, -11, -75, 103};

        String originalPath = "D:\\Java\\SberCourse\\Practice\\Task07_ClassLoaders\\encrypted-classloader\\plugin\\original";
        String encryptedPath = "D:\\Java\\SberCourse\\Practice\\Task07_ClassLoaders\\encrypted-classloader\\plugin\\encrypted";
        String classname = "GoodLuck";

        // кодировка файла
        EncriptionUtil.encrypt(originalPath, encryptedPath, classname, keys);
    }
}
