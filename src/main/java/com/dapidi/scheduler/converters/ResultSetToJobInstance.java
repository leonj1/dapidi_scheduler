package com.dapidi.scheduler.converters;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.mappers.MyConverter;
import com.dapidi.scheduler.models.JobInstance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ResultSetToJobInstance implements MyConverter {
    private String name;
    private SimpleDateFormat format;

    public ResultSetToJobInstance() {
        this.name = "JobInstance";
        this.format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public Object object(ResultSet rs) throws SQLException, ParseException {
        return new JobInstance(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("job_id")),
                JobInstanceState.valueOf(rs.getString("job_state")),
                format.parse(rs.getString("create_date")).toInstant()
        );
    }

    @Override
    public String name() {
        return this.name;
    }
}
