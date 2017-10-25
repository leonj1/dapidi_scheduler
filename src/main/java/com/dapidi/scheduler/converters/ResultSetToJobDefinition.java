package com.dapidi.scheduler.converters;

import com.dapidi.scheduler.mappers.MyConverter;
import com.dapidi.scheduler.models.JobDefinition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.UUID;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class ResultSetToJobDefinition implements MyConverter {
    private String name;

    public ResultSetToJobDefinition() {
        this.name = "JobDefinition";
    }

    @Override
    public Object object(ResultSet rs) throws SQLException, ParseException {
        return new JobDefinition(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("command"),
                rs.getString("schedule"),
                rs.getString("machine"),
                rs.getString("run_as"),
                rs.getString("user_profile"),
                rs.getBoolean("alarm_if_fail"),
                rs.getInt("retry_on_failure"),
                rs.getString("cron_date"),
                rs.getString("condition_statement"),
                rs.getString("std_out_file"),
                rs.getString("std_err_file"),
                rs.getString("comment"),
                rs.getInt("max_run_time"),
                rs.getInt("max_retry")
        );
    }

    @Override
    public String name() {
        return this.name;
    }
}
