package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.converters.ResultSetToJobInstance;
import com.dapidi.scheduler.enums.JobInstanceState;
import com.dapidi.scheduler.mappers.MyMapperResultSetExtractor;
import com.dapidi.scheduler.mappers.MyMapperRowMapper;
import com.dapidi.scheduler.models.JobInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobInstanceRepositoryImpl implements JobInstanceRepository {
    private static final Logger log = LoggerFactory.getLogger(JobInstanceRepositoryImpl.class);
    private static final String TABLE = "job_instances";

    private JdbcTemplate jdbcTemplate;
    private MyMapperRowMapper<ResultSetToJobInstance, JobInstance> myMapperRowMapper;
    private MyMapperResultSetExtractor<ResultSetToJobInstance, JobInstance> myMapperResultSetExtractor;

    public JobInstanceRepositoryImpl(JdbcTemplate jdbcTemplate, MyMapperRowMapper<ResultSetToJobInstance, JobInstance> myMapperRowMapper, MyMapperResultSetExtractor<ResultSetToJobInstance, JobInstance> myMapperResultSetExtractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.myMapperRowMapper = myMapperRowMapper;
        this.myMapperResultSetExtractor = myMapperResultSetExtractor;
    }

    @Override
    @Transactional
    public UUID save(JobInstance item) {
        expect(item, "item").not().toBeNull().check();
        if (item.getId() != null) {
            String sql = String.format("UPDATE %s SET job_id='%s', job_state=?, create_time=? where id='%s'", TABLE, item.getJobId(), item.getId());
            int num = this.jdbcTemplate.update(
                    sql,
                    item.getJobState().name(),
                    Date.from(item.getCreateTime())
            );
            log.debug(String.format("Updated jobInstance %s with job_state %s", item.getId(), item.getJobState().name()));
            return item.getId();
        } else {
            UUID itemId = UUID.randomUUID();
            item.setId(itemId);
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(TABLE);
            Map<String, Object> parameters = new HashMap<String, Object>() {{
                put("id", itemId.toString());
                put("job_id", item.getJobId());
                put("create_time", Date.from(item.getCreateTime()));
                put("job_state", item.getJobState().name());
            }};
            jdbcInsert.execute(parameters);
            return itemId;
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = String.format("DELETE from %s where id='%s'", TABLE, id);
        log.debug(sql);
        this.jdbcTemplate.execute(sql);
    }

    @Override
    public void deleteByJobId(UUID id) {
        String sql = String.format("DELETE from %s where job_id='%s'", TABLE, id);
        log.debug(sql);
        this.jdbcTemplate.execute(sql);
    }

    @Override
    public JobInstance findOne(UUID id) {
        String sql = String.format("select * from %s where id='%s'", TABLE, id);
        log.debug(sql);
        JobInstance result = null;
        try {
            result = this.jdbcTemplate.queryForObject(sql, this.myMapperRowMapper);
        } catch (DataAccessException e) {
            log.warn(String.format("Query for jobInstance %s returned no results", id));
        }
        return result;
    }

    @Override
    public List<JobInstance> findAll() {
        String sql = String.format("select * from %s", TABLE);
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public List<JobInstance> findActive() {
        String sql = String.format("select * from %s where job_state='ACTIVE'", TABLE);
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public List<JobInstance> findByJobId(UUID jobId) {
        String sql = String.format("select * from %s where job_id='%s'", TABLE, jobId);
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public List<JobInstance> findLatestForJobs() {
        // SOURCE http://stackoverflow.com/questions/2111384/sql-join-selecting-the-last-records-in-a-one-to-many-relationship
        String sql = String.format("SELECT \n" +
                "  c.id, c.name,\n" +
                "(SELECT pi.price FROM %s pi WHERE pi.id = MAX(p.id)) AS [last_job_instance]\n" +
                "FROM jobs c INNER JOIN %s p \n" +
                "ON c.id = p.jobs_id \n" +
                "GROUP BY c.id,c.name", TABLE, TABLE);
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public List<JobInstance> findJobIdNotInRunState(UUID id, List<JobInstanceState> states) {
        StringJoiner si = new StringJoiner(",", "(", ")");
        for(JobInstanceState state : states) {
            si.add(String.format("'%s'", state.name()));
        }
        String sql = String.format("select * from %s where job_id='%s' and job_state NOT IN %s", TABLE, id, si.toString());
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public List<JobInstance> findNotInRunState(LocalDateTime date, List<JobInstanceState> states) {
        StringJoiner si = new StringJoiner(",", "(", ")");
        for(JobInstanceState state : states) {
            si.add(String.format("'%s'", state.name()));
        }
        String sql = String.format("select * from %s where create_time < '%s' and job_state IN %s", TABLE, date, si.toString());
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public void setToJobInstanceState(List<JobInstance> jobs, JobInstanceState state) {
        expect(jobs).not().toBeNull().check();
        if (jobs.size() == 0) {
            return;
        }
        StringJoiner si = new StringJoiner(",", "(", ")");
        for(JobInstance ji : jobs) {
            si.add(String.format("'%s'", ji.getId()));
        }
        String sql = String.format("update %s set job_state='%s' where id IN %s", TABLE, state.name(), si.toString());
        log.debug(sql);
        this.jdbcTemplate.execute(sql);
    }
}
