#!/usr/bin/env bash

docker-compose -f docker-compose.yml down
./build.sh
docker-compose -f docker-compose.yml up
