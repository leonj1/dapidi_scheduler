package com.dapidi.scheduler.mappers;

import com.dapidi.scheduler.models.JobRun;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobRunRowMapper implements RowMapper<JobRun> {

    @Override
    public JobRun mapRow(ResultSet rs, int i) throws SQLException {
//        Source: http://stackoverflow.com/questions/867194/java-resultset-how-to-check-if-there-are-any-results
//        ... this returns false if the cursor is not before the first record or if there are no rows in the ResultSet.
        if (!rs.isBeforeFirst() && !rs.first()) {
            return null;
        }
        return new JobRun(rs);
    }
}
