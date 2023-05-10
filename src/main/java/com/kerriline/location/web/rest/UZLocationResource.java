package com.kerriline.location.web.rest;

import com.kerriline.location.LocationManager;
import com.kerriline.location.ReportManager;
import com.kerriline.location.domain.LocationRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link LocationRequest}.
 */
@RestController
@RequestMapping("/static")
@Transactional
public class UZLocationResource {

    private final Logger log = LoggerFactory.getLogger(UZLocationResource.class);

    @Autowired
    LocationManager location;

    @GetMapping("/submit-request")
    public ResponseEntity<Void> submitRequestByEmail() {
        try {
            int sendEmails = location.sendLocationRequest();
            ResponseEntity.accepted().body("Successfully sent " + sendEmails + "mails");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/process-response")
    public ResponseEntity<Void> processResponseReceivedByEmail() {
        try {
            location.saveLocationResponses();
            ResponseEntity.accepted().body("Successfully processed email response");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return ResponseEntity.status(500).build();
        }
    }

    @Autowired
    ReportManager reports;

    @GetMapping("/generate-report")
    public ResponseEntity<ByteArrayResource> generateReport() {
        try {
            File file = reports.generateReport();

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

            return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/send-report")
    public ResponseEntity<String> sendReport() {
        try {
            location.sendReport(reports.generateReport());

            return ResponseEntity.ok().body("Report send successfully");
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/full-flow")
    public ResponseEntity<String> full() {
        try {
            location.updateAndSendReport();

            return ResponseEntity.ok().body("Everything done successfully");
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return ResponseEntity.status(500).build();
        }
    }
}
