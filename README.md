### Building and running

Build and start:
```sh
docker compose up --build --force-recreate -d
```

Shutdown:
```sh
docker compose down
```

Shutdown + cleanup:
```sh
docker compose down
docker stop fx-service fx-service-redis
docker rm fx-service fx-service-redis
docker image rm fx-service:latest eclipse-temurin:26-jre-jammy redis:latest maven:3.9-eclipse-temurin-26
```

### Implementation

#### Caching
When the FX conversion rates get fetched, I cache them (using Redis), using the base currency as the key (1 entry per base-currency), while also setting an expiration time (built-in redis) and scheduling a task that fetches the new FX rates at the time when they expire in the cache.

#### Idempotency
Added a column for an idempotancy key in the table that stores the currency conversions.

#### Concurrency
The optimistic locking version seems like it would be enough to me. I also have a second protection in the form of the idempotancy header (although it's not a required header).

### Possible improvements
Better (full) exception handling, testing (at all, but also with a separate Spring context with, for example, a no-op implementation of the APIClientService, testcontainers with PG SQL for testing), better polished DTO classes with .convert() extension functions, better class/function naming, better concurrency (althogh I did spend some time on this, I'm sure it can be better), PG SQL instead of H2.

#### Seed data
Client with id 1:
- balance in USD (currency id 1): 10000
- balance in EUR (currency id 2): 8000
  Client with id 2:
- balance in GBP (currency id 3): 5000
