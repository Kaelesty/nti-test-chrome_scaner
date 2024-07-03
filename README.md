# Chrome Scanner
## Описание
В рамках проекта реализовано два android-приложения:
- Сервер, способный сканировать, сохранять и восстанавливать файловую систему (кеш) браузера Chrome
- Клиент, управляющий сервером
<br><br>Создано в качестве тестового задания для Nti.team

## Стек
- UI: Jetpack Compose; Compose Navigation
- Сетевое взаимодействие: Ktor; Websockets
- DI: Hilt
- Асинхрнонность: Coroutines, Flow
- Хранение данных: Room(SqlLite); Jetpack Datastore

## Сборка & использование

### Сервер
0. Собрать модуль server с помощью Gradle
1. Запустить модуль в режиме отладки Android Studio или выполнить сборку apk-файла
- **ВАЖНО** Сервер запускаться на устройстве или эмуляторе с root-доступом, иначе сканирование будет завершаться с ошибкой из-за отсутстпия доступа к папке кеша Chrome.
При работе сервера приложение запросит root-доступ, его можно предоставить навсегда или на определенное время.
- **ВАЖНО 2** Root-сессия должна использовать глобальное пространство имен. Если для получения root-доступа используется Magisk, этого эффекта можно достичь установив настройку "Mount Namespace Module" в состояние "Global Namespace".
 <br> ![Снимок экрана 2024-07-03 224438](https://github.com/Kaelesty/nti-test-chrome_scaner/assets/74826130/01643eb9-d5d5-4ddb-be46-5a4f870af07c) <br>
2. Установить порт сервера и нажать "Start server"
  * В разделе "Memory Usage" отображаются данные об использовании памяти процессом. Эти данные также отправляются на клиент.
  * В разделе "Logs" выводится информация о состоянии сервера, происходящих сканированиях и восстановлениях. 


### Клиент
0. Собрать модуль client с помощью Gradle
1. Запустить модуль в режиме отладки Android Studio или выполнить сборку apk-файла
2. В меню "Config" установить IP-адрес (127.0.0.1, если сервер и клиент запущены на одном устройстве) и порт сервера, нажать "Save", затем "Reconnect" (после запуска сервера)
<br>![Снимок экрана 2024-07-03 225429](https://github.com/Kaelesty/nti-test-chrome_scaner/assets/74826130/772fa7db-6132-489b-8938-eee6c21d7387)
<br><br>
- По нажатию "Start scanning" запускается сканирование файловой системы на сервере
- В меню "Scan list" доступен список текущих сохраненных сканов, полученных от сервера
- В меню "Visualization" доступна визуализация дерева каталогов скана
<br>![Снимок экрана 2024-07-03 225613](https://github.com/Kaelesty/nti-test-chrome_scaner/assets/74826130/3d227e51-635c-4816-855f-a31b7b314b8b)
<br>
