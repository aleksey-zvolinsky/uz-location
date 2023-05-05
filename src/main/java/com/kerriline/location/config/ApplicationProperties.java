package com.kerriline.location.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to UZ Location.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {}
