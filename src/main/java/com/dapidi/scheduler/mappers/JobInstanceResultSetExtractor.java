package com.dapidi.scheduler.mappers;

import com.dapidi.scheduler.models.JobInstance;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobInstanceResultSetExtractor implements ResultSetExtractor<List<JobInstance>> {

    @Override
    public List<JobInstance> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<JobInstance> results = Lists.newArrayList();
        while(rs.next()) {
            results.add(new JobInstance(rs));
        }
        return results;
    }
}
