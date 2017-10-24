USE mysql;
UPDATE user SET host = '%' WHERE host = '1%';
FLUSH PRIVILEGES;

create database scheduler;

create table scheduler.job_definition (
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  schedule VARCHAR(64) NOT NULL,
  command VARCHAR(512) NOT NULL,
  `comment` VARCHAR(512) NOT NULL,
  machine VARCHAR(256) NOT NULL,
  run_as VARCHAR(256) NOT NULL,
  user_profile VARCHAR(256) NOT NULL,
  alarm_if_fail INT NOT NULL,
  max_run_time INT,
  max_retry INT,
  retry_on_failure INT NOT NULL,
  cron_date VARCHAR(64) NOT NULL,
  condition_statement VARCHAR(256) NOT NULL,
  std_out_file VARCHAR(512) NOT NULL,
  std_err_file VARCHAR(512) NOT NULL
);

create table scheduler.job_instances (
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  job_id VARCHAR(64) NOT NULL,
  job_state VARCHAR(64) NOT NULL,
  create_time timestamp default CURRENT_TIMESTAMP NOT NULL
);

create table scheduler.jobs (
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  job_definition_id VARCHAR(64) NOT NULL,
  job_state VARCHAR(64) NOT NULL
);

create table scheduler.job_run (
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  job_instance_id VARCHAR(64) NOT NULL,
  run_state VARCHAR(64) NOT NULL,
  job_state VARCHAR(64),
  exit_code INT,
  start_time timestamp default CURRENT_TIMESTAMP NOT NULL,
  end_time timestamp default CURRENT_TIMESTAMP NOT NULL
);
