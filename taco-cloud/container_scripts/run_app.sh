#! /usr/bin/env bash

java -jar taco-cloud-0.0.1-SNAPSHOT.jar 2>&1 | tee /var/log/taco-cloud/taco-cloud-$(date +%s).log
