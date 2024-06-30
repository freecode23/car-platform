# Stage 1: Build the Go binary
FROM golang:1.22 AS build

WORKDIR /app

# Copy go.mod and go.sum files
COPY go.mod go.sum ./

# Download dependencies
RUN go mod download

# Copy the source code
COPY . .

# Build the Go binary
RUN go build -o go-consumer main.go

# Stage 2: Create the final image
FROM alpine:latest
WORKDIR /root/

# Install necessary dependencies
RUN apk add --no-cache ca-certificates libc6-compat

# Copy the Go binary from the build stage
COPY --from=build /app/go-consumer .

# Ensure the binary is executable
RUN chmod +x go-consumer

# Set environment variables
ENV BOOTSTRAP_SERVERS=kafka:29092

# Command to run the binary
CMD ["./go-consumer"]
