FROM sbtscala/scala-sbt:graalvm-ce-21.3.0-java11_1.7.1_2.13.8 AS builder

WORKDIR /build
COPY . .

RUN [ "sbt", "stage" ]


FROM ghcr.io/graalvm/graalvm-ce:ol7-java11-21.3.3

ARG http_server_port=8000
ENV APP_HTTP_SERVER_PORT=${http_server_port}

EXPOSE ${http_server_port}

WORKDIR /cli
COPY --from=builder /build/target/universal/stage ./stage

HEALTHCHECK --interval=5m --timeout=3s \
  CMD curl -f http://localhost/ || exit 1

ENTRYPOINT [ "./stage/bin/api" ]
