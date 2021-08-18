package com.kerriline.location.config;

import com.kerriline.location.LocationManager;
import com.kerriline.location.SchedulerManager;
import com.kerriline.location.mail.MailManager;
import com.kerriline.location.mail.MailParser;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Properties specific to UZ Location.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MailManager mailManager() {
        return new MailManager();
    }

    @Bean
    public MailParser mailParser() {
        return new MailParser();
    }

    private String locationSchedule = "0 45 7,13 ? * *I";

    private String mileageSchedule = "0 45 5 ? * *I";

    @Bean
    public SchedulerManager schedulerManager() throws SchedulerException {
        SchedulerManager manager = new SchedulerManager(locationSchedule, mileageSchedule);
        return manager;
    }

    @Bean
    public LocationManager locationManager() throws SchedulerException {
        LocationManager manager = new LocationManager();
        return manager;
    }

    public String getLocationSchedule() {
        return locationSchedule;
    }

    public String getMileageSchedule() {
        return mileageSchedule;
    }

    public void setLocationSchedule(String locationSchedule) {
        this.locationSchedule = locationSchedule;
    }

    public void setMileageSchedule(String mileageSchedule) {
        this.mileageSchedule = mileageSchedule;
    }

    private ReportMail reportMail = new ReportMail();

    private ResultMail resultMail = new ResultMail();

    public ReportMail getReportMail() {
        return reportMail;
    }

    public void setReportMail(ReportMail reportMail) {
        this.reportMail = reportMail;
    }

    public ResultMail getResultMail() {
        return resultMail;
    }

    public void setResultMail(ResultMail resultMail) {
        this.resultMail = resultMail;
    }

    public static class ReportMail {
        private String login;
        private String password;
        private String to;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }

    public static class ResultMail {
        private String attachmentFilename;
        private String subject;
        private String text;
        private String to;

        public String getAttachmentFilename() {
            return attachmentFilename;
        }

        public void setAttachmentFilename(String attachmentFilename) {
            this.attachmentFilename = attachmentFilename;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }
}
