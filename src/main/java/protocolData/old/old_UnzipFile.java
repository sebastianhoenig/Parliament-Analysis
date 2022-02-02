package protocolData.old;

// java
// import org.apache.tools.ant.taskdefs.Zip;

/*public class UnzipFile {
    String source;
    String target;

    public UnzipFile(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public void unzip() {
        Path mySource = Paths.get(this.source);
        Path myTarget = Paths.get(this.target);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(mySource.toFile()))) {
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }
                Path newPath = zipSlipProject(zipEntry, myTarget);

                if (isDirectory) {
                    Files.createDirectories(newPath);
                }
                else {
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
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

    public Path zipSlipProject(ZipEntry zipEntry, Path targetDir)
            throws IOException {

        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }

        return normalizePath;
    }
}*/