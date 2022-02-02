package protocolData;

import org.apache.tools.ant.taskdefs.Zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile {

    public static void unzip(String source, String target) {
        System.out.println("uuunnnziiipppp");
        Path mySource = Paths.get(source);
        Path myTarget = Paths.get(target);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(mySource.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();
            System.out.println("1");

            while (zipEntry != null) {
                System.out.println("2");
                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                    System.out.println("3");
                }
                Path newPath = zipSlipProject(zipEntry, myTarget);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                    System.out.println("4");
                }
                else {
                    System.out.println("5");
                    if (newPath.getParent() != null) {
                        System.out.println("6");
                        if (Files.notExists(newPath.getParent())) {
                            System.out.println("7");
                            Files.createDirectories(newPath.getParent());
                        }
                    }
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Path zipSlipProject(ZipEntry zipEntry, Path targetDir)
            throws IOException {
        System.out.println("8");

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            System.out.println("9");
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
}

