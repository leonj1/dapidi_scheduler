package com.dapidi.scheduler.converters;

import com.dapidi.scheduler.enums.JobState;
import com.dapidi.scheduler.mappers.MyConverter;
import com.dapidi.scheduler.models.Job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ResultSetToJob implements MyConverter {
    private String name;

    public ResultSetToJob() {
        this.name = "Job";
    }

    @Override
    public Object object(ResultSet rs) throws SQLException, ParseException {
        return new Job(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("job_definition_id")),
                JobState.valueOf(rs.getString("job_state"))
        );
    }

    @Override
    public String name() {
        return this.name;
    }
}
