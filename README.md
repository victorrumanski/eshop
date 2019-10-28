# eshop
This project tries to build an e-commerce using microservices separated by Bounded Contexts

Inter microservice notification is via kafka

Each microservice has its own Relational DB.

The complex PlaceOrder workflow is handled by Zeebe BPMN

I try to use DDD where it makes sense

I tried to use a simplified CQRS + Event Sourcing, so every meaningful event is saved 


There are lots of events on this project, they fall in two categories: 

Domain Events: Those that are important for the current microservice. ie: Basket Item Added

Integration Events: Those domain events that other microservices are interested. ie: Product Price Changed. This event should be propagated to other microservices that rely on the current price of a product.

![Alt text](place-order.png?raw=true "Optional Title")

PS:

I did not use Kafka-Spring @s on purpose

I did not use Zeebe-Spring @s on purpose

I did not use Axon on purpose

I did not use Avro Schema Registry on purpose



### Components
Kafka, Zookeper: https://tecadmin.net/install-apache-kafka-ubuntu/

Zeebe: https://docs.zeebe.io/getting-started/tutorial-setup.html

Postgres

Spring Boot

