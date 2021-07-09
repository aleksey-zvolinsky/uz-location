package com.kerriline.location.web.rest;

import com.kerriline.location.LocationManager;
import com.kerriline.location.domain.LocationRequest;
import com.kerriline.location.repository.LocationRequestRepository;
import com.kerriline.location.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link LocationRequest}.
 */
@RestController
@RequestMapping("/static")
@Transactional
public class UZLocationResource {

    private final Logger log = LoggerFactory.getLogger(UZLocationResource.class);

    private static final String ENTITY_NAME = "locationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationRequestRepository locationRequestRepository;

    public UZLocationResource(LocationRequestRepository locationRequestRepository) {
        this.locationRequestRepository = locationRequestRepository;
    }

    @Autowired
    LocationManager location;

    @GetMapping("/submit-request")
    public ResponseEntity<Void> submitRequestByEmail() {
        try {
            location.send();
            return ResponseEntity
                .noContent()
                .build();
        } catch (Exception e) {
            log.error("Failed to get mails", e);
            return  ResponseEntity
                .status(500)
                .build();
        }


    }
}
