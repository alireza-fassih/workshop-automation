package ir.fassih.workshopautomation.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class UploaderManager implements InitializingBean {

    private String uploaderPath;
    private Path parent;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.uploaderPath = System.getProperty("java.io.tmpdir") + File.separator + "uploader";
        parent = Paths.get(uploaderPath);
        if(!Files.isDirectory(parent)) {
            Files.createDirectory(parent);
        }
        log.info("working on {}", uploaderPath);
    }



    public void saveUpload(String id, byte[] bytes) {
        try {
            Path tempFile = getPath(id);
            Files.write(tempFile, bytes);
        } catch (IOException e) {
            log.error("cannot create new file", e);
        }
    }

    private Path getPath(String id) throws IOException {
        return Files.createFile(Paths.get(getPathStr(id)));
    }

    public byte[] readFile(String id) {
        try {
            String path = getPathStr(id);
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new IllegalStateException("cannot read file ", e);
        }
    }

    private String getPathStr(String id) {
        return uploaderPath + File.separator + "uploader-" +
                id.replace(".", "").replace(File.separator, "");
    }

    public void removeUpload(String id) {
        try {
            Path tempFile = getPath(id);
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            log.error("cannot remove file", e);
        }
    }
}
