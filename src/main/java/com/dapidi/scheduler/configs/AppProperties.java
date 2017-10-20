package com.dapidi.scheduler.configs;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.RunState;
import com.josemleon.AppProperty;
import com.josemleon.exceptions.PropertiesFileNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2016
 **/
public class AppProperties {

    private AppProperty getProperty;

    public AppProperties(AppProperty getProperty) {
        this.getProperty = getProperty;
    }

    public String dataSourceUrl() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("spring.datasource.url");
    }

    public String databaseUser() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("app.db.user");
    }

    public String databasePassword() throws PropertiesFileNotFoundException, IOException {
        return this.getProperty.value("app.db.pass");
    }

    public Integer getJobOrphanedAfterDays() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("job.orphaned.after.days"));
    }

    public Integer getCheckOrphanedJobsInterval() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("app.check.orphaned.jobs.interval"));
    }

    public Integer getClientDefaultPort() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("app.client.default.port"));
    }

    public Boolean getPhoneMissingClients() throws PropertiesFileNotFoundException, IOException {
        return Boolean.parseBoolean(this.getProperty.value("app.phone.missing.clients"));
    }

    public Integer getNumThreads() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("app.num.threads"));
    }

    public Long getCheckInterval() throws PropertiesFileNotFoundException, IOException {
        return Long.parseLong(this.getProperty.value("app.check.next.jobs.interval"));
    }

    public int getSchedulerHttpServerPort() throws PropertiesFileNotFoundException, IOException {
        return Integer.parseInt(this.getProperty.value("scheduler.server.http.port"));
    }

    public List<RunState> jobStatesThatCanBeStarted() {
        return new ArrayList<RunState>(){{
            add(RunState.INACTIVE);
            add(RunState.SUCCESS);
            add(RunState.TERMINATED);
        }};
    }

    public List<JobInstanceState> jobInstanceStatesThatCanBeStarted() {
        return new ArrayList<JobInstanceState>(){{
            add(JobInstanceState.UNKNOWN);
        }};
    }

    public List<JobInstanceState> orphanedJobInstanceState() {
        return new ArrayList<JobInstanceState>() {{
            add(JobInstanceState.ACTIVE);
            add(JobInstanceState.INACTIVE);
        }};
    }
}
