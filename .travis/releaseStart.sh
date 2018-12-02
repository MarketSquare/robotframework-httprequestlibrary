#!/bin/bash

if [ -z "$SONATYPE_USERNAME" ]
then
    echo "error: please set SONATYPE_USERNAME and SONATYPE_PASSWORD environment variable"
    exit 1
fi

if [ -z "$SONATYPE_PASSWORD" ]
then
    echo "error: please set SONATYPE_PASSWORD environment variable"
    exit 1
fi

if [ -z "$GIT_USERNAME" ]
then
    echo "error: please set GIT_USERNAME and GIT_PASSWORD environment variable"
    exit 1
fi

if [ -z "$GIT_PASSWORD" ]
then
    echo "error: please set GIT_PASSWORD environment variable"
    exit 1
fi

mvn clean jgitflow:release-start --settings .travis/settings.xml -DskipTests=true -B -U -Prelease
