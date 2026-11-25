# Online Store – Technical Assessment

Three small Spring Boot applications:

| App | Port | Responsibility | Storage |
|---|---|---|---|
| `catalog-service` | 8081 | Product catalog REST API | In memory (no database yet) |
| `order-service` | 8082 | Order REST API, client of `catalog-service` | H2 in-memory database |
| `store-web` | 8080 | Backend for Frontend + Thymeleaf UI | HTTP session (cart) |

## Requirements

- JDK 25
- Maven 3.10.1+ or use the Maven wrapper at `.mvn/wrapper/maven-wrapper.properties` generated with `mvn -N wrapper:wrapper -Dmaven=3.10.1`)

## Run

```bash
mvn -f catalog-service spring-boot:run
mvn -f order-service spring-boot:run
mvn -f store-web spring-boot:run
```

Open http://localhost:8080.

The order database runs in memory and is initialized automatically. Orders are lost
when `order-service` stops; no external database or Docker is required.

## Build and test everything

```bash
mvn verify
```

## The exercise

The build is red on purpose. Run `mvn verify` and you will see failing tests.
Work through them one at a time, in any order:

1. Run a failing test and read the failure.
2. **Explain why it fails before you change anything.**
3. Fix the code, and re-run until it passes without breaking other tests.

See [`TASKS.md`](TASKS.md) for the list of failing tests. Test names start with `Task N`. If a fix changes a signature or route, update the test accordingly.
The `order-service` tests start a small fake catalog on port 8081, so stop `catalog-service` before running them.

When `Cart.summary()` passes, explain how you would improve its implementation, and why.
