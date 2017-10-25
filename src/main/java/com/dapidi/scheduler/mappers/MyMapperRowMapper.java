package com.dapidi.scheduler.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class MyMapperRowMapper <T extends MyConverter, U> implements RowMapper<U> {
    private static final Logger log = LoggerFactory.getLogger(MyMapperRowMapper.class);
    private T t;

    public MyMapperRowMapper(T t) {
        this.t = t;
    }

    @Override
    public U mapRow(ResultSet rs, int i) throws SQLException {
//        Source: http://stackoverflow.com/questions/867194/java-resultset-how-to-check-if-there-are-any-results
//        ... this returns false if the cursor is not before the first record or if there are no rows in the ResultSet.
        if (!rs.isBeforeFirst() && !rs.first()) {
            return null;
        }
        U item = null;
        try {
            item = (U) t.object(rs);
        } catch (SQLException|ParseException e) {
            log.error(String.format("Problem getting Account from ResultSet: %s", e.getMessage()));
        }
        return item;
    }
}
