package proxy;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SerializationUtil {

    public static void serialize(Serializable o, File file) throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(
                new FileOutputStream(file))) {
            stream.writeObject(o);
        }
    }

    public static <T> T deserialize(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream stream = new ObjectInputStream(
                new FileInputStream(file))) {
            return (T) stream.readObject();
        }
    }

    public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ObjectInputStream stream = new ObjectInputStream(
                new ByteArrayInputStream(bytes))) {
            return (T) stream.readObject();
        }
    }

    public static String createZipFileName(File file){
        String fileName = file.getAbsoluteFile().toString();
        String zipFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".zip";
        return zipFileName;
    }

    public static void zipFile(File file) {
        String zipFileName = createZipFileName(file);
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(file)) {
            String entryName = file.getName();
            ZipEntry e = new ZipEntry(entryName);
            zos.putNextEntry(e);

            int tmp = 0;
            while ((tmp = fis.read()) >= 0) {
                zos.write(tmp);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Файл " + zipFileName + " не найден", e);
        } catch (IOException e) {
            throw new RuntimeException("Исключение при работе с ZipOutputStream", e);
        }
    }
}
