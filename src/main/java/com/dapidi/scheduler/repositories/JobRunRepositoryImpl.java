package com.dapidi.scheduler.repositories;

import com.dapidi.scheduler.converters.ResultSetToJobRun;
import com.dapidi.scheduler.mappers.MyMapperResultSetExtractor;
import com.dapidi.scheduler.models.JobRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.choonchernlim.betterPreconditions.preconditions.PreconditionFactory.expect;

/**
 * Created for K and M Consulting LLC.
 * Created by Jose M Leon 2017
 **/
public class JobRunRepositoryImpl implements JobRunRepository {
    private static final Logger log = LoggerFactory.getLogger(JobRunRepositoryImpl.class);
    private static final String TABLE = "job_run";

    private JdbcTemplate jdbcTemplate;
    private MyMapperResultSetExtractor<ResultSetToJobRun, JobRun> myMapperResultSetExtractor;

    public JobRunRepositoryImpl(JdbcTemplate jdbcTemplate, MyMapperResultSetExtractor<ResultSetToJobRun, JobRun> myMapperResultSetExtractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.myMapperResultSetExtractor = myMapperResultSetExtractor;
    }

    @Override
    public List<JobRun> findByJobInstanceId(UUID id) {
        String sql = String.format("select * from %s where job_instance_id='%s'", TABLE, id);
        log.debug(sql);
        return this.jdbcTemplate.query(sql, this.myMapperResultSetExtractor);
    }

    @Override
    public void save(JobRun item) {
        expect(item, "item").not().toBeNull().check();
        if (item.getId() != null) {
            String sql = String.format("UPDATE %s SET job_instance_id=?, run_state=?, job_state=?, exit_code=?, start_time=?, end_time=? where id=?", TABLE);
            this.jdbcTemplate.update(
                    sql,
                    item.getJobInstanceId(),
                    item.getRunState(),
                    item.getJobState(),
                    item.getExitCode(),
                    item.getStartTime(),
                    item.getEndTime(),
                    item.getId()
            );
        } else {
            UUID itemId = UUID.randomUUID();
            item.setId(itemId);
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName(TABLE);
            Map<String, Object> parameters = new HashMap<String, Object>() {{
                put("id", itemId.toString());
                put("job_instance_id", item.getJobInstanceId());
                put("run_state", item.getRunState());
                put("job_state", item.getJobState());
                put("exit_code", item.getExitCode());
                put("start_time", Date.from(item.getStartTime()));
                put("end_time", Date.from(item.getEndTime()));
            }};
            jdbcInsert.execute(parameters);
        }
    }
}
