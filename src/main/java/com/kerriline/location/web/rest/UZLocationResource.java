package com.kerriline.location.web.rest;

import com.kerriline.location.LocationManager;
import com.kerriline.location.ReportManager;
import com.kerriline.location.domain.LocationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link LocationRequest}.
 */
@RestController
@RequestMapping("/static")
@Transactional
public class UZLocationResource {

    private final Logger log = LoggerFactory.getLogger(UZLocationResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    LocationManager location;

    @GetMapping("/submit-request")
    public ResponseEntity<Void> submitRequestByEmail() {
        try {
            int sendEmails = location.send();
            ResponseEntity
            	.accepted()
            	.body("Successfully sent " + sendEmails + "mails");
			return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return  ResponseEntity
                .status(500)
                .build();
        }
    }
    
    @GetMapping("/process-response")
    public ResponseEntity<Void> processResponseReceivedByEmail() {
        try {
        	location.mail2database();
            ResponseEntity
            	.accepted()
            	.body("Successfully processed email response");
			return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return  ResponseEntity
                .status(500)
                .build();
        }
    }

    
    @Autowired
    ReportManager reports;
    
    @GetMapping("/generate-report")
    public ResponseEntity<Void> generateReport() {
        try {
        	reports.generateReport();
            ResponseEntity
            	.accepted()
            	.body("Successfully processed email response");
			return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return  ResponseEntity
                .status(500)
                .build();
        }
    }
}
