<<<<<<< HEAD
# Dedrarion Adventures

[![License: LGPL v3](https://img.shields.io/badge/License-LGPL_v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

## 🌍 О проекте | About | プロジェクトについて

**Dedrarion Adventures** — это коллекция модов для Minecraft Forge, направленная на расширение и улучшение различных аспектов игры. Проект состоит из основного API-модуля (`dedrarion`) и контентных модулей (`hdu` - Dedrarion Underground).

**Dedrarion Adventures** is a collection of mods for Minecraft Forge aimed at expanding and improving various aspects of the game. The project consists of a core API module (`dedrarion`) and content modules (`hdu` - Dedrarion Underground).

**Dedrarion Adventures** は、ゲームの様々な側面を拡張・改善することを目的としたMinecraft Forge向けのMODコレクションです。プロジェクトは、コアAPIモジュール (`dedrarion`) とコンテンツモジュール (`hdu` - Dedrarion Underground) で構成されています。

## 🌳 Структура | Structure | 構造

* `/dedrarion`: Ядро API, предоставляющее общие функции для других модулей.
* `/hdu`: Модуль **Dedrarion Underground**, добавляющий контент, связанный с подземельями и пещерами.

* `/dedrarion`: The Core API, providing common functionalities for other modules.
* `/hdu`: The **Dedrarion Underground** module, adding content related to dungeons and caves.

* `/dedrarion`: コアAPI。他のモジュールに共通機能を提供します。
* `/hdu`: **Dedrarion Underground** モジュール。ダンジョンや洞窟に関連するコンテンツを追加します。

## ✨ Модули | Modules | モジュール

### 1. Dedrarion Core API (`dedrarion`)
* Предоставляет базовые утилиты, эффекты и API для других модулей. См. [README в /dedrarion](dedrarion/README.md).
* Provides base utilities, effects, and APIs for other modules. See [README in /dedrarion](dedrarion/README.md).
* 他のモジュール用の基本ユーティリティ、エフェクト、APIを提供します。[ /dedrarion内のREADME](dedrarion/README.md) を参照してください。

### 2. Dedrarion Underground (`hdu`)
* Перерабатывает подземелья, добавляет руды (Эфириум, Рубин, Эфторит), блоки (Эфторитовая Наковальня), предметы (Магический Детектор, Мнемозина и Алета, Тетралин), существ и многое другое. См. [README в /hdu](hdu/README.md).
* Overhauls dungeons, adds ores (Ethereum, Ruby, Eftorit), blocks (Eftorit Forge), items (Magical Detector, Mnemosyne & Aleta, Tetralin), creatures, and more. See [README in /hdu](hdu/README.md).
* ダンジョンを刷新し、鉱石（エーテリウム、ルビー、エフトリット）、ブロック（エフトリット炉）、アイテム（魔法探知機、ムネモシュネ＆アレタ、テトラリン）、クリーチャーなどを追加します。[ /hdu内のREADME](hdu/README.md) を参照してください。

---

## ⚙️ Установка | Installation | インストール

### 🇷🇺
1.  📥 Установите **Minecraft Forge** (версия указана в `gradle.properties`).
2.  📂 Скачайте `.jar` файлы для **Dedrarion Core API** (`dedrarion`) и **Dedrarion Underground** (`hdu`).
3.  📂 Переместите **ОБА** `.jar` файла в папку `mods` вашего Minecraft.
4.  🚀 Запустите игру!

### 🇺🇸
1.  📥 Install **Minecraft Forge** (version specified in `gradle.properties`).
2.  📂 Download the `.jar` files for both **Dedrarion Core API** (`dedrarion`) and **Dedrarion Underground** (`hdu`).
3.  📂 Move **BOTH** `.jar` files into your Minecraft `mods` folder.
4.  🚀 Launch the game!

### 🇯🇵
1.  📥 **Minecraft Forge** をインストールします (バージョンは `gradle.properties` に記載)。
2.  📂 **Dedrarion Core API** (`dedrarion`) と **Dedrarion Underground** (`hdu`) の両方の `.jar` ファイルをダウンロードします。
3.  📂 **両方** の `.jar` ファイルをMinecraftの `mods` フォルダに移動します。
4.  🚀 ゲームを起動してください！

---

## 🤝 Участие | Contributing | 貢献

* Сообщения об ошибках и предложения приветствуются! Используйте раздел "Issues" на GitHub.
* Bug reports and suggestions are welcome! Please use the GitHub Issues section.
* バグレポートや提案は大歓迎です！GitHubのIssuesセクションをご利用ください。

---

## 📜 Лицензия | License | ライセンス

Весь проект **Dedrarion Adventures** распространяется под лицензией **GNU LGPL v3**. Подробности см. в файле [LICENSE](LICENSE).

The entire **Dedrarion Adventures** project is distributed under the **GNU LGPL v3** license. See the [LICENSE](LICENSE) file for details.

**Dedrarion Adventures** プロジェクト全体は **GNU LGPL v3** ライセンスの下で配布されています。詳細については [LICENSE](LICENSE) ファイルを参照してください。

---

## 👨‍💻 Авторы | Authors | 作者

* **Dedrarion Core API:** Haru (Hivens)
* **Dedrarion Underground:** Haru, Afanet (Hivens)
* **Благодарности:** logerions, thisgoi, Maris260 (из старого README)
=======
# Dedrarion Adventures - Core API

[![License: LGPL v3](https://img.shields.io/badge/License-LGPL_v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

## 📜 Описание | Description | 説明

Core API модуль для набора модов **Dedrarion Adventures**. Предоставляет общие утилиты, классы и интерфейсы для других модулей Dedrarion.

This is the Core API module for the **Dedrarion Adventures** mod collection. It provides common utilities, classes, and interfaces for other Dedrarion modules.

**Dedrarion Adventures** modコレクションのコアAPIモジュールです。他のDedrarionモジュールに共通のユーティリティ、クラス、インターフェースを提供します。

## ⚙️ Основные возможности | Features | 特徴

* **API Утилиты:** Включает базовые классы для предметов с подсказками (`TooltipItem`, `TooltipFuelItem`), тиров инструментов (`ModTiers`), тэгов (`DedrarionTags`) и эффектов (`ModEffects`).
* **Базовые Эффекты:** Регистрирует общие эффекты, такие как Кровотечение (`BleedingEffect`), Снижение брони (`ARMOR_REDUCTION`), Фазовая энергия (`PHASE_ENERGY`) и Резонанс (`RESONANCE`).
* **Модульность:** Служит основой для других модов серии Dedrarion, обеспечивая общую базу кода.

* **API Utilities:** Includes base classes for tooltip items (`TooltipItem`, `TooltipFuelItem`), tool tiers (`ModTiers`), tags (`DedrarionTags`), and effects (`ModEffects`).
* **Base Effects:** Registers common effects like Bleeding (`BleedingEffect`), Armor Reduction (`ARMOR_REDUCTION`), Phase Energy (`PHASE_ENERGY`), and Resonance (`RESONANCE`).
* **Modularity:** Serves as the foundation for other mods in the Dedrarion series, providing a common codebase.

* **APIユーティリティ:** ツールチップ付きアイテム (`TooltipItem`, `TooltipFuelItem`)、ツールティア (`ModTiers`)、タグ (`DedrarionTags`)、エフェクト (`ModEffects`) の基本クラスを含みます。
* **基本エフェクト:** 出血 (`BleedingEffect`)、アーマー低下 (`ARMOR_REDUCTION`)、フェーズエネルギー (`PHASE_ENERGY`)、共鳴 (`RESONANCE`) などの共通エフェクトを登録します。
* **モジュール性:** Dedrarionシリーズの他のMODの基盤として機能し、共通のコードベースを提供します。

## 📦 Зависимости | Dependencies | 依存関係

* Minecraft Forge (Версия указана в `build.gradle`)
* Minecraft (Версия указана в `build.gradle`)

* Minecraft Forge (Version specified in `build.gradle`)
* Minecraft (Version specified in `build.gradle`)

* Minecraft Forge (バージョンは `build.gradle` に記載)
* Minecraft (バージョンは `build.gradle` に記載)

## 📜 Лицензия | License | ライセンス

Этот модуль распространяется под лицензией **GNU LGPL v3**.

This module is distributed under the **GNU LGPL v3** license.

このモジュールは **GNU LGPL v3** ライセンスの下で配布されています。

## 👨‍💻 Авторы | Authors | 作者

* Haru (Hivens)
>>>>>>> f4e097affa61a92d11795962e7600ce18a8ca266
