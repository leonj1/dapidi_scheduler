package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.mappers.JobResultSetExtractor;
import com.dapidi.scheduler.mappers.JobRowMapper;
import com.dapidi.scheduler.models.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobRepositoryImpl implements JobRepository {
    private static final Logger log = LoggerFactory.getLogger(JobRepositoryImpl.class);
    private static final String TABLE = "jobs";

    private JobResultSetExtractor jobResultSetExtractor;
    private JobRowMapper jobRowMapper;
    private JdbcTemplate jdbcTemplate;

    public JobRepositoryImpl(JobResultSetExtractor jobResultSetExtractor, JobRowMapper jobRowMapper, JdbcTemplate jdbcTemplate) {
        this.jobResultSetExtractor = jobResultSetExtractor;
        this.jobRowMapper = jobRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Job item) {
        expect(item, "item").not().toBeNull().check();
        if (item.getId() != null) {
            String sql = String.format("UPDATE %s SET job_definition_id=?, job_state=? where id = ?", TABLE);
            this.jdbcTemplate.update(
                    sql,
                    item.getJobDefinitionId(),
                    item.getJobState().name(),
                    item.getId()
            );
        } else {
            UUID itemId = UUID.randomUUID();
            item.setId(itemId);
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(TABLE);
            Map<String, Object> parameters = new HashMap<String, Object>() {{
                put("id", itemId.toString());
                put("job_definition_id", item.getJobDefinitionId());
                put("job_state", item.getJobState().name());
            }};
            jdbcInsert.execute(parameters);
        }
    }

    @Override
    public Job findOne(UUID id) {
        String sql = String.format("select * from %s where id='%s'", TABLE, id);
        log.debug(sql);
        Job job = null;
        try {
            job = this.jdbcTemplate.queryForObject(sql, this.jobRowMapper);
        } catch (DataAccessException e) {
            log.warn(String.format("No Job found with id %s", id));
        }
        return job;
    }

    @Override
    public List<Job> findAll() {
        String sql = String.format("select * from %s", TABLE);
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.jobResultSetExtractor);
    }
}
