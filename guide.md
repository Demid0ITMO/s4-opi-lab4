# Как запустить мониторинг приложения на удаленном сервере

## Настройка и установка wildfly
- Скачать zip-ку wildfly с официального сайта и распаковать // появится папка ```<WILDFLY-DIRNAME>```

- поменять порты в ```<WILDFLY-DIRNAME>/standalone/configuration/standalone.xml``` //  ```<HTTP-PORT>``` ```<MANAGEMENT-HTTP-PORT>```

- закинуть war с лабой в ```<WILDFLY-DIRNAME>/standalone/deployments/```

- заслать ```<WILDFLY-DIRNAME>``` на гелиос
  ```scp -P 2222 -r <WILDFLY-DIRNAME> sXXXXXX@se.ifmo.ru:~/```

- заходим на гелиос
  ```ssh -p 2222 sXXXXXX@se.ifmo.ru ```
- добавляем пользователя ``` ./<WILDFY-DIRNAME>/bin/add_user.sh ``` нам нужен ```managment user```, вводим ```<WILDFLY-LOGIN>``` ```<WILDFLY-PASSWORD>```

## Проброс портов и запуск лабы
терминал №1 (написать команду и не закрывать)
- ```sshpass -p <HELIOS-PASSWORD> ssh -L localhost:<HTTP-PORT>:127.0.0.1:<HTTP-PORT> sXXXXXX@se.ifmo.ru -p 2222 -N```

терминал №2 (здесь пробрасываем порт и, не выходя с гелиоса, запускаем лабу, как запустили тоже не закрывать)
- ```sshpass -p <HELIOS-PASSWORD> ssh -L localhost:<MANAGEMENT-HTTP-PORT>:127.0.0.1:<MANAGEMENT-HTTP-PORT> sXXXXXX@se.ifmo.ru -p 2222```
- ```./<WILDFLY-DIRNAME>/bin/standalone.sh```

## Мониторинг и профилирование
### JCONSOLE

Открыть jconsole через терминал (wildfly прописали скрипт чтобы с их продуктом jconsole работал норм)


```./<WILDFLY-DIRNAME>/bin/jconsole.sh```

Подключение (выбираем Remote process):

Connection: ```service:jmx:remote+http://localhost:<MANAGEMENT-HTTP-PORT>```

Username: ```<WILDFLY-LOGIN>```

Password: ```<WILDFLY-PASSWORD>```

### VISUALVM
Открыть visualvm через терминал
  
```visualvm --cp:a <WILDFLY-DIRNAME>/bin/client/jboss-client.jar```

Подключение (Add JMX Connection):

Connection: ```service:jmx:http-remoting-jmx://localhost:<MANAGEMENT-HTTP-PORT>```

Username: ```<WILDFLY-LOGIN>```

Password: ```<WILDFLY-PASSWORD>```