# Please join and contribute!

# covid19app - server

Server for covid19app mobile app(s) helping us fight the covid19 virus. Build by humans. Build for humans.

This repository contains the server. The mobile app(s) are at [covid19app](https://github.com/covid19app/covid19app).


# Run and Test

Install java 11 (or newer).

Run with:

```bash
./mvnw spring-boot:run
```

Test with:

```bash
./mvnw test
```

Demo instance running in cloud is coming! Tomorrow?

BTW: New to maven, kotlin, spring boot, etc?
Just install [intellij idea](https://www.jetbrains.com/idea/download). Community edition is enough.
It has Java 11 bundled inside. Intellij helps a lot!
You can google the rest of the tech stack. It has great documentations.


# Architecture

Usual Spring Boot project with JPA. All events will be pushed to Kafka. From there they will go to analytics dashboard.
