package com.dapidi.scheduler.mappers;

import com.dapidi.scheduler.models.Job;
import com.google.common.collect.Lists;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobResultSetExtractor implements ResultSetExtractor<List<Job>> {
    @Override
    public List<Job> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Job> results = Lists.newArrayList();
        while(rs.next()) {
            results.add(new Job(rs));
        }
        return results;
    }
}
