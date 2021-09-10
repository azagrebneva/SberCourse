import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncryptedClassLoader extends ClassLoader {
    private final byte[] keys;
    private final File rootDir;

    public EncryptedClassLoader(byte[] keys, File rootDir, ClassLoader parent) {
        super(parent);
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("Переданный путь не является директорией.");
        }
        this.keys = keys;
        this.rootDir = rootDir;
    }

    @Override
    public Class findClass(String name){
        byte[] b = new byte[0];
        try {
            b = loadClassData(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) throws IOException {
        Path file = Paths.get(rootDir+"\\"+name+".class");
        byte[] encryptedContent = Files.readAllBytes(file);

        byte[] decryptedContent = new byte[0];
        try {
            Cipher decryption = Cipher.getInstance("AES");
            decryption.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keys, 0, keys.length, "AES"));
            decryptedContent = decryption.doFinal(encryptedContent);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибки при дешифровке.");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Неправильные ключи метода шифрования");
        }

        return decryptedContent;
    }
}
