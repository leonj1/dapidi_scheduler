#!/bin/bash

# Default assumption is that its running on MBP for local dev
DBNAME=${DBNAME:=scheduler}
DBUSER=${DBUSER:=root}
DBPASS=${DBPASS:=}
DBHOST=${DBHOST:=scheduler_db}
DBPORT=${DBPORT:=3350}
DBMAX_ACTIVE_CONNECTIONS=${DBMAX_ACTIVE_CONNECTIONS:=2}
APPCHECKNEXTJOB=${APPCHECKNEXTJOB:=30}
APPCHECKORPHANEDJOB=${APPCHECKORPHANEDJOB:=60}
JOB_ORPHANED_AFTER_DAYS=${JOB_ORPHANED_AFTER_DAYS:=1}
APP_PHONE_MISSING_CLIENTS=${APP_PHONE_MISSING_CLIENTS:=true}
CLIENT_DEFAULT_PORT=${CLIENT_DEFAULT_PORT:=3232}
LOG_LEVEL=${LOG_LEVEL:=DEBUG}
ZK_CONNECTION_STRING:=${ZK_CONNECTION_STRING:=}
APP_PROFILE=${APP_PROFILE:=production}
SERVERPORT=${SERVERPORT:=3230}

java -jar /scheduler.jar \
    --app.db.name=${DBNAME} \
    --app.db.user=${DBUSER} \
    --app.db.pass=${DBPASS} \
    --app.db.host=${DBHOST} \
    --app.db.port=${DBPORT} \
    --app.num.threads=${DBMAX_ACTIVE_CONNECTIONS} \
    --app.check.next.jobs.interval=${APPCHECKNEXTJOB} \
    --app.check.orphaned.jobs.interval=${APPCHECKORPHANEDJOB} \
    --job.orphaned.after.days=${JOB_ORPHANED_AFTER_DAYS} \
    --app.phone.missing.clients=${APP_PHONE_MISSING_CLIENTS} \
    --app.client.default.port=${CLIENT_DEFAULT_PORT} \
    --logging.level.com.dapidi.scheduler=${LOG_LEVEL} \
    --zk.host=${ZK_CONNECTION_STRING} \
    --spring.profiles.active=${APP_PROFILE} \
    --scheduler.server.http.port=${SERVERPORT}

