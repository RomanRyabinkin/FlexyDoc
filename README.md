FlexyDoc: Android-приложение по работе с PDF

FlexyDoc — это мобильное Android-клиент для просмотра, редактирования и конвертации PDF-документов. Приложение поддерживает:

Конвертацию PDF в различные форматы:

DOCX: извлечение текста и вставка изображений страниц для сохранения макета.

XLSX: экспорт каждой строки текста в отдельную строку Excel.

JPG/PNG: рендеринг страниц PDF в изображения с настраиваемым DPI.

Просмотр и редактирование PDF: открытие, аннотации, выделение, зачёркивание текста и печать с помощью встроенного PDF-вьювера.

Настройки приложения:

Выбор языка интерфейса (Русский/Английский).

Выбор темы (Светлая/Тёмная/System Default).

Управление файлами: сохранение результатов конвертации в папку «Загрузки» через MediaStore.

Мультиязычность: все строки в ресурсах на русском и английском, поддержка смены локали на ходу.

Структура проекта

ui/ — экраны на Jetpack Compose и навигация:

AppRoot, HomeScreen, ActionsScreen, FormatSelectionScreen, FilePickerScreen, PdfConvertScreen, PdfEditorScreen, SettingsScreen, AboutScreen.

converter/ — интерфейс PdfConverter и реализация RealPdfConverter (PDFBox + Apache POI).

util/ — утилиты для копирования URI в кэш, получения имени файла, сохранения в «Загрузки».

components/ — переиспользуемые Compose-компоненты: FilePickerButton, AppDrawer, TopBar.

core/ — определение маршрутов (класс-сущность Screen).

data/ — репозиторий настроек (DataStore Preferences) для языка и темы.

Технологии и библиотеки

Kotlin, Coroutines

Jetpack Compose

Navigation Compose

PDFBox Android (парсинг PDF)

Apache POI (XWPF, XSSF)

PdfiumCore (рендеринг страниц в Bitmap)

DataStore Preferences

AndroidX Material3

Multidex support

Быстрый старт

Требования

Android Studio Arctic Fox или новее

JDK 11

Эмулятор или устройство с Android 8.0 (API 26+)

Сборка и запуск

Клонируйте репозиторий:

git clone https://github.com/yourusername/FlexyDoc.git
cd FlexyDoc

Откройте проект в Android Studio.

Дождитесь синхронизации Gradle и выполните сборку.

Запустите приложение на эмуляторе или реальном устройстве.

Тестирование

Unit tests: ./gradlew test

Instrumented tests: ./gradlew connectedAndroidTest

Как пользоваться

Домашний экран: выбрать категорию (PDF, Word, Image).

Экран действий: выбрать операцию (Конвертация, Редактирование, Аннотации и т.д.).

Выбор формата (при конвертации): указать целевой формат.

Выбор файла: через системный диалог выбрать PDF.

Конвертация: нажать «Начать конвертацию», наблюдать прогресс и получить уведомление о сохранении.

Просмотр/редактирование: открыть PDF для аннотаций, выделений или печати.

Лицензия

MIT License (см. файл LICENSE)
