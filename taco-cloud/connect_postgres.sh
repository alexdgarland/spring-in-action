#! /usr/bin/env bash

container_id=$(docker ps | grep postgres | cut -d' ' -f1)
docker exec -it $container_id psql -U tacos
