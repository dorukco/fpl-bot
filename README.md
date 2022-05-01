# FPL Bot

This repository contains the code of a bot which pushes live EPL updates to a telegram chat.

## Preparation

* Create your telegram bot by using the [BotFather](https://core.telegram.org/bots#6-botfather) and get your token.
* Create a group and add your telegram bot to it.
* Send a message to the chat and get your chat id by visiting this URL https://api.telegram.org/bot<TOKEN>/getUpdates.
* Set your environment variables by executing the following commands.
```
export TELEGRAM_CHAT_IDS='<CHAT_ID>'
export TELEGRAM_TOKEN=<TOKEN>
```

## Running the application

* Run `docker-compose up --build -d` to run the application.
* Run `docker-compose stop` to stop the application.