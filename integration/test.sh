#!/bin/bash

export CORE_NETWORK=core_net
export PROJECT=scheduler
export container=${PROJECT}; docker stop $container; docker rm $container
export container=${PROJECT}_db; docker stop $container; docker rm $container

docker run --name ${PROJECT}_db \
-p 6650:3350 \
-e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
-e MYSQL_ROOT_HOST=% \
-e CHECK_INTERVAL=15 \
-v /Users/jose/workarea/dapidi/${PROJECT}/src/main/resources:/docker-entrypoint-initdb.d \
-v /Users/jose/workarea/dapidi/${PROJECT}/src/main/resources/my.cnf:/etc/my.cnf \
--net ${CORE_NETWORK} \
-d mysql/mysql-server:latest

echo "Waiting for DB to come online"
while ! netstat -tna | grep 'LISTEN\>' | grep -q '.6650'; do
  sleep 5
done


docker run -d --name ${PROJECT} \
-p 3230:3230 \
-e APP_PROFILE=integration \
--net ${CORE_NETWORK} \
dapidi/${PROJECT}:0.1

echo "Waiting for ${PROJECT} to come online"
while ! netstat -tna | grep 'LISTEN\>' | grep -q '.3230'; do
  sleep 5
done

sleep 10

curl http://localhost:5676/public/health

