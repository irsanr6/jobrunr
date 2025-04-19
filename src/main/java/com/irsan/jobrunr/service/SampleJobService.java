package com.irsan.jobrunr.service;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.scheduling.BackgroundJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SampleJobService {

    private static final Logger log = LoggerFactory.getLogger(SampleJobService.class);

    @Job(retries = 3)
    public void executeJob() {
        throw new RuntimeException("Simulasi error tanpa retry");
    }

    public void executeJobFromSchedule() {
        log.info("Distributed job schedule is running on instance: {}", System.getenv("HOSTNAME"));
    }

    public void executeJobWithProgress(JobContext jobContext, UUID jobId) {
        log.info("Job {} dimulai di instance: {}", jobId, System.getenv("HOSTNAME"));

        JobDashboardProgressBar progressBar = jobContext.progressBar(1000);

        for (int i = 0; i < 1000; i++) {
            progressBar.increaseByOne();

            log.info("Progress Job {} : {}", jobId, i + "%");
        }

        log.info("Job {} selesai.", jobId);
    }

}
