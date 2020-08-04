# Mirror Bot

Simple VK chatbot that that mirrors user's text messages.

### Setting up

Fill in all required properties in `src/main/resoures/application.properties` file.

- `vk.api.version` - **5.80** by default, you shouldn't change it ([API versions](https://vk.com/dev/versions));
- `vk.api.token` - community access token with **community managing** and **community messages** rights ([Getting a Token](https://vk.com/dev/access_token));
- `vk.api.groupId` - id of your community;
- `bot.host` - url endpoint which bot going to be listen;
- `bot.title` - bot title.

You can specify additional Spring Boot Application properties if you need, e.g. `spring.port`.

### Running

If you are done with properties, simply run Bot Application via Gradle:

``./gradlew :bootRun``