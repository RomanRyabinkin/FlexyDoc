# FlexyDoc: Android PDF Converter & Editor

## 🇬🇧 English

> **Default branch**: `master` (all actual code and documentation is in the `master` branch).

**FlexyDoc** is an Android mobile client for viewing, editing, and converting PDF documents. The app supports:

* **PDF Conversion** into multiple formats:

  * **DOCX**: extracts editable text and embeds page images to preserve layout
  * **XLSX**: exports each line of text into a separate Excel row
  * **JPG/PNG**: renders PDF pages into images with configurable DPI
* **PDF Viewing & Editing**: open, annotate, highlight, strike-through text and print using built-in PDF viewer
* **App Settings**:

  * Language selection (English/Russian)
  * Theme selection (Light/Dark/System Default)
* **File Management**: save converted files to the device’s Downloads folder via MediaStore API
* **Internationalization**: all UI strings available in Russian and English, runtime locale switching

---

### Project Structure

* **ui/** — Jetpack Compose screens and navigation:

  * `AppRoot`, `HomeScreen`, `ActionsScreen`, `FormatSelectionScreen`, `FilePickerScreen`, `PdfConvertScreen`, `PdfEditorScreen`, `SettingsScreen`, `AboutScreen`
* **converter/** — `PdfConverter` interface and `RealPdfConverter` implementation (PDFBox + Apache POI)
* **util/** — utilities for URI copying, filename retrieval, saving to Downloads
* **components/** — reusable Compose components: `FilePickerButton`, `AppDrawer`, `TopBar`
* **core/** — navigation routes definition (`Screen` sealed class)
* **data/** — settings repository (DataStore Preferences) for language and theme

---

### Technologies & Libraries

* Kotlin & Coroutines
* Jetpack Compose & Material3
* Navigation-Compose
* PDFBox Android (PDF parsing)
* Apache POI (XWPF, XSSF)
* PdfiumCore (PDF ➔ Bitmap rendering)
* DataStore Preferences
* AndroidX Material3
* Multidex support

---

### Quick Start

**Requirements:**

* Android Studio Arctic Fox or later
* JDK 11
* Emulator or device with Android 8.0 (API 26+)

**Build & Run:**

```bash
git clone https://github.com/yourusername/FlexyDoc.git
cd FlexyDoc
```

1. Open the project in Android Studio
2. Sync Gradle and build
3. Run on emulator or physical device

**Testing:**

* Unit tests: `./gradlew test`
* Instrumented tests: `./gradlew connectedAndroidTest`

---

## 🇷🇺 Русский

> **Ветка по умолчанию**: `master` (весь актуальный код и документация находятся в ветке `master`).

**FlexyDoc** — это мобильный Android-клиент для просмотра, редактирования и конвертации PDF-документов. Приложение поддерживает:

* **Конвертацию PDF** в различные форматы:

  * **DOCX**: извлечение текста и вставка изображений страниц для сохранения макета
  * **XLSX**: экспорт каждой строки текста в отдельную строку Excel
  * **JPG/PNG**: рендеринг страниц PDF в изображения с настраиваемым DPI
* **Просмотр и редактирование PDF**: открытие, аннотации, выделение, зачёркивание текста и печать с помощью встроенного PDF-вьювера
* **Настройки приложения**:

  * Выбор языка интерфейса (Русский/Английский)
  * Выбор темы (Светлая/Тёмная/System Default)
* **Управление файлами**: сохранение результатов конвертации в папку «Загрузки» через MediaStore API
* **Мультиязычность**: все строки в ресурсах на русском и английском, поддержка смены локали на ходу

---

## Структура проекта

* **ui/** — экраны на Jetpack Compose и навигация:

  * `AppRoot`, `HomeScreen`, `ActionsScreen`, `FormatSelectionScreen`, `FilePickerScreen`, `PdfConvertScreen`, `PdfEditorScreen`, `SettingsScreen`, `AboutScreen`
* **converter/** — интерфейс `PdfConverter` и реализация `RealPdfConverter` (PDFBox + Apache POI)
* **util/** — утилиты для копирования URI в кэш, получения имени файла и сохранения в «Загрузки»
* **components/** — переиспользуемые Compose-компоненты: `FilePickerButton`, `AppDrawer`, `TopBar`
* **core/** — определение маршрутов (sealed-класс `Screen`)
* **data/** — репозиторий настроек (DataStore Preferences) для языка и темы

---

## Технологии и библиотеки

* Kotlin, Coroutines
* Jetpack Compose & Material3
* Navigation-Compose
* PDFBox Android (парсинг PDF)
* Apache POI (XWPF, XSSF)
* PdfiumCore (рендеринг PDF в Bitmap)
* DataStore Preferences
* AndroidX Material3
* Multidex support

---

## Быстрый старт

### Требования

* Android Studio Arctic Fox или новее
* JDK 11
* Эмулятор или устройство с Android 8.0 (API 26+)

### Сборка и запуск

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/yourusername/FlexyDoc.git
   cd FlexyDoc
   ```
2. Откройте проект в Android Studio.
3. Дождитесь синхронизации Gradle и выполните сборку.
4. Запустите приложение на эмуляторе или реальном устройстве.

### Тестирование

* **Unit tests**: `./gradlew test`
* **Instrumented tests**: `./gradlew connectedAndroidTest`

---

## Как пользоваться

1. **Домашний экран**: выбрать категорию (PDF, Word, Image)
2. **Экран действий**: выбрать операцию (Конвертация, Редактирование, Аннотации и т.д.)
3. **Выбор формата** (при конвертации): указать целевой формат
4. **Выбор файла**: через системный диалог выбрать PDF
5. **Конвертация**: нажать «Начать конвертацию», наблюдать прогресс и получить уведомление о сохранении
6. **Просмотр/редактирование**: открыть PDF для аннотаций, выделений или печати

---


## Лицензия

MIT License (см. файл LICENSE)

