# «Кредитный банк»

**Приложение для расчёта и выдачи кредитов**

[![Java](https://img.shields.io/badge/-Java%2017-F29111?style=for-the-badge&logo=java&logoColor=e38873)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/-Spring%20Boot%203.2-6AAD3D?style=for-the-badge&logo=spring-boot&logoColor=90fd87)](https://spring.io/projects/spring-boot)
[![Postgresql](https://img.shields.io/badge/-postgresql%20-31648C?style=for-the-badge&logo=postgresql&logoColor=FFFFFF)](https://www.postgresql.org/)
[![Hibernate](https://img.shields.io/badge/-Hibernate%206.4-B6A975?style=for-the-badge&logo=hibernate&logoColor=717c88)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/-Maven-7D2675?style=for-the-badge&logo=apache&logoColor=e38873)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
[![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)](https://editor-next.swagger.io/)
[![CI/CD](https://img.shields.io/badge/CI/CD-118249?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/features/actions)
[![Liquibase](https://img.shields.io/badge/Liquibase-2a62ff?style=for-the-badge&logo=liquibase&logoColor=white)](https://www.liquibase.com/)
[![JUnit](https://img.shields.io/badge/JUnit%205-6CA315?style=for-the-badge&logo=JUnit&logoColor=white)](https://junit.org/junit5/docs/current/user-guide/)
[![MapStruct](https://img.shields.io/badge/MapStruct-d23120?style=for-the-badge&logo=&logoColor=white)](https://mapstruct.org/)
[![LogBook](https://img.shields.io/badge/Logbook-ff6900?style=for-the-badge&logo=logbook&logoColor=white)](https://github.com/zalando/logbook)
![Lombok](https://img.shields.io/badge/Lombok-1.18.24-green?logo=java&logoColor=white)
![Feign](https://img.shields.io/badge/Feign-11.0-blue?logo=spring&logoColor=white)
![Java Mail](https://img.shields.io/badge/Java%20Mail-1.6.2-blue?logo=java&logoColor=white)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ValentinaBuddha_java-credit-bank&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=ValentinaBuddha_java-credit-bank)
[![codecov](https://codecov.io/gh/ValentinaBuddha/java-credit-bank/graph/badge.svg?token=TI11OA2PD2)](https://codecov.io/gh/ValentinaBuddha/java-credit-bank)


## Логика работы

1. Пользователь отправляет заявку на кредит.
2. МС Заявка осуществляет прескоринг прескорингзаявки и если прескоринг проходит, то заявка сохраняется в МС Сделка и отправляется в МС калькулятор.
3. МС Калькулятор возвращает через МС Заявку пользователю 4 предложения (сущность "LoanOffer") по кредиту с разными условиями (например без страховки, со страховкой, с зарплатным клиентом, со страховкой и зарплатным клиентом) или отказ.
4. Пользователь выбирает одно из предложений, отправляется запрос в МС Заявка, а оттуда в МС Сделка, где заявка на кредит и сам кредит сохраняются в базу.
5. МС Досье отправляет клиенту письмо с текстом "Ваша заявка предварительно одобрена, завершите оформление".
6. Клиент отправляет запрос в МС Сделка со всеми своими полными данными о работодателе и прописке.
   Происходит скоринг данных в МС Калькулятор, МС Калькулятор рассчитывает все данные по кредиту (ПСК, график платежей и тд), МС Сделка сохраняет обновленную заявку и сущность кредит сделанную на основе CreditDto полученного из КК со статусом CALCULATED в БД.
7. После валидации МС Досье отправляет письмо на почту клиенту с одобрением или отказом.
   Если кредит одобрен, то в письме присутствует ссылка на запрос "Сформировать документы"
8. Клиент отправляет запрос на формирование документов в МС Досье, МС Досье отправляет клиенту на почту документы для подписания и ссылку на запрос на согласие с условиями.
9. Клиент может отказаться от условий или согласиться.
   Если согласился - МС Досье на почту отправляет код и ссылку на подписание документов, куда клиент должен отправить полученный код в МС Сделка.
10. Если полученный код совпадает с отправленным, МС Сделка выдает кредит (меняет статус сущности "Кредит" на ISSUED, а статус заявки на CREDIT_ISSUED)

### Архитектура

![Architecture](documents/arch.png)

### Sequence диаграмма

![Sequence-diagram](documents/seq.png)

### Business flow

![Business-flow](documents/bf.png)

Цвета:
- Оранжевый: Application
- Голубой: Deal + Database
- Фиолетовый: Conveyor
- Зеленый: Dossier
- Красный: конец флоу

Типы действий:
- Иконка «человек» сверху слева: пользовательское действие
- Иконка «зубчатое колесо» сверху слева: действие системы
- Иконка «молния»: ошибка
- Иконка «прямоугольник с горизонтальными полосками»: выбор пользователя
- Иконка «конверт»: асинхронная отправка email-сообщения на почту

## Запуск приложения

1. Прописать логин и пароль для почты для отправки писем в файле application-dev.yml;
2. Собрать все сервисы в пакеты .jar (например, через вкладку Maven: Maven -> Lifecycle -> application -> package);
3. Для локального запуска бекенд-приложения с БД установите и откройте программу
   [Docker Desktop](https://www.docker.com/products/docker-desktop/).
   <br>Затем в командной строке cmd выполните следующие команды

```shell
   git clone git@github.com:ValentinaBuddha/java-credit-bank.git
   cd ~/java-credit-bank  
   mvn clean package
   git checkout main
   docker-compose up -d
   ```

4. [Swagger](http://localhost:8085/swagger-ui/index.html#/)


## Диаграмма базы данных

![Database schema](documents/db.png)

Цвета:
- Зеленый: сущность реализована в виде отдельного отношения.
- Желтый: сущность реализована в виде поля типа jsonb.
- Синий: сущность реализована в виде java enum, сохранена в БД как varchar.