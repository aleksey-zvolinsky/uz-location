package com.kerriline.location.cucumber;

import com.kerriline.location.UzLocationApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = UzLocationApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
