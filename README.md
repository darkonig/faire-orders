# Faire Orders

This api who connects to Faire service API and process the NEW orders from a given brandId.

# Building
```cmd
./mvnw package
```

# Install
```cmd
./mvnw install
```

# Build image
```cmd
./mvnw package dockerfile:build
```

# Run docker image
```cmd
docker-compose up -d
```