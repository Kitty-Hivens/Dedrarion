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