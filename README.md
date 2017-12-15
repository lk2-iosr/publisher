# Publisher service [![Build Status](https://travis-ci.org/lk2-iosr/publisher.svg?branch=master)](https://travis-ci.org/lk2-iosr/publisher)

Service for consuming facebook posts from SQS queue, filtering them based on keywords and sending email notifications.
It also fetches post statistics from [stats](https://github.com/lk2-iosr/stats) service and sends them to selected email.

## Running application locally

### Instructions
1. Clone this repository: 
```git clone https://github.com/lk2-iosr/publisher```
2. Run 
```mvn clean install```. 
This will build `jar` file and create docker image ``lk2iosr/publisher:<TAG>``
3. Run 
```docker run --env-file <PATH_TO_FILE_WITH_ENVIRONMENT_VARIABLES> lk2iosr/publisher:<TAG>```

### Environment variables

**1. REQUIRED**
* `AWS_ACCESS_KEY` - AWS account id;
* `AWS_SECRET_KEY` - AWS account password;
* `AWS_REGION` - region in which queue was created;
* `AWS_QUEUE_NAME` - name of SQS queue from which messages will be consumed;
* `SPRING_MAIL_USERNAME` - gmail username;
* `SPRING_MAIL_PASSWORD` - gmail app password. This is not a regular password for email but app password that needs to 
be generated;
* `DESTINATION_MAIL` - email to which notifications will be sent.
* `REDIS_HOST` - redis db host


**2. OPTIONAL**
* `POST_FILTER_KEYWORD` - keyword which needs to be found in post message so as to send email notification, default 
value 'hello';
* `PUBLISH_STATS_INTERVAL_MINUTES` - time interval after which post statistics will be published, default value is 60;
* `REDIS_PORT` - redis db port, default value 6379;
* `REDIS_KEY` - redis set key, default value 'PostIds'.