package com.kerriline.location;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Scheduler {

    private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(@Value("${application.location.timeoutMinutes}") Long timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
    }

    private final Long timeoutMinutes;

    @Autowired
    LocationManager locationManager;

    @Scheduled(cron = "${application.location.schedule}")
    public void schedulerLocationUpdate() {
        LOG.info("Task Started: " + LocalDateTime.now());
        try {
            executeTaskWithTimeout(
                (Callable<Object>) () -> {
                    locationManager.updateAndSendReport();
                    LOG.info("Task Worked: " + LocalDateTime.now());
                    return true;
                },
                timeoutMinutes
            )
                .get();
        } catch (Exception e) {
            LOG.error("Schedule failed", e);
        }
    }

    ExecutorService service = Executors.newFixedThreadPool(1);
    ScheduledExecutorService canceller = Executors.newSingleThreadScheduledExecutor();

    public <T> Future<T> executeTaskWithTimeout(Callable<T> c, long timeoutMinutes) {
        final Future<T> future = service.submit(c);
        canceller.schedule(
            () -> {
                if (!future.isDone()) LOG.info("Task Cancelled: " + LocalDateTime.now());
                future.cancel(true);
                return "done";
            },
            timeoutMinutes,
            TimeUnit.MINUTES
        );
        return future;
    }
}
