#!/usr/bin/env bash

java -jar target/cred.experiments-1.0-shaded.jar \
--debug.logging false \
--log.directory logs \
--local.filename src/main/resources/project.properties \
--s3.bucket.name cred-experiment \
--s3.secret.name s3-access-secret

