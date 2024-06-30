# builder
FROM golang:1.22-alpine AS builder

RUN set -ex &&\
    apk add --no-progress --no-cache \
      gcc \
      musl-dev

WORKDIR /go/src/app
COPY go.* ./
RUN go mod download

COPY . .
RUN go get -d -v
RUN GOOS=linux GOARCH=arm64 go build -a -v -tags musl

CMD ["./gps_processing_go"]