# Getting Started

### How to boot this application

Step 1: You need to setup an OpenAI API key in the `application.yml` file.
Follow this guide to get an API key from
OpenAI: [Create and export an API key](https://platform.openai.com/docs/quickstart#create-and-export-an-api-key)

```yaml
spring:
  application:
    name: spring-ai-demo
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
```

Step 2: Uncomment the code to use the OpenAI API in the `AiChatController.kt` file

```java
        // uncomment the following line to test
        /*

        // Ask anything
        response = simplePrompt(query)

        // Ask about top 10 videos on youtube about a topic, the response is in a structured format
        response = structuredOutput(query)

        // stuffing the prompt with the provided data
        response = simplePromptStuffing(query)
        response = promptStuffingWithSimpleVectorStore(query)
        response = promptStuffingWithPgVectorStore(query)

        // ask the MFV employees list
        response = functionCalling(query)

        * */
```

Step 3: Run docker-compose up

```shell
docker-compose up -d
```

Step 4: Run the application from the IDE or using the following command

```shell
./gradlew bootRun
```

Step 5: Open the browser and navigate to the following URL to use: [Demo chat](http://localhost:8080/chat)

```
http://localhost:8080/chat
```

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.0/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.0/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.0/reference/web/servlet.html)
* [OpenAI](https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
