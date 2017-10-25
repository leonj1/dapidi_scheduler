package com.dapidi.scheduler.mappers;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class MyMapperResultSetExtractor <T extends MyConverter, U> implements ResultSetExtractor<List<U>> {
    private static final Logger log = LoggerFactory.getLogger(MyMapperResultSetExtractor.class);
    private T t;

    public MyMapperResultSetExtractor(T t) {
        this.t = t;
    }

    @Override
    public List<U> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<U> results = Lists.newArrayList();
        while(rs.next()) {
            try {
                results.add((U) t.object(rs));
            } catch (SQLException|ParseException e) {
                log.error(
                        String.format(
                                "Problem getting %s from ResultSet: %s",
                                t.name(),
                                e.getMessage()
                        ))
                ;
            }
        }
        return results;
    }
}
