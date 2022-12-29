#!/bin/bash

export DOCKER_TAG=lionants02/tele_api:a4
export MIS_SERVICE_NAME=tele-stack_tele_api
docker pull ${DOCKER_TAG}
docker service update --image=${DOCKER_TAG} ${MIS_SERVICE_NAME}


