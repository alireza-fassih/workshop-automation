package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.manager.UploaderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Secured("ADMIN")
@RestController
@RequestMapping("/rest/upload")
public class Uploader {

    private static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

    @Autowired
    private UploaderManager uploaderManager;


    @PostMapping("/{id:"+UUID_REGEX+"}")
    public void handleFileUpload(@RequestParam("filepond") MultipartFile file, @PathVariable("id") String id) {
        try {
            uploaderManager.saveUpload(id, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/{id:"+UUID_REGEX+"}")
    public void deleteFile(@PathVariable("id") String id) {
        uploaderManager.removeUpload(id);
    }
}
