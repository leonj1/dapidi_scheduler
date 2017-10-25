package com.dapidi.scheduler.converters;

import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.enums.RunState;
import com.dapidi.scheduler.mappers.MyConverter;
import com.dapidi.scheduler.models.JobRun;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ResultSetToJobRun implements MyConverter {
    private String name;
    private SimpleDateFormat format;

    public ResultSetToJobRun() {
        this.name = "JobRun";
        this.format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public Object object(ResultSet rs) throws SQLException, ParseException {
        return new JobRun(
                UUID.fromString(rs.getString("id")),
                UUID.fromString(rs.getString("job_instance_id")),
                RunState.valueOf(rs.getString("run_state")),
                JobInstanceState.valueOf(rs.getString("job_state")),
                rs.getInt("exit_code"),
                this.format.parse(rs.getString("start_time")).toInstant(),
                this.format.parse(rs.getString("end_time")).toInstant()
        );
    }

    @Override
    public String name() {
        return this.name;
    }
}
