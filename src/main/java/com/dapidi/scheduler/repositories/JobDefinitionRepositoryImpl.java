package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.converters.ResultSetToJobDefinition;
import com.dapidi.scheduler.mappers.MyMapperResultSetExtractor;
import com.dapidi.scheduler.mappers.MyMapperRowMapper;
import com.dapidi.scheduler.models.JobDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobDefinitionRepositoryImpl implements JobDefinitionRepository {
    private static final Logger log = LoggerFactory.getLogger(JobDefinitionRepositoryImpl.class);
    private static final String TABLE = "job_definition";

    private MyMapperResultSetExtractor<ResultSetToJobDefinition, JobDefinition> myMapperResultSetExtractor;
    private MyMapperRowMapper<ResultSetToJobDefinition, JobDefinition> myMapperRowMapper;
    private JdbcTemplate jdbcTemplate;

    public JobDefinitionRepositoryImpl(MyMapperResultSetExtractor<ResultSetToJobDefinition, JobDefinition> myMapperResultSetExtractor, MyMapperRowMapper<ResultSetToJobDefinition, JobDefinition> myMapperRowMapper, JdbcTemplate jdbcTemplate) {
        this.myMapperResultSetExtractor = myMapperResultSetExtractor;
        this.myMapperRowMapper = myMapperRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public JobDefinition findOne(UUID id) {
        String sql = String.format("select * from %s where id='%s'", TABLE, id);
        log.debug(sql);
        JobDefinition jobDefinition = null;
        try {
            jobDefinition = this.jdbcTemplate.queryForObject(sql, this.myMapperRowMapper);
        } catch (DataAccessException e) {
            log.warn(String.format("No Job definition for id %s", id));
        }
        return jobDefinition;
    }

    @Override
    public UUID save(JobDefinition item) {
        expect(item, "item").not().toBeNull().check();
        if (item.getId() != null) {
            String sql = String.format("UPDATE %s SET name=?, command=?, schedule=?, machine=?, run_as=?, user_profile=?, alarm_if_fail=?, retry_on_failure=?, cron_date=?, condition_statement=?, std_out_file=?, std_err_file=?, comment=?, max_run_time=?, max_retry=? where id = ?", TABLE);
            this.jdbcTemplate.update(
                    sql,
                    item.getName(),
                    item.getCommand(),
                    item.getSchedule(),
                    item.getMachine(),
                    item.getRunAs(),
                    item.getUserProfile(),
                    item.isAlarmIfFail(),
                    item.getRetryOnFailure(),
                    item.getCronDate(),
                    item.getCondition(),
                    item.getStdoutFile(),
                    item.getStderrFile(),
                    item.getComment(),
                    item.getMaxRunTime(),
                    item.getMaxRetry(),
                    item.getId()
            );
        } else {
            UUID itemId = UUID.randomUUID();
            item.setId(itemId);
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(TABLE);
            Map<String, Object> parameters = new HashMap<String, Object>() {{
                put("id", itemId.toString());
                put("name", item.getName());
                put("command", item.getCommand());
                put("schedule", item.getSchedule());
                put("machine", item.getMachine());
                put("run_as", item.getRunAs());
                put("user_profile", item.getUserProfile());
                put("alarm_if_fail", item.isAlarmIfFail());
                put("cron_date", item.getCronDate());
                put("condition_statement", item.getCondition());
                put("retry_on_failure", item.getRetryOnFailure());
                put("std_out_file", item.getStdoutFile());
                put("std_err_file", item.getStderrFile());
                put("max_run_time", item.getMaxRunTime());
                put("max_retry", item.getMaxRetry());
                put("comment", item.getComment());
            }};
            jdbcInsert.execute(parameters);
        }
        return item.getId();
    }

    @Override
    public void delete(UUID id) {
        String sql = String.format("delete from %s where id='%s'", TABLE, id);
        log.debug(sql);
        this.jdbcTemplate.execute(sql);
    }

    @Override
    public List<JobDefinition> findAll() {
        String sql = String.format("select * from %s", TABLE);
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public JobDefinition findByName(String name) {
        String sql = String.format("select * from %s where name='%s'", TABLE, name);
        log.debug(sql);
        JobDefinition jobDefinition = null;
        try {
            jobDefinition = this.jdbcTemplate.queryForObject(sql, this.myMapperRowMapper);
        } catch (DataAccessException e) {
            log.warn(String.format("No Job definition for name %s", name));
        }
        return jobDefinition;
    }
}
