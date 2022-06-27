![](https://img.shields.io/badge/Maven-_3-red)
![](https://img.shields.io/badge/Java-_11-orange)
![](https://img.shields.io/badge/SpringBoot-_2-darkorange)
![](https://img.shields.io/badge/SpringDataJPA-blue)
![](https://img.shields.io/badge/PostgerSQL-_13.2-blue)
![](https://img.shields.io/badge/Checkstyle-lightgrey)

## Описание
Чат на REST API, реализованный с использованием Spring Boot.
В системе используется авторизация по токену. При регистрации пользователю выдается токен и с использованием этого токена пользователь может отправлять запросы в систему.

## Технологии
* Java 11
* PostgreSQL 13.2
* Hibernate
* Spring Boot 2
* Spring Data JPA
* Spring Security
* JWT

## Использование

### Пользователи

`GET /person/sign-up` - регистрация нового пользователя

`POST person/login` - получить токен пользователя (в теле передает username и token созданного пользователя)

`GET /person/all` - получить всех пользователей

`GET /person/{id}` - получить пользователя по его id

`POST /person/` - создать пользователя

`PUT /person/` - обновить данные пользователя или создать нового

`PATCH /person/` - обновить данные существующего пользователя

`PUT /person/{personId}/role` - Изменяет роль У пользователя с ROLE_USER на ROLE_ADMIN, если роль - Admin, то наоборот

`DELETE /person/{id}` - удалить пользователя по его id

### Комнаты

`GET /room/` - получить все комнаты

`GET /room/{id}` - получить комнату по её id

`POST /room/` - создать новую комнату

`PUT /room/` - обновить данные комнаты или создать новую

`PATCH /room/patch` - обновить данные существующей комнаты

`DELETE /room/{id}` - удалить комнату по её id

`POST /room/{roomId}/{personId}` - добавление существующего пользователя в комнату

`PUT /room/{roomId}/{personId}` - сделать пользователя в комнате админом

### Сообщения

`GET /message/` - получить все сообщения всех участников со всех комнат

`GET /message/room/{roomId}` - получить все сообщения в определенной комнате

`GET /message/{id}` - получить сообщение по его id

`POST /message/{roomId}/{personId}` - создать сообщение от пользователя в комнате

`PUT /message/{roomId}/{personId}` - обновить данные сообщения или создать новое

`PATCH /message/patch` - обновить данные существующего сообщения

`DELETE /message/{id}` - удалить сообщение по его id

## Примеры работы запросов

### Получение всех пользователей
![chat_1](/img/GetAll_r.png)

### Вход зарегистрированного пользователя с получением им аутентификационного токена
![chat_2](/img/login_r.png)

### Создание нового сообщения от пользователя
![chat_3](/img/new_message_r.png)

### Пример некорректного запроса и ответ сервера в виде сообщения в формате JSON
![chat_4](/img/error_patch_r.png)

### Попытка зарегистрировать пользователя с существующим логином и ответ сервера в формате JSON
![chat_5](/img/sign_up_error_r.png)
