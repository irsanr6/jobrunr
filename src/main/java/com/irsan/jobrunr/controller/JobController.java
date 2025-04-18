package com.irsan.jobrunr.controller;

import com.irsan.jobrunr.service.SampleJobService;
import org.jobrunr.jobs.Job;
import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.states.StateName;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.StorageProvider;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobScheduler jobScheduler;

    private final StorageProvider storageProvider;


    private final SampleJobService sampleJobService;

    public JobController(JobScheduler jobScheduler, StorageProvider storageProvider, SampleJobService sampleJobService) {
        this.jobScheduler = jobScheduler;
        this.storageProvider = storageProvider;
        this.sampleJobService = sampleJobService;
    }

    @PostMapping("/run-now")
    public String runJobNow() {
        jobScheduler.enqueue(sampleJobService::executeJob);
        return "Job telah dijalankan!";
    }

    // API untuk menjadwalkan job (misalnya setelah 1 menit)
    @PostMapping("/schedule")
    public String scheduleJob(@RequestParam int delayInMinutes) {
        jobScheduler.schedule(java.time.Instant.now().plusSeconds(delayInMinutes * 60L),
                sampleJobService::executeJobFromSchedule);
        return "Job dijadwalkan dalam " + delayInMinutes + " menit!";
    }

    @PostMapping("/run-now-with-progress")
    public String runJobNowWithProgress() {
        UUID uuid = UUID.randomUUID();
        JobId jobId = jobScheduler.enqueue(uuid, () -> sampleJobService.executeJobWithProgress(JobContext.Null, uuid));
        return "Job dengan progress telah dijalankan! Dengan ID " + jobId;
    }

    @GetMapping("/{jobId}/progress")
    public String getJobProgress(@PathVariable UUID jobId) {
        Job job = storageProvider.getJobById(jobId);

        if (job == null) {
            return "Job tidak ditemukan.";
        }

        StateName jobState = job.getState();

        return "Job ID: " + jobId + ", Status: " + jobState.name();
    }

}
