# Assessment Tasks

Every task has a failing test. Run it, read the failure, and explain **why** it fails before you change anything.

```bash
mvn -f store-web test       # tasks 1, 2, 7
mvn -f order-service test   # tasks 3, 4, 5, 6, 9 (stop catalog-service first, the tests start a fake on port 8081)
```

| # | Area | Failing test |
|---|---|---|
| 1 | Java fundamentals / collections | [`CartTest.sameSkuFromHttpIsMerged`](store-web/src/test/java/com/example/store/web/domain/CartTest.java#L29) |
| 2 | Java fundamentals / String | [`CartTest.summaryListsQuantitiesAndNames`](store-web/src/test/java/com/example/store/web/domain/CartTest.java#L39) |
| 3 | Java fundamentals / error handling | [`OrderApiTest.missingQuantityIsABadRequest`](order-service/src/test/java/com/example/store/order/api/OrderApiTest.java#L49), [`unknownSkuIsNotFound`](order-service/src/test/java/com/example/store/order/api/OrderApiTest.java#L57) |
| 4 | REST APIs / resilience | [`OrderApiTest.createReturns201WithLocation`](order-service/src/test/java/com/example/store/order/api/OrderApiTest.java#L65), [`sameIdempotencyKeyReturnsTheSameOrder`](order-service/src/test/java/com/example/store/order/api/OrderApiTest.java#L75) |
| 5 | REST APIs / performance | [`OrderApiTest.catalogIsCalledOnceForAllLines`](order-service/src/test/java/com/example/store/order/api/OrderApiTest.java#L92) |
| 6 | Spring IoC / DI / testing | [`OrderServiceTest.createUsesTheCatalogItWasGiven`](order-service/src/test/java/com/example/store/order/service/OrderServiceTest.java#L32) |
| 7 | Spring beans / concurrency | [`CartControllerTest.cartIsNotSharedBetweenSessions`](store-web/src/test/java/com/example/store/web/api/controller/CartControllerTest.java#L43) |
| 9 | Databases / JPA | [`OrderPersistenceTest.savedOrderCanBeReadBack`](order-service/src/test/java/com/example/store/order/repository/OrderPersistenceTest.java#L38), [`confirmationEndpointReturnsTheOrder`](order-service/src/test/java/com/example/store/order/repository/OrderPersistenceTest.java#L49) |

Once a test passes, be ready to explain the root cause and how else the code could be improved.
