#!/usr/bin/env bash

set -x

native-image --report-unsupported-elements-at-runtime \
                 --no-fallback \
                 --static \
                 --libc=musl \
                 -jar target/*.jar \
                 -o target/shai \
                 -H:+ReportExceptionStackTraces \
                 --features=clj_easy.graal_build_time.InitClojureClasses \
                 --enable-url-protocols=http,https \
                 -march=compatibility \
                 --strict-image-heap \
                 -J-Xmx4500m \
                 --gc=epsilon