## RabbitMQ Region-Based Messaging System

This repository contains two Spring Boot microservices that demonstrate region-based message routing using RabbitMQ topic exchanges.

- `middle02rabbit` — Order Service that publishes orders to RabbitMQ using region-specific routing keys.
- `middle02rabbitreceiver` — Notification Service that consumes orders from region queues and logs the payload.

The `docker-compose.yml` file runs a RabbitMQ instance with the management UI enabled.

---

### How to Run

1. **Start RabbitMQ**
   - `docker-compose up -d`
   - Management UI: http://localhost:15672 (guest/guest)

2. **Run Order Service**
   - `cd middle02rabbit`
   - `./gradlew bootRun`
   - Service listens on `http://localhost:8000`

3. **Run Notification Service**
   - `cd middle02rabbitreceiver`
   - `./gradlew bootRun`
   - Service listens on `http://localhost:8001`

4. **Send Test Orders**
   - Import `RabbitMQRegionMessaging.postman_collection.json` into Postman.
   - Execute requests to send orders to different regions.

---

### Technologies Used

- Spring Boot 3 (Web, AMQP)
- Spring RabbitMQ
- RabbitMQ (Topic Exchange)
- Lombok
- Gradle
- Docker / Docker Compose

---

### Key Concepts

- **Topic Exchange** — Routes messages to queues based on routing keys with pattern matching (e.g., `order.almaty`, `order.#`).
- **Routing Keys** — Determined by the order's region; controls which queues receive the message.
- **Durable Queues** — Persisted queues for Almaty, Astana, Shymkent, plus a catch-all queue.
- **JSON Message Conversion** — `Jackson2JsonMessageConverter` ensures POJOs are serialized/deserialized automatically.
- **Microservice Communication** — Services are decoupled and communicate via RabbitMQ.

---

### Implementation Logic

#### Order Service (`middle02rabbit`)
- `OrderDTO` defines properties for restaurant, courier, foods, and status.
- `OrderPublisherService` builds routing keys (`order.{region}`) and publishes to `order-topic-exchange`.
- `OrderController` exposes a `POST /orders?region=...` endpoint to submit orders destined for a specific region.

#### Notification Service (`middle02rabbitreceiver`)
- Configures topic exchange, region-specific queues, bindings, and a catch-all queue.
- `OrderNotificationListener` has dedicated listeners for Almaty, Astana, Shymkent, and a logged catch-all handler.
- Logs include queue label, routing key, and order content.

---

### Architecture Overview

```
+-------------------+          Routing Key: order.almaty          +------------------------+
| Order Service     |-------------------------------------------->| Queue: order.almaty    |
| (REST + Publisher)|                                            +------------------------+
| POST /orders      |----> Topic Exchange: order-topic-exchange --| Queue: order.astana    |
|  region=astana    |      (RabbitMQ)                             +------------------------+
|  body: OrderDTO   |-------------------------------------------->| Queue: order.shymkent  |
+-------------------+                                            +------------------------+
                                                                   \--> Queue: order.all (order.#)
                                                                      ^         ^
                                                                      |         |
                                                            Notification Service Listeners
```

1. REST request hits Order Service and is converted into an `OrderDTO`.
2. Publisher sends the DTO to RabbitMQ using `order.{region}`.
3. Topic exchange routes the message to matching queues.
4. Notification Service listens to queue-specific messages and logs them.

---

### Additional Notes

- RabbitMQ must be running before services start, otherwise connection retries will appear in logs.
- Modify queue bindings or add new regions in `RabbitConfig` (Notification Service).
- Queue names, exchange, and routing patterns are configurable via `application.properties`.
