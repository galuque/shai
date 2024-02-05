#!/usr/bin/env bash

set -x

SHAI_STATIC=${SHAI_STATIC:-}

args=("--report-unsupported-elements-at-runtime"
    "--no-fallback"
    "-jar" target/*.jar
    "-o" "target/shai"
    "-H:+ReportExceptionStackTraces"
    "--features=clj_easy.graal_build_time.InitClojureClasses"
    "--enable-url-protocols=http,https"
    "-march=compatibility"
    "--strict-image-heap"
    "-J-Xmx4500m"
    "--gc=epsilon")

if [[ "${SHAI_STATIC}" == "true" ]]; then
    args+=("--static"
        "--libc=musl")
fi

native-image "${args[@]}"
