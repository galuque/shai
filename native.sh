#!/usr/bin/env bash

native-image --report-unsupported-elements-at-runtime \
                 --no-fallback \
                 --static \
                 -jar target/*.jar \
                 -o target/shai \
                 -H:+ReportExceptionStackTraces \
                 --features=clj_easy.graal_build_time.InitClojureClasses \
                 --enable-url-protocols=http,https \
                 -march=native \
                 --strict-image-heap \
                 -J-Xmx4G \
                 --gc=epsilon