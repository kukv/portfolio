# ポートフォリオ刷新 ① 基盤 実装計画

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 設計書 `docs/superpowers/specs/2026-10-09-portfolio-foundation-design.md` の ①-1〜①-6 を実装する。対象はコード整理、日英切り替え、配色と Expressive テーマ、Home の Bento 化、検索対策。

**Architecture:** 1 ページスクロール構成のまま、以下を行う。
- セクションの識別を enum にし、画面幅の判定を `compose-adaptive` に寄せる。
- 設定（テーマ・言語）は `AppViewModel` が `localStorage` と同期する。
- 言語切り替えは Compose Resources と公式の `navigator.languages` 上書き手法で行う。判定ロジックは純粋関数にして wasmJs のテストで検証する。

**Tech Stack:** Kotlin 2.4.20 / Compose Multiplatform 1.12.1 / material3 1.10.0-alpha05（Expressive）/ material3-adaptive 1.3.0-rc01 / Compose Resources / js・wasmJs ターゲット / spotless(ktlint)

---

## 共通ルール

- 作業ディレクトリはリポジトリのルート。コマンドはすべてルートから実行する。
- このリポジトリのパスには `github.com` が含まれるため、worktree のガードが複雑なシェルコマンドを拒否することがある。コマンドは 1 つずつ実行し、パスはルートからの相対パスで書く。パイプ、`while` ループ、`$PWD` や `$HOME` を使ったコマンドは避ける。
- コミットの前に必ず `./gradlew spotlessApply` を実行する。
- コミットメッセージの末尾に次の行を付ける（以下のコマンド例では省略している）。
  ```
  Co-Authored-By: Claude Opus 5.5 (1M context) <noreply@anthropic.com>
  ```
- テストは `src/webTest/kotlin/` に置き、`./gradlew wasmJsTest` で実行する。js のブラウザテストは Task 1 で無効化する。
- `import` は各コード例に書いてあるものを使う。書いていない場合は IDE の補完に任せず、既存ファイルの import を見て合わせる。
- 実装前に確認した事項:
  - `MaterialShapes.Cookie9Sided.toShape()`、`MotionScheme.expressive()`、`currentWindowAdaptiveInfoV2()` がコンパイルできる。
  - webMain に書いた `js("...")` が js と wasmJs の両方でコンパイルできる。
  - wasmJs のブラウザテストがローカルの Chrome で動く。

## ファイル構成

| ファイル | 役割 | 区分 |
|---|---|---|
| `build.gradle.kts` | js のブラウザテスト無効化、文言キーの一致チェックタスク | 変更 |
| `kotlin-js-store/yarn.lock` | テスト無効化に伴う lock の再生成 | 変更 |
| `src/webMain/kotlin/jp/kukv/portfolio/shared/i18n/AppLanguage.kt` | 言語 enum と言語の決定ロジック | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/shared/i18n/AppEnvironment.kt` | その場での言語切り替え（公式の回避策を閉じ込める） | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/shared/theme/DarkTheme.kt` | テーマの決定ロジック | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/shared/lib/Browser.kt` | localStorage、matchMedia、navigator、document の操作 | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/shared/layout/Section.kt` | セクション enum | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/shared/layout/LayoutSize.kt` | 画面幅の 3 区分 | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/app/DocumentMetadata.kt` | `<html lang>` と `document.title` の同期 | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/components/LanguageToggle.kt` | EN / JA の切り替えボタン | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/screens/about/SkillCategoryCard.kt` | Skills カード | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/screens/home/BentoTile.kt` | Bento タイルの共通部品（出現と押下の動き） | 新規 |
| `src/webMain/kotlin/jp/kukv/portfolio/screens/home/BentoTiles.kt` | 各タイル | 新規 |
| `src/webMain/composeResources/values/strings.xml` | 英語の文言 | 変更 |
| `src/webMain/composeResources/values-ja/strings.xml` | 日本語の文言 | 新規 |
| `src/webMain/resources/index.html` | 検索対策、静的な要約、ロケール上書きスクリプト | 変更 |
| `src/webMain/resources/styles.css` | 静的な要約のスタイル | 変更 |
| `src/webMain/resources/og-image.png` | 共有用画像 | 新規 |
| `src/webTest/kotlin/jp/kukv/portfolio/shared/i18n/AppLanguageTest.kt` | 言語の決定のテスト | 新規 |
| `src/webTest/kotlin/jp/kukv/portfolio/shared/theme/DarkThemeTest.kt` | テーマの決定のテスト | 新規 |
| `app/App.kt`、`app/AppViewModel.kt`、`app/Theme.kt` | 設定の保持、環境の組み立て | 変更 |
| `components/Header.kt`、`components/NavigationDrawer.kt` | Section と文言リソース、トグル | 変更 |
| `shared/layout/Layout.kt` | Section 化 | 変更 |
| `shared/theme/Colors.kt`、`shared/theme/Fonts.kt` | 新配色、4 ウェイト | 変更 |
| `screens/home/*` | Bento 化（`Introduction.kt` は削除） | 変更・削除 |
| `screens/about/AboutScreen.kt`、`screens/showcase/ShowcaseScreen.kt`、`screens/contact/ContactScreen.kt` | 画面幅判定の置き換え、文言リソース化 | 変更 |
| `composeResources/font/NotoSansJP-{Thin,ExtraLight,Light,SemiBold,Black}.ttf` | 不要なウェイト | 削除 |

以降、`app/` や `screens/` などの短縮パスは `src/webMain/kotlin/jp/kukv/portfolio/` 配下を指す。

---

### Task 1: テスト基盤と言語の決定ロジック

**Files:**
- Modify: `build.gradle.kts:18-21`
- Modify: `kotlin-js-store/yarn.lock`（再生成）
- Create: `src/webMain/kotlin/jp/kukv/portfolio/shared/i18n/AppLanguage.kt`
- Test: `src/webTest/kotlin/jp/kukv/portfolio/shared/i18n/AppLanguageTest.kt`

- [ ] **Step 1: js のブラウザテストを無効化する**

背景: karma が、`build.gradle.kts` の yarn resolutions で固定した `minimatch` 9.x と非互換です。そのため `jsBrowserTest` は `TypeError: mm is not a function` で起動できません（確認済み）。テストは wasmJs で実行します。

`build.gradle.kts` の次の部分を:

```kotlin
    js(IR) {
        browser()
        binaries.executable()
    }
```

次のように変更する:

```kotlin
    js(IR) {
        browser {
            // karma が上記 resolutions で固定した minimatch 9.x と非互換で起動できないため、
            // js のブラウザテストは無効化し、テストは wasmJs で実行する。
            testTask { enabled = false }
        }
        binaries.executable()
    }
```

- [ ] **Step 2: yarn.lock を再生成する**

Run: `./gradlew kotlinUpgradeYarnLock --rerun-tasks`
Expected: `BUILD SUCCESSFUL`。`kotlin-js-store/yarn.lock` から karma 関連の依存が消える（差分は 700 行以上の削除）。

- [ ] **Step 3: 失敗するテストを書く**

`src/webTest/kotlin/jp/kukv/portfolio/shared/i18n/AppLanguageTest.kt`:

```kotlin
package jp.kukv.portfolio.shared.i18n

import kotlin.test.Test
import kotlin.test.assertEquals

class AppLanguageTest {
    @Test
    fun savedValueTakesPriorityOverBrowserLanguage() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = "ja", browserLanguage = "en-US"))
    }

    @Test
    fun japaneseBrowserResolvesToJapanese() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = null, browserLanguage = "ja-JP"))
    }

    @Test
    fun englishBrowserResolvesToEnglish() {
        assertEquals(AppLanguage.En, resolveLanguage(saved = null, browserLanguage = "en-US"))
    }

    @Test
    fun unsupportedBrowserLanguageFallsBackToEnglish() {
        assertEquals(AppLanguage.En, resolveLanguage(saved = null, browserLanguage = "fr-FR"))
    }

    @Test
    fun invalidSavedValueIsIgnored() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = "xx", browserLanguage = "ja"))
    }

    @Test
    fun missingValuesFallBackToEnglish() {
        assertEquals(AppLanguage.En, resolveLanguage(saved = null, browserLanguage = null))
    }

    @Test
    fun tagMatchingIgnoresCase() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = null, browserLanguage = "JA-jp"))
    }
}
```

- [ ] **Step 4: テストが失敗することを確認する**

Run: `./gradlew wasmJsTest`
Expected: FAIL（`Unresolved reference 'AppLanguage'` / `'resolveLanguage'` でコンパイルエラー）

- [ ] **Step 5: 実装する**

`src/webMain/kotlin/jp/kukv/portfolio/shared/i18n/AppLanguage.kt`:

```kotlin
package jp.kukv.portfolio.shared.i18n

enum class AppLanguage(
    val tag: String,
) {
    En("en"),
    Ja("ja"),
    ;

    companion object {
        fun fromTag(tag: String?): AppLanguage? {
            val primary = tag?.substringBefore('-')?.lowercase() ?: return null
            return entries.firstOrNull { it.tag == primary }
        }
    }
}

fun resolveLanguage(
    saved: String?,
    browserLanguage: String?,
): AppLanguage = AppLanguage.fromTag(saved) ?: AppLanguage.fromTag(browserLanguage) ?: AppLanguage.En
```

- [ ] **Step 6: テストが通ることを確認する**

Run: `./gradlew wasmJsTest`
Expected: `BUILD SUCCESSFUL`。`build/test-results/wasmJsBrowserTest/` に `AppLanguageTest` の 7 件が成功として記録されている。

- [ ] **Step 7: check 全体が通ることを確認する**

Run: `./gradlew check`
Expected: `BUILD SUCCESSFUL`、`:jsBrowserTest SKIPPED`

- [ ] **Step 8: コミット**

```bash
./gradlew spotlessApply
git add build.gradle.kts kotlin-js-store/yarn.lock src/webMain/kotlin/jp/kukv/portfolio/shared/i18n/AppLanguage.kt src/webTest
git commit -m "feat: 言語の決定ロジックを追加し、テストを wasmJs で実行する"
```

---

### Task 2: テーマの決定ロジック

**Files:**
- Create: `src/webMain/kotlin/jp/kukv/portfolio/shared/theme/DarkTheme.kt`
- Test: `src/webTest/kotlin/jp/kukv/portfolio/shared/theme/DarkThemeTest.kt`

- [ ] **Step 1: 失敗するテストを書く**

```kotlin
package jp.kukv.portfolio.shared.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DarkThemeTest {
    @Test
    fun savedDarkWins() {
        assertTrue(resolveDarkTheme(saved = "dark", systemPrefersDark = false))
    }

    @Test
    fun savedLightWins() {
        assertFalse(resolveDarkTheme(saved = "light", systemPrefersDark = true))
    }

    @Test
    fun followsSystemWhenNothingSaved() {
        assertTrue(resolveDarkTheme(saved = null, systemPrefersDark = true))
        assertFalse(resolveDarkTheme(saved = null, systemPrefersDark = false))
    }

    @Test
    fun followsSystemWhenSavedValueIsInvalid() {
        assertTrue(resolveDarkTheme(saved = "blue", systemPrefersDark = true))
    }

    @Test
    fun storageValueRoundTrips() {
        assertEquals("dark", themeStorageValue(isDark = true))
        assertEquals("light", themeStorageValue(isDark = false))
        assertTrue(resolveDarkTheme(themeStorageValue(isDark = true), systemPrefersDark = false))
        assertFalse(resolveDarkTheme(themeStorageValue(isDark = false), systemPrefersDark = true))
    }
}
```

- [ ] **Step 2: テストが失敗することを確認する**

Run: `./gradlew wasmJsTest`
Expected: FAIL（`Unresolved reference 'resolveDarkTheme'`）

- [ ] **Step 3: 実装する**

`src/webMain/kotlin/jp/kukv/portfolio/shared/theme/DarkTheme.kt`:

```kotlin
package jp.kukv.portfolio.shared.theme

private const val THEME_DARK = "dark"
private const val THEME_LIGHT = "light"

fun resolveDarkTheme(
    saved: String?,
    systemPrefersDark: Boolean,
): Boolean =
    when (saved) {
        THEME_DARK -> true
        THEME_LIGHT -> false
        else -> systemPrefersDark
    }

fun themeStorageValue(isDark: Boolean): String = if (isDark) THEME_DARK else THEME_LIGHT
```

- [ ] **Step 4: テストが通ることを確認する**

Run: `./gradlew wasmJsTest`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin/jp/kukv/portfolio/shared/theme/DarkTheme.kt src/webTest/kotlin/jp/kukv/portfolio/shared/theme
git commit -m "feat: テーマの決定ロジックを追加"
```

---

### Task 3: ブラウザ API と設定の保持

**Files:**
- Create: `src/webMain/kotlin/jp/kukv/portfolio/shared/lib/Browser.kt`
- Modify: `app/AppViewModel.kt`（全体を置き換え）

- [ ] **Step 1: ブラウザ API の関数を作る**

`src/webMain/kotlin/jp/kukv/portfolio/shared/lib/Browser.kt`:

```kotlin
@file:OptIn(ExperimentalWasmJsInterop::class)

package jp.kukv.portfolio.shared.lib

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

fun readStorage(key: String): String? = js("window.localStorage.getItem(key)")

fun writeStorage(
    key: String,
    value: String,
) {
    js("window.localStorage.setItem(key, value)")
}

fun prefersDarkColorScheme(): Boolean = js("window.matchMedia('(prefers-color-scheme: dark)').matches")

/** Task 7 で上書きする navigator.languages ではなく、ブラウザ本来の言語を返す。 */
fun browserLanguage(): String = js("window.navigator.language")

fun setDocumentLanguage(tag: String) {
    js("document.documentElement.lang = tag")
}

fun setDocumentTitle(title: String) {
    js("document.title = title")
}

fun removeElementById(id: String) {
    js("document.getElementById(id)?.remove()")
}
```

- [ ] **Step 2: AppViewModel に設定の保持を追加する**

`app/AppViewModel.kt` を次の内容に置き換える。`WindowSizeState` は Task 6 で削除するので、ここでは残す。

```kotlin
package jp.kukv.portfolio.app

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import jp.kukv.portfolio.shared.i18n.AppLanguage
import jp.kukv.portfolio.shared.i18n.resolveLanguage
import jp.kukv.portfolio.shared.lib.browserLanguage
import jp.kukv.portfolio.shared.lib.prefersDarkColorScheme
import jp.kukv.portfolio.shared.lib.readStorage
import jp.kukv.portfolio.shared.lib.writeStorage
import jp.kukv.portfolio.shared.theme.resolveDarkTheme
import jp.kukv.portfolio.shared.theme.themeStorageValue

enum class WindowSizeClass {
    Mobile,
    Tablet,
    Desktop,
}

@Stable
class WindowSizeState(windowSizeClass: WindowSizeClass) {
    var windowSizeClass by mutableStateOf(windowSizeClass)
        private set

    val isMobile: Boolean get() = windowSizeClass == WindowSizeClass.Mobile
    val isTablet: Boolean get() = windowSizeClass == WindowSizeClass.Tablet
    val isDesktop: Boolean get() = windowSizeClass == WindowSizeClass.Desktop

    internal fun update(sizeClass: WindowSizeClass) {
        windowSizeClass = sizeClass
    }
}

class AppViewModel : ViewModel() {
    var isDarkTheme by mutableStateOf(resolveDarkTheme(readStorage(THEME_KEY), prefersDarkColorScheme()))
        private set
    var language by mutableStateOf(resolveLanguage(readStorage(LANGUAGE_KEY), browserLanguage()))
        private set
    val windowSizeState = WindowSizeState(WindowSizeClass.Mobile)

    fun setDarkTheme(value: Boolean) {
        isDarkTheme = value
        writeStorage(THEME_KEY, themeStorageValue(value))
    }

    fun setLanguage(value: AppLanguage) {
        language = value
        writeStorage(LANGUAGE_KEY, value.tag)
    }

    fun updateWindowSize(sizeClass: WindowSizeClass) {
        windowSizeState.update(sizeClass)
    }

    private companion object {
        const val THEME_KEY = "theme"
        const val LANGUAGE_KEY = "language"
    }
}

val LocalAppViewModel = staticCompositionLocalOf<AppViewModel> { error("No AppViewModel provided") }
```

- [ ] **Step 3: ビルドを確認する**

Run: `./gradlew compileKotlinWasmJs compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 4: 動作を確認する**

Run: `./gradlew wasmJsBrowserDevelopmentRun`（起動後に表示される URL をブラウザで開く）

次を確認する:
- OS をダークモードにしてページを開くと、ダークテーマで表示される。
- テーマのボタンでライトに切り替えて再読み込みしても、ライトのまま。
- DevTools の Application → Local Storage に `theme` が保存されている。

確認できたらサーバーを止める。

- [ ] **Step 5: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin/jp/kukv/portfolio/shared/lib/Browser.kt src/webMain/kotlin/jp/kukv/portfolio/app/AppViewModel.kt
git commit -m "feat: テーマと言語の設定を OS とブラウザの設定から決め、localStorage に保存する"
```

---

### Task 4: 文言リソースとキー一致チェック

**Files:**
- Modify: `src/webMain/composeResources/values/strings.xml`（全体を置き換え）
- Create: `src/webMain/composeResources/values-ja/strings.xml`
- Modify: `build.gradle.kts`（末尾にタスクを追加）

- [ ] **Step 1: キー一致チェックタスクを追加する**

`build.gradle.kts` の末尾（`spotless { ... }` の後）に追加する:

```kotlin
// Compose Resources は訳し漏れがあると既定(英語)へ黙ってフォールバックするため、
// values と values-ja のキーが一致することを check で保証する。
val checkStringResources by tasks.registering {
    val base = layout.projectDirectory.file("src/webMain/composeResources/values/strings.xml")
    val ja = layout.projectDirectory.file("src/webMain/composeResources/values-ja/strings.xml")
    inputs.files(base, ja)
    doLast {
        val keyPattern = Regex("""<string name="([^"]+)"""")

        fun keys(file: File) = keyPattern.findAll(file.readText()).map { it.groupValues[1] }.toSet()

        val enKeys = keys(base.asFile)
        val jaKeys = keys(ja.asFile)
        val errors = (enKeys - jaKeys).map { "values-ja に無い: $it" } + (jaKeys - enKeys).map { "values に無い: $it" }
        if (errors.isNotEmpty()) {
            throw GradleException("文言リソースのキーが一致しません:\n" + errors.joinToString("\n"))
        }
    }
}

tasks.named("check") { dependsOn(checkStringResources) }
```

- [ ] **Step 2: 英語の文言を書く**

`src/webMain/composeResources/values/strings.xml`:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<resources>
    <string name="app_title">Nonaka Koki | Software Engineer</string>
    <string name="header_title">Portfolio</string>
    <string name="nav_home">Home</string>
    <string name="nav_about">About</string>
    <string name="nav_showcase">Showcase</string>
    <string name="nav_contact">Contact</string>
    <string name="menu">Menu</string>
    <string name="toggle_theme">Toggle theme</string>

    <string name="home_greeting">Hi, I'm</string>
    <string name="home_name">Nonaka Koki</string>
    <string name="home_role">Software Engineer</string>
    <string name="home_status">Available for new opportunities</string>
    <string name="home_ai_title">Building with AI</string>
    <string name="home_ai_body">I design agent-driven development workflows to ship fast without cutting corners.</string>
    <string name="home_now_title">Now building</string>
    <string name="home_now_body">AI-powered products. Coming soon.</string>
    <string name="home_cta_showcase">See my work</string>
    <string name="home_cta_contact">Get in touch</string>
    <string name="profile_image">Profile photo</string>

    <string name="about_title">About Me</string>
    <string name="skills_title">Skills &amp; Stack</string>
    <string name="experience_title">Experience</string>

    <string name="showcase_title">Showcase</string>
    <string name="showcase_more">More</string>
    <string name="showcase_close">Close</string>
    <string name="showcase_image_placeholder">Image coming soon</string>
    <string name="showcase_coming_soon">Coming Soon</string>
    <string name="showcase_added">Added: %1$s</string>
    <string name="showcase_technologies">Technologies: %1$s</string>
    <string name="showcase_url">URL: %1$s</string>

    <string name="contact_title">Contact</string>
    <string name="contact_description">Please fill out the form below to get in touch.</string>
    <string name="contact_first_name">First name*</string>
    <string name="contact_last_name">Last name*</string>
    <string name="contact_company">Company*</string>
    <string name="contact_email">Email*</string>
    <string name="contact_message">Message*</string>
    <string name="contact_privacy">By selecting this, you agree to our privacy policy.</string>
    <string name="contact_submit">Submit</string>
    <string name="contact_sent">Message sent successfully!</string>
    <string name="error_required">Required</string>
    <string name="error_max_100">Max 100 characters</string>
    <string name="error_max_254">Max 254 characters</string>
    <string name="error_max_500">Max 500 characters</string>
    <string name="error_invalid_email">Invalid email format</string>
</resources>
```

- [ ] **Step 3: チェックが失敗することを確認する**

まず `values-ja/strings.xml` を、キーを 1 つだけ入れた状態で作る:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<resources>
    <string name="app_title">Nonaka Koki | ソフトウェアエンジニア</string>
</resources>
```

Run: `./gradlew checkStringResources`
Expected: FAIL。メッセージに `values-ja に無い: header_title` などが並ぶ。

- [ ] **Step 4: 日本語の文言を書く**

`src/webMain/composeResources/values-ja/strings.xml` を次の内容に置き換える:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<resources>
    <string name="app_title">Nonaka Koki | ソフトウェアエンジニア</string>
    <string name="header_title">ポートフォリオ</string>
    <string name="nav_home">ホーム</string>
    <string name="nav_about">自己紹介</string>
    <string name="nav_showcase">作品</string>
    <string name="nav_contact">連絡先</string>
    <string name="menu">メニュー</string>
    <string name="toggle_theme">テーマを切り替え</string>

    <string name="home_greeting">こんにちは、</string>
    <string name="home_name">Nonaka Koki</string>
    <string name="home_role">ソフトウェアエンジニア</string>
    <string name="home_status">新しい機会を探しています</string>
    <string name="home_ai_title">AI と一緒に開発する</string>
    <string name="home_ai_body">AI エージェントを前提にした開発の進め方を組み立て、速く、品質を落とさずに作っています。</string>
    <string name="home_now_title">いま作っているもの</string>
    <string name="home_now_body">AI を組み込んだプロダクト。準備中です。</string>
    <string name="home_cta_showcase">作品を見る</string>
    <string name="home_cta_contact">連絡する</string>
    <string name="profile_image">プロフィール写真</string>

    <string name="about_title">自己紹介</string>
    <string name="skills_title">スキルと技術</string>
    <string name="experience_title">経歴</string>

    <string name="showcase_title">作品</string>
    <string name="showcase_more">もっと見る</string>
    <string name="showcase_close">閉じる</string>
    <string name="showcase_image_placeholder">画像は準備中です</string>
    <string name="showcase_coming_soon">近日公開</string>
    <string name="showcase_added">追加日: %1$s</string>
    <string name="showcase_technologies">使用技術: %1$s</string>
    <string name="showcase_url">URL: %1$s</string>

    <string name="contact_title">お問い合わせ</string>
    <string name="contact_description">以下のフォームからご連絡ください。</string>
    <string name="contact_first_name">名*</string>
    <string name="contact_last_name">姓*</string>
    <string name="contact_company">会社名*</string>
    <string name="contact_email">メールアドレス*</string>
    <string name="contact_message">メッセージ*</string>
    <string name="contact_privacy">チェックすると、プライバシーポリシーに同意したものとみなします。</string>
    <string name="contact_submit">送信</string>
    <string name="contact_sent">メッセージを送信しました。</string>
    <string name="error_required">必須項目です</string>
    <string name="error_max_100">100 文字以内で入力してください</string>
    <string name="error_max_254">254 文字以内で入力してください</string>
    <string name="error_max_500">500 文字以内で入力してください</string>
    <string name="error_invalid_email">メールアドレスの形式が正しくありません</string>
</resources>
```

- [ ] **Step 5: チェックとビルドが通ることを確認する**

Run: `./gradlew check`
Expected: `BUILD SUCCESSFUL`（`:checkStringResources` が実行され、成功している）

- [ ] **Step 6: コミット**

```bash
./gradlew spotlessApply
git add build.gradle.kts src/webMain/composeResources/values src/webMain/composeResources/values-ja
git commit -m "feat: 日英の文言リソースと、キーの一致チェックを追加"
```

---

### Task 5: Section enum とナビゲーションの整理

**Files:**
- Create: `shared/layout/Section.kt`
- Modify: `components/Header.kt`（全体を置き換え）
- Modify: `components/NavigationDrawer.kt`（全体を置き換え）
- Modify: `shared/layout/Layout.kt`（全体を置き換え）
- Modify: `app/App.kt:35`
- Modify: `screens/home/HomeScreen.kt`、`screens/home/Introduction.kt`

- [ ] **Step 1: Section enum を作る**

`src/webMain/kotlin/jp/kukv/portfolio/shared/layout/Section.kt`:

```kotlin
package jp.kukv.portfolio.shared.layout

import org.jetbrains.compose.resources.StringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.nav_about
import portfolio.generated.resources.nav_contact
import portfolio.generated.resources.nav_home
import portfolio.generated.resources.nav_showcase

enum class Section(
    val label: StringResource,
) {
    Home(Res.string.nav_home),
    About(Res.string.nav_about),
    Showcase(Res.string.nav_showcase),
    Contact(Res.string.nav_contact),
}
```

- [ ] **Step 2: Header を置き換える**

`components/Header.kt`。タブレット用とデスクトップ用で同じだったテーマボタンの分岐をやめ、`ThemeToggle` に共通化する。

```kotlin
package jp.kukv.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.layout.Section
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.header_title
import portfolio.generated.resources.menu
import portfolio.generated.resources.toggle_theme

@Composable
fun MobileHeader(onMenuOpen: () -> Unit) {
    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            IconButton(
                onClick = onMenuOpen,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(Res.string.menu),
                )
            }
            Text(
                stringResource(Res.string.header_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
fun DesktopHeader(
    onNavigate: (Section) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {
    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(Res.string.header_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
            ) {
                Section.entries.forEach { section ->
                    TextButton(onClick = { onNavigate(section) }) { Text(stringResource(section.label)) }
                }
            }
            ThemeToggle(isDarkTheme = isDarkTheme, onThemeChange = onThemeChange)
        }
    }
}

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {
    IconToggleButton(
        checked = isDarkTheme,
        onCheckedChange = onThemeChange,
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
            contentDescription = stringResource(Res.string.toggle_theme),
        )
    }
}
```

- [ ] **Step 3: NavigationDrawer を置き換える**

```kotlin
package jp.kukv.portfolio.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.layout.Section
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.header_title

@Composable
fun NavigationDrawer(
    onNavigate: (Section) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxHeight().width(280.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
        ) {
            Text(
                stringResource(Res.string.header_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Section.entries.forEach { section ->
                NavigationDrawerItem(
                    label = { Text(stringResource(section.label)) },
                    selected = false,
                    onClick = { onNavigate(section) },
                    modifier = Modifier.padding(horizontal = 8.dp),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                ThemeToggle(isDarkTheme = isDarkTheme, onThemeChange = onThemeChange)
            }
        }
    }
}
```

- [ ] **Step 4: Layout を置き換える**

`shared/layout/Layout.kt`:

```kotlin
package jp.kukv.portfolio.shared.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import jp.kukv.portfolio.app.LocalAppViewModel
import jp.kukv.portfolio.components.DesktopHeader
import jp.kukv.portfolio.components.Footer
import jp.kukv.portfolio.components.MobileHeader
import jp.kukv.portfolio.components.NavigationDrawer
import jp.kukv.portfolio.screens.about.AboutScreen
import jp.kukv.portfolio.screens.contact.ContactScreen
import jp.kukv.portfolio.screens.home.HomeScreen
import jp.kukv.portfolio.screens.showcase.ShowcaseScreen
import kotlinx.coroutines.launch

@Composable
fun MobileLayout(
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
    snackbarHostState: SnackbarHostState,
) {
    val appViewModel = LocalAppViewModel.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun navigate(section: Section) {
        scope.launch { scrollToSection(section, scrollState, sectionPositions) }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                onNavigate = { section ->
                    scope.launch {
                        drawerState.close()
                        scrollToSection(section, scrollState, sectionPositions)
                    }
                },
                isDarkTheme = appViewModel.isDarkTheme,
                onThemeChange = { appViewModel.setDarkTheme(it) },
            )
        },
    ) {
        Scaffold(
            topBar = {
                MobileHeader(
                    onMenuOpen = { scope.launch { drawerState.open() } },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            MainContent(
                padding = padding,
                scrollState = scrollState,
                sectionPositions = sectionPositions,
                snackbarHostState = snackbarHostState,
                onNavigate = ::navigate,
            )
        }
    }
}

@Composable
fun DesktopLayout(
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
    snackbarHostState: SnackbarHostState,
) {
    val appViewModel = LocalAppViewModel.current
    val scope = rememberCoroutineScope()

    fun navigate(section: Section) {
        scope.launch { scrollToSection(section, scrollState, sectionPositions) }
    }

    Scaffold(
        topBar = {
            DesktopHeader(
                onNavigate = ::navigate,
                isDarkTheme = appViewModel.isDarkTheme,
                onThemeChange = { appViewModel.setDarkTheme(it) },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        MainContent(
            padding = padding,
            scrollState = scrollState,
            sectionPositions = sectionPositions,
            snackbarHostState = snackbarHostState,
            onNavigate = ::navigate,
        )
    }
}

private suspend fun scrollToSection(
    section: Section,
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
) {
    val pos = sectionPositions[section] ?: 0
    scrollState.animateScrollTo(pos)
}

private fun Modifier.trackPosition(
    section: Section,
    sectionPositions: SnapshotStateMap<Section, Int>,
): Modifier =
    onGloballyPositioned { coordinates ->
        sectionPositions[section] = maxOf(0, coordinates.positionInParent().y.toInt())
    }

@Composable
private fun MainContent(
    padding: PaddingValues,
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
    snackbarHostState: SnackbarHostState,
    onNavigate: (Section) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
    ) {
        HomeScreen(
            modifier = Modifier.trackPosition(Section.Home, sectionPositions),
            onNavigate = onNavigate,
            topPadding = padding.calculateTopPadding(),
        )
        AboutScreen(modifier = Modifier.trackPosition(Section.About, sectionPositions))
        ShowcaseScreen(modifier = Modifier.trackPosition(Section.Showcase, sectionPositions))
        ContactScreen(
            onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
            modifier = Modifier.trackPosition(Section.Contact, sectionPositions),
        )
        Footer()
    }
}
```

- [ ] **Step 5: App と Home の型を合わせる**

`app/App.kt`:
- `val sectionPositions = remember { mutableStateMapOf<String, Int>() }` を `val sectionPositions = remember { mutableStateMapOf<Section, Int>() }` に変更する。
- `import jp.kukv.portfolio.shared.layout.Section` を追加する。

`screens/home/HomeScreen.kt`:
- `onNavigate: (String) -> Unit` の 3 か所（`HomeScreen`、`DesktopIntroduction`、`MobileTabletIntroduction` の引数）をすべて `onNavigate: (Section) -> Unit` に変更する。
- `import jp.kukv.portfolio.shared.layout.Section` を追加する。

`screens/home/Introduction.kt`:
- `fun Introduction(onNavigate: (String) -> Unit)` を `fun Introduction(onNavigate: (Section) -> Unit)` に変更する。
- `onNavigate("showcase")` を `onNavigate(Section.Showcase)` に変更する。
- `import jp.kukv.portfolio.shared.layout.Section` を追加する。

- [ ] **Step 6: ビルドと動作を確認する**

Run: `./gradlew check compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

Run: `./gradlew wasmJsBrowserDevelopmentRun`

次を確認する:
- デスクトップ幅でヘッダーの 4 つのリンクを押すと、それぞれのセクションへスクロールする。
- モバイル幅でドロワーの 4 つのリンクを押すと、それぞれのセクションへスクロールする。
- Home の Showcase ボタンを押すと、Showcase へスクロールする。

- [ ] **Step 7: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin
git commit -m "refactor: セクションの識別を enum にし、ナビゲーションの文言をリソース化"
```

---

### Task 6: 画面幅の判定を compose-adaptive に置き換える

**Files:**
- Create: `shared/layout/LayoutSize.kt`
- Modify: `app/App.kt`（全体を置き換え）、`app/AppViewModel.kt`
- Modify: `screens/home/HomeScreen.kt`、`screens/about/AboutScreen.kt`、`screens/showcase/ShowcaseScreen.kt`、`screens/contact/ContactScreen.kt`

- [ ] **Step 1: LayoutSize を作る**

`src/webMain/kotlin/jp/kukv/portfolio/shared/layout/LayoutSize.kt`:

```kotlin
package jp.kukv.portfolio.shared.layout

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

/** 画面幅の 3 区分。境界は Material の標準(600dp / 840dp)。 */
enum class LayoutSize {
    Compact,
    Medium,
    Expanded,
}

@Composable
fun currentLayoutSize(): LayoutSize {
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    return when {
        sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> LayoutSize.Expanded
        sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> LayoutSize.Medium
        else -> LayoutSize.Compact
    }
}
```

- [ ] **Step 2: App を置き換える**

`app/App.kt`:

```kotlin
package jp.kukv.portfolio.app

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.kukv.portfolio.shared.layout.DesktopLayout
import jp.kukv.portfolio.shared.layout.LayoutSize
import jp.kukv.portfolio.shared.layout.MobileLayout
import jp.kukv.portfolio.shared.layout.Section
import jp.kukv.portfolio.shared.layout.currentLayoutSize

@Composable
fun App() {
    val viewModel: AppViewModel = viewModel { AppViewModel() }

    val scrollState = remember { ScrollState(0) }
    val sectionPositions = remember { mutableStateMapOf<Section, Int>() }
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme(viewModel) {
        when (currentLayoutSize()) {
            LayoutSize.Compact -> MobileLayout(scrollState, sectionPositions, snackbarHostState)
            else -> DesktopLayout(scrollState, sectionPositions, snackbarHostState)
        }
    }
}
```

- [ ] **Step 3: AppViewModel から画面幅の状態を削除する**

`app/AppViewModel.kt` から次を削除する:
- `enum class WindowSizeClass { ... }` 全体
- `@Stable class WindowSizeState(...) { ... }` 全体
- `val windowSizeState = WindowSizeState(WindowSizeClass.Mobile)`
- `fun updateWindowSize(sizeClass: WindowSizeClass) { ... }`
- 使われなくなった `import androidx.compose.runtime.Stable`

- [ ] **Step 4: 各画面の判定を置き換える**

`screens/home/HomeScreen.kt`:
- 次の 2 行を:
  ```kotlin
      val appViewModel = LocalAppViewModel.current
      val windowSizeState = appViewModel.windowSizeState
  ```
  次の 1 行に置き換える:
  ```kotlin
      val layoutSize = currentLayoutSize()
  ```
- `windowSizeState.isDesktop -> DesktopIntroduction(onNavigate)` を `layoutSize == LayoutSize.Expanded -> DesktopIntroduction(onNavigate)` に変更する。
- import の `jp.kukv.portfolio.app.LocalAppViewModel` を削除し、`jp.kukv.portfolio.shared.layout.LayoutSize` と `jp.kukv.portfolio.shared.layout.currentLayoutSize` を追加する。

`screens/about/AboutScreen.kt`（`SkillAndStacksSection` 内）:
- 次の 2 行を:
  ```kotlin
      val appViewModel = LocalAppViewModel.current
      val windowSizeState = appViewModel.windowSizeState
  ```
  次の 1 行に置き換える:
  ```kotlin
      val layoutSize = currentLayoutSize()
  ```
- `if (windowSizeState.isMobile)` を `if (layoutSize == LayoutSize.Compact)` に変更する。
- `else if (windowSizeState.isTablet)` を `else if (layoutSize == LayoutSize.Medium)` に変更する。
- import の `jp.kukv.portfolio.app.LocalAppViewModel` を削除し、`LayoutSize` と `currentLayoutSize` を追加する。

`screens/showcase/ShowcaseScreen.kt`（`ShowcaseScreen` 内）:
- 次の 3 行を:
  ```kotlin
      val appViewModel = LocalAppViewModel.current
      val isMobile = appViewModel.windowSizeState.isMobile
      val isTablet = appViewModel.windowSizeState.isTablet
  ```
  次の 3 行に置き換える:
  ```kotlin
      val layoutSize = currentLayoutSize()
      val isMobile = layoutSize == LayoutSize.Compact
      val isTablet = layoutSize == LayoutSize.Medium
  ```
- import の `jp.kukv.portfolio.app.LocalAppViewModel` を削除し、`LayoutSize` と `currentLayoutSize` を追加する。

`screens/contact/ContactScreen.kt`（`ContactScreen` 内）:
- 次の 2 行を:
  ```kotlin
      val appViewModel = LocalAppViewModel.current
      val isMobile = appViewModel.windowSizeState.isMobile
  ```
  次の 1 行に置き換える:
  ```kotlin
      val isMobile = currentLayoutSize() == LayoutSize.Compact
  ```
- import の `jp.kukv.portfolio.app.LocalAppViewModel` を削除し、`LayoutSize` と `currentLayoutSize` を追加する。

- [ ] **Step 5: 残りがないことを確認する**

Run: `grep -rn "windowSizeState\|WindowSizeClass\.\(Mobile\|Tablet\|Desktop\)" src/webMain/kotlin`
Expected: 出力なし

- [ ] **Step 6: ビルドと動作を確認する**

Run: `./gradlew check compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

Run: `./gradlew wasmJsBrowserDevelopmentRun`

ブラウザの幅を 500px、700px、1000px に変えて、次を確認する:
- 500px: モバイル用ヘッダー（ハンバーガーメニュー）、Skills が 1 列。
- 700px: デスクトップ用ヘッダー、Skills が 2 列。
- 1000px: Skills が 3 列。

- [ ] **Step 7: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin
git commit -m "refactor: 画面幅の判定を compose-adaptive の WindowSizeClass に置き換える"
```

---

### Task 7: その場での言語切り替え

**Files:**
- Create: `shared/i18n/AppEnvironment.kt`、`app/DocumentMetadata.kt`、`components/LanguageToggle.kt`
- Modify: `src/webMain/resources/index.html`、`app/App.kt`、`components/Header.kt`、`components/NavigationDrawer.kt`、`shared/layout/Layout.kt`

- [ ] **Step 1: index.html に navigator.languages の上書きを入れる**

公式ドキュメント「Resource environment」の Web 向けの方法です。`src/webMain/resources/index.html` の `<link type="text/css" ...>` の直前に、次を挿入する:

```html
    <script>
        // Compose Resources の言語をその場で切り替えるための上書き(公式ドキュメントの一時的な回避策)。
        // window.__customLocale は shared/i18n/AppEnvironment.kt が設定する。
        var currentLanguagesImplementation = Object.getOwnPropertyDescriptor(Navigator.prototype, "languages");
        var newLanguagesImplementation = Object.assign({}, currentLanguagesImplementation, {
            get: function () {
                if (window.__customLocale) {
                    return [window.__customLocale];
                } else {
                    return currentLanguagesImplementation.get.apply(this);
                }
            }
        });
        Object.defineProperty(Navigator.prototype, "languages", newLanguagesImplementation);
    </script>
```

- [ ] **Step 2: AppEnvironment を作る**

`src/webMain/kotlin/jp/kukv/portfolio/shared/i18n/AppEnvironment.kt`:

```kotlin
@file:OptIn(ExperimentalWasmJsInterop::class)

package jp.kukv.portfolio.shared.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

/**
 * Compose Resources の言語をその場で切り替える。
 * 公式 API がまだ無いため、公式ドキュメントの回避策(index.html の navigator.languages 上書き)を使う。
 * 公式 API ができたら、このファイルだけを差し替える。
 */
@Composable
fun AppEnvironment(
    language: AppLanguage,
    content: @Composable () -> Unit,
) {
    applyCustomLocale(language.tag)
    key(language) {
        content()
    }
}

private fun applyCustomLocale(tag: String) {
    js(
        """
        if (window.__customLocale !== tag) {
            window.__customLocale = tag;
            window.dispatchEvent(new Event("languagechange"));
        }
        """,
    )
}
```

- [ ] **Step 3: DocumentMetadata を作る**

`src/webMain/kotlin/jp/kukv/portfolio/app/DocumentMetadata.kt`:

```kotlin
package jp.kukv.portfolio.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import jp.kukv.portfolio.shared.i18n.AppLanguage
import jp.kukv.portfolio.shared.lib.setDocumentLanguage
import jp.kukv.portfolio.shared.lib.setDocumentTitle
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.app_title

/** 表示中の言語を <html lang> と document.title に反映する。 */
@Composable
fun DocumentMetadata(language: AppLanguage) {
    val title = stringResource(Res.string.app_title)
    LaunchedEffect(language, title) {
        setDocumentLanguage(language.tag)
        setDocumentTitle(title)
    }
}
```

- [ ] **Step 4: App に組み込む**

`app/App.kt` の `AppTheme(viewModel) { ... }` を次に置き換え、`import jp.kukv.portfolio.shared.i18n.AppEnvironment` を追加する:

```kotlin
    AppTheme(viewModel) {
        AppEnvironment(viewModel.language) {
            DocumentMetadata(viewModel.language)
            when (currentLayoutSize()) {
                LayoutSize.Compact -> MobileLayout(scrollState, sectionPositions, snackbarHostState)
                else -> DesktopLayout(scrollState, sectionPositions, snackbarHostState)
            }
        }
    }
```

- [ ] **Step 5: LanguageToggle を作る**

`src/webMain/kotlin/jp/kukv/portfolio/components/LanguageToggle.kt`:

```kotlin
package jp.kukv.portfolio.components

import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jp.kukv.portfolio.shared.i18n.AppLanguage

@Composable
fun LanguageToggle(
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = AppLanguage.entries
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = option == language,
                onClick = { onLanguageChange(option) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                icon = {},
            ) {
                Text(option.tag.uppercase())
            }
        }
    }
}
```

- [ ] **Step 6: ヘッダーとドロワーに置く**

`components/Header.kt` の `DesktopHeader`:
- 引数の `onThemeChange: (Boolean) -> Unit,` の後に次を追加する:
  ```kotlin
      language: AppLanguage,
      onLanguageChange: (AppLanguage) -> Unit,
  ```
- `ThemeToggle(isDarkTheme = isDarkTheme, onThemeChange = onThemeChange)` の直前に次を追加する:
  ```kotlin
              LanguageToggle(language = language, onLanguageChange = onLanguageChange)
              Spacer(modifier = Modifier.width(8.dp))
  ```
- import に `androidx.compose.foundation.layout.Spacer`、`androidx.compose.foundation.layout.width`、`jp.kukv.portfolio.shared.i18n.AppLanguage` を追加する。

`components/NavigationDrawer.kt`:
- 引数の `onThemeChange: (Boolean) -> Unit,` の後に `language: AppLanguage,` と `onLanguageChange: (AppLanguage) -> Unit,` を追加する。
- 末尾の `Box(...) { ThemeToggle(...) }` を次に置き換える:
  ```kotlin
              Row(
                  modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                  LanguageToggle(language = language, onLanguageChange = onLanguageChange)
                  ThemeToggle(isDarkTheme = isDarkTheme, onThemeChange = onThemeChange)
              }
  ```
- import の `androidx.compose.foundation.layout.Box` を削除し、`Arrangement`、`Row`、`jp.kukv.portfolio.shared.i18n.AppLanguage` を追加する。

`shared/layout/Layout.kt`:
- `NavigationDrawer(...)` と `DesktopHeader(...)` の呼び出しで、`onThemeChange = { appViewModel.setDarkTheme(it) },` の後に次を追加する:
  ```kotlin
                  language = appViewModel.language,
                  onLanguageChange = { appViewModel.setLanguage(it) },
  ```

- [ ] **Step 7: ビルドと動作を確認する**

Run: `./gradlew check compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

Run: `./gradlew wasmJsBrowserDevelopmentRun`

次を確認する:
- ブラウザの言語が日本語なら、初回は JA が選ばれ、ナビゲーションが「ホーム / 自己紹介 / 作品 / 連絡先」になる。
- EN を押すと、再読み込みせずにナビゲーションが「Home / About / Showcase / Contact」に変わる。
- 再読み込みしても EN のまま。
- DevTools の Elements で `<html lang="en">` になり、タブのタイトルが「Nonaka Koki | Software Engineer」になっている。
- JA に戻すと `<html lang="ja">` になり、タイトルが「Nonaka Koki | ソフトウェアエンジニア」になる。
- モバイル幅では、ドロワーの下部に EN / JA とテーマのボタンが並んでいる。

**言語を切り替えても文言が変わらない場合:** Compose Resources が `Locale.current` を `key` の再構成で読み直していません。コードを推測で直さず、作業を止めて報告してください。

- [ ] **Step 8: コミット**

```bash
./gradlew spotlessApply
git add src/webMain
git commit -m "feat: ヘッダーとドロワーから日英をその場で切り替えられるようにする"
```

---

### Task 8: Skills カードの重複をなくす

**Files:**
- Create: `screens/about/SkillCategoryCard.kt`
- Modify: `screens/about/AboutScreen.kt`（`SkillAndStacksSection` 全体）

- [ ] **Step 1: カードを 1 つの Composable にする**

`src/webMain/kotlin/jp/kukv/portfolio/screens/about/SkillCategoryCard.kt`:

```kotlin
package jp.kukv.portfolio.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillCategoryCard(
    category: SkillCategory,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = category.label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                category.skills.forEach { skill ->
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Text(
                            text = skill,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}
```

- [ ] **Step 2: SkillAndStacksSection を置き換える**

`screens/about/AboutScreen.kt` の `@Composable fun SkillAndStacksSection() { ... }` 全体を次に置き換える。見出しの文言はここでリソース化する（Task 9 の作業を前倒し）。

```kotlin
@Composable
fun SkillAndStacksSection() {
    val columns =
        when (currentLayoutSize()) {
            LayoutSize.Compact -> 1
            LayoutSize.Medium -> 2
            LayoutSize.Expanded -> 3
        }

    Text(
        text = stringResource(Res.string.skills_title),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Column(
        modifier = Modifier.widthIn(max = 1000.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        skillCategories.chunked(columns).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                rowCategories.forEach { category ->
                    SkillCategoryCard(category = category, modifier = Modifier.weight(1f).fillMaxHeight())
                }
                repeat(columns - rowCategories.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
```

- import に `org.jetbrains.compose.resources.stringResource`、`portfolio.generated.resources.Res`、`portfolio.generated.resources.skills_title` を追加する。
- 使われなくなった import を削除する: `FlowRow`、`FontWeight`、`androidx.compose.runtime.remember`、`kotlin.collections.chunked`、`kotlin.collections.forEach`。`Surface` は `AboutMeSection` で使っているので残す。
- `@OptIn(ExperimentalLayoutApi::class)` が `AboutScreen` に付いている。`FlowRow` を使わなくなったので、注釈と `ExperimentalLayoutApi` の import を削除する。

- [ ] **Step 3: ビルドと見た目を確認する**

Run: `./gradlew check compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

Run: `./gradlew wasmJsBrowserDevelopmentRun`

500px、700px、1000px の幅で、Skills のカードがそれぞれ 1 列、2 列（2 段目は左に 1 枚と空き）、3 列で並ぶことを確認する。各段のカードの高さがそろっていることも確認する。

- [ ] **Step 4: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin/jp/kukv/portfolio/screens/about
git commit -m "refactor: Skills のカードを 1 つの Composable にまとめる"
```

---

### Task 9: About・Showcase・Contact の文言をリソース化する

**Files:**
- Modify: `screens/about/AboutScreen.kt`、`screens/showcase/ShowcaseScreen.kt`、`screens/contact/ContactScreen.kt`

各ファイルに `import org.jetbrains.compose.resources.stringResource` と `import portfolio.generated.resources.Res` を追加し、使うキーごとに `import portfolio.generated.resources.<キー名>` を追加する。

- [ ] **Step 1: About の見出し**

`AboutScreen.kt`:
- `text = "About Me",` → `text = stringResource(Res.string.about_title),`
- `text = "Experience",` → `text = stringResource(Res.string.experience_title),`

`AboutMeSection` の本文と、`skillCategories`、`experiences` のダミーデータは設計どおり変更しない。

- [ ] **Step 2: Showcase**

`ShowcaseScreen.kt`:
- `text = "Showcase",` → `text = stringResource(Res.string.showcase_title),`
- `Text("More")` → `Text(stringResource(Res.string.showcase_more))`
- `Text("Image Placeholder")` → `Text(stringResource(Res.string.showcase_image_placeholder))`
- `Text("Coming Soon", style = MaterialTheme.typography.titleMedium)` → `Text(stringResource(Res.string.showcase_coming_soon), style = MaterialTheme.typography.titleMedium)`
- `Text(text = "Added: ${project.addedDate}", style = MaterialTheme.typography.bodySmall)`（2 か所）→ `Text(text = stringResource(Res.string.showcase_added, project.addedDate), style = MaterialTheme.typography.bodySmall)`
- `Text(text = "Technologies: ${project.technologies.joinToString(", ")}", ...)` → `Text(text = stringResource(Res.string.showcase_technologies, project.technologies.joinToString(", ")), ...)`
- `Text(text = "URL: ${project.url}", ...)` → `Text(text = stringResource(Res.string.showcase_url, project.url), ...)`
- `Text("Close")` → `Text(stringResource(Res.string.showcase_close))`

`projects` のダミーデータと既存の TODO コメントは変更しない。

- [ ] **Step 3: Contact**

`ContactScreen.kt`:
- `text = "contact us",` → `text = stringResource(Res.string.contact_title),`
- `text = "Please fill out the form below to get in touch with us.",` → `text = stringResource(Res.string.contact_description),`
- `label = { Text("Company*") }` → `label = { Text(stringResource(Res.string.contact_company)) }`
- `label = { Text("Email*") }` → `label = { Text(stringResource(Res.string.contact_email)) }`
- `label = { Text("Message*") }` → `label = { Text(stringResource(Res.string.contact_message)) }`
- `label = { Text("First name*") }` → `label = { Text(stringResource(Res.string.contact_first_name)) }`
- `label = { Text("Last name*") }` → `label = { Text(stringResource(Res.string.contact_last_name)) }`
- 文字列 `"Required"`（5 か所）→ `stringResource(Res.string.error_required)`
- 文字列 `"Max 100 characters"`（3 か所）→ `stringResource(Res.string.error_max_100)`
- 文字列 `"Max 254 characters"` → `stringResource(Res.string.error_max_254)`
- 文字列 `"Invalid email format"` → `stringResource(Res.string.error_invalid_email)`
- 文字列 `"Max 500 characters"` → `stringResource(Res.string.error_max_500)`
- `text = "By selecting this, you agree to our privacy policy.",` → `text = stringResource(Res.string.contact_privacy),`
- `Text("Submit")` → `Text(stringResource(Res.string.contact_submit))`
- `onSuccess = { onShowSnackbar("Message sent successfully!") },` → `onSuccess = { onShowSnackbar(getString(Res.string.contact_sent)) },`（`import org.jetbrains.compose.resources.getString` を追加。`onSuccess` は suspend なので `getString` を呼べる）

- [ ] **Step 4: 英語の直書きが残っていないことを確認する**

Run: `grep -rnE 'Text\("[A-Za-z]|text = "[A-Za-z]|label = \{ Text\("' src/webMain/kotlin/jp/kukv/portfolio/screens src/webMain/kotlin/jp/kukv/portfolio/components`
Expected: `screens/home/` 配下（Task 12 で置き換える）以外に、UI ラベルの出力がない。`AboutScreen.kt` の自己紹介本文はダミーデータなので残ってよい。

- [ ] **Step 5: ビルドと動作を確認する**

Run: `./gradlew check compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

Run: `./gradlew wasmJsBrowserDevelopmentRun`

JA と EN を切り替えて、About・Showcase・Contact の見出し、ボタン、フォームのラベルが切り替わることを確認する。メールアドレス欄に `abc` と入れたとき、エラー文言も切り替わることを確認する。

- [ ] **Step 6: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin/jp/kukv/portfolio/screens
git commit -m "feat: About・Showcase・Contact の UI ラベルを日英対応にする"
```

---

### Task 10: 配色を刷新する

**Files:**
- Modify: `shared/theme/Colors.kt`（全体を置き換え）

値は `@material/material-color-utilities` 0.3.0 で算出した。主色 `#8C1D2C` を種にした Fidelity スキームを使い、tertiary パレットは `#B85C38` から作った。ライトの主色はモックで合意した `#8C1D2C` に、ライトの tertiary は白文字でも読める tone 40（`#9A4523`）に調整している。

- [ ] **Step 1: Colors.kt を置き換える**

```kotlin
package jp.kukv.portfolio.shared.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// 主色 #8C1D2C(ワインレッド)、tertiary #B85C38(テラコッタ)から生成した Material 3 のカラーロール。
val WineLightColorScheme =
    lightColorScheme(
        primary = Color(0xFF8C1D2C),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDADA),
        onPrimaryContainer = Color(0xFF891A2A),
        inversePrimary = Color(0xFFFFB3B4),
        secondary = Color(0xFF815153),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFDBFC0),
        onSecondaryContainer = Color(0xFF7A4B4C),
        tertiary = Color(0xFF9A4523),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDBCF),
        onTertiaryContainer = Color(0xFF7B2F0E),
        background = Color(0xFFFFF8F7),
        onBackground = Color(0xFF221919),
        surface = Color(0xFFFFF8F7),
        onSurface = Color(0xFF221919),
        surfaceVariant = Color(0xFFF8DCDC),
        onSurfaceVariant = Color(0xFF554242),
        surfaceTint = Color(0xFF8C1D2C),
        inverseSurface = Color(0xFF382E2E),
        inverseOnSurface = Color(0xFFFFEDEC),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        outline = Color(0xFF887272),
        outlineVariant = Color(0xFFDBC0C0),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFFFFF8F7),
        surfaceDim = Color(0xFFE8D6D5),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFFF0F0),
        surfaceContainer = Color(0xFFFCEAE9),
        surfaceContainerHigh = Color(0xFFF6E4E3),
        surfaceContainerHighest = Color(0xFFF0DEDE),
    )

val WineDarkColorScheme =
    darkColorScheme(
        primary = Color(0xFFFFB3B4),
        onPrimary = Color(0xFF680017),
        primaryContainer = Color(0xFF8C1D2C),
        onPrimaryContainer = Color(0xFFFFDADA),
        inversePrimary = Color(0xFFA9333F),
        secondary = Color(0xFFF4B7B8),
        onSecondary = Color(0xFF4C2526),
        secondaryContainer = Color(0xFF693D3E),
        onSecondaryContainer = Color(0xFFE5A9AA),
        tertiary = Color(0xFFFFB59A),
        onTertiary = Color(0xFF5B1B00),
        tertiaryContainer = Color(0xFF7E3110),
        onTertiaryContainer = Color(0xFFFFDBCF),
        background = Color(0xFF1A1111),
        onBackground = Color(0xFFF0DEDE),
        surface = Color(0xFF1A1111),
        onSurface = Color(0xFFF0DEDE),
        surfaceVariant = Color(0xFF554242),
        onSurfaceVariant = Color(0xFFDBC0C0),
        surfaceTint = Color(0xFFFFB3B4),
        inverseSurface = Color(0xFFF0DEDE),
        inverseOnSurface = Color(0xFF382E2E),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        outline = Color(0xFFA38B8B),
        outlineVariant = Color(0xFF554242),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFF413736),
        surfaceDim = Color(0xFF1A1111),
        surfaceContainerLowest = Color(0xFF140C0C),
        surfaceContainerLow = Color(0xFF221919),
        surfaceContainer = Color(0xFF271D1D),
        surfaceContainerHigh = Color(0xFF322827),
        surfaceContainerHighest = Color(0xFF3D3232),
    )

fun changeColorScheme(isDarkTheme: Boolean): ColorScheme =
    when (isDarkTheme) {
        true -> WineDarkColorScheme
        false -> WineLightColorScheme
    }
```

- [ ] **Step 2: 旧パレットの定数が他で使われていないことを確認する**

Run: `grep -rnE "WineRed[0-9]|Rosetta[0-9]|WarmGrey[0-9]|VariantGrey[0-9]|WineBase" src/webMain/kotlin`
Expected: 出力なし

- [ ] **Step 3: ビルドと見た目を確認する**

Run: `./gradlew check compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

Run: `./gradlew wasmJsBrowserDevelopmentRun`

ライトとダークを切り替え、ボタンが `#8C1D2C`（ライト）/ `#FFB3B4`（ダーク）になっていることを確認する。

- [ ] **Step 4: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin/jp/kukv/portfolio/shared/theme/Colors.kt
git commit -m "feat: ワインレッドとテラコッタで全カラーロールを定義する"
```

---

### Task 11: Expressive の動きとフォントの整理

**Files:**
- Modify: `app/Theme.kt`、`shared/theme/Fonts.kt`
- Delete: `src/webMain/composeResources/font/NotoSansJP-{Thin,ExtraLight,Light,SemiBold,Black}.ttf`

- [ ] **Step 1: MotionScheme.expressive() を指定する**

`app/Theme.kt` の `MaterialExpressiveTheme(` の引数に `motionScheme = MotionScheme.expressive(),` を追加し、`import androidx.compose.material3.MotionScheme` を追加する:

```kotlin
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            motionScheme = MotionScheme.expressive(),
            shapes = shapes,
            typography = PortfolioTypography(),
            content = content,
        )
```

- [ ] **Step 2: フォントを 4 ウェイトに絞る**

`shared/theme/Fonts.kt` の `NotoSansJpFamily` を次に置き換える:

```kotlin
@Composable
fun NotoSansJpFamily(): FontFamily =
    FontFamily(
        Font(Res.font.NotoSansJP_Regular, weight = FontWeight.Normal),
        Font(Res.font.NotoSansJP_Medium, weight = FontWeight.Medium),
        Font(Res.font.NotoSansJP_Bold, weight = FontWeight.Bold),
        Font(Res.font.NotoSansJP_ExtraBold, weight = FontWeight.ExtraBold),
    )
```

import から次の 5 つを削除する: `NotoSansJP_Black`、`NotoSansJP_ExtraLight`、`NotoSansJP_Light`、`NotoSansJP_SemiBold`、`NotoSansJP_Thin`。

- [ ] **Step 3: 不要なフォントファイルを削除する**

```bash
git rm src/webMain/composeResources/font/NotoSansJP-Thin.ttf src/webMain/composeResources/font/NotoSansJP-ExtraLight.ttf src/webMain/composeResources/font/NotoSansJP-Light.ttf src/webMain/composeResources/font/NotoSansJP-SemiBold.ttf src/webMain/composeResources/font/NotoSansJP-Black.ttf
```

- [ ] **Step 4: SemiBold を使っている箇所が崩れないことを確認する**

`FontWeight.SemiBold` は `SkillCategoryCard` などで使われています。ファイルを削除しても、Compose が最も近い Bold で代替するので問題ありません。

Run: `grep -rn "FontWeight\.\(Thin\|ExtraLight\|Light\|Black\)" src/webMain/kotlin`
Expected: 出力なし（ある場合は最も近い残存ウェイト `Normal` / `ExtraBold` に変更する）

- [ ] **Step 5: ビルドと配布物のサイズを確認する**

Run: `./gradlew check wasmJsBrowserDistribution jsBrowserDistribution`
Expected: `BUILD SUCCESSFUL`

Run: `find build/dist/wasmJs -name "NotoSansJP-*.ttf"`
Expected: `NotoSansJP-Regular.ttf`、`NotoSansJP-Medium.ttf`、`NotoSansJP-Bold.ttf`、`NotoSansJP-ExtraBold.ttf` の 4 件だけが表示される。

- [ ] **Step 6: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin/jp/kukv/portfolio/app/Theme.kt src/webMain/kotlin/jp/kukv/portfolio/shared/theme/Fonts.kt
git commit -m "feat: Expressive の MotionScheme を使い、フォントを 4 ウェイトに絞る"
```

---

### Task 12: Home を Bento グリッドにする

**Files:**
- Create: `screens/home/BentoTile.kt`、`screens/home/BentoTiles.kt`
- Modify: `screens/home/HomeScreen.kt`、`screens/home/ProfileImage.kt`、`screens/home/StatusPill.kt`（全体を置き換え）
- Delete: `screens/home/Introduction.kt`
- Modify: `shared/layout/Layout.kt`（`HomeScreen` の呼び出し）

- [ ] **Step 1: タイルの共通部品を作る**

`src/webMain/kotlin/jp/kukv/portfolio/screens/home/BentoTile.kt`:

```kotlin
package jp.kukv.portfolio.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val STAGGER_MILLIS = 70L

/**
 * Bento の 1 マス。表示時に index に応じて時間差でフェードとスケールで現れ、
 * onClick がある場合は押下中に少し縮む。
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BentoTile(
    index: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * STAGGER_MILLIS)
        visible = true
    }
    val motionScheme = MaterialTheme.motionScheme
    val alpha by animateFloatAsState(if (visible) 1f else 0f, motionScheme.defaultEffectsSpec())
    val appearScale by animateFloatAsState(if (visible) 1f else 0.9f, motionScheme.defaultSpatialSpec())
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(if (pressed) 0.96f else 1f, motionScheme.fastSpatialSpec())

    val tileModifier =
        modifier.graphicsLayer {
            this.alpha = alpha
            scaleX = appearScale * pressScale
            scaleY = appearScale * pressScale
        }
    val colors =
        CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColorFor(containerColor),
        )
    val body: @Composable ColumnScope.() -> Unit = {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), content = content)
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = tileModifier,
            shape = MaterialTheme.shapes.extraLarge,
            colors = colors,
            interactionSource = interactionSource,
            content = body,
        )
    } else {
        Card(
            modifier = tileModifier,
            shape = MaterialTheme.shapes.extraLarge,
            colors = colors,
            content = body,
        )
    }
}
```

- [ ] **Step 2: ProfileImage を Expressive のシェイプにする**

`screens/home/ProfileImage.kt` を置き換える:

```kotlin
package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.profile
import portfolio.generated.resources.profile_image

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.profile),
        contentDescription = stringResource(Res.string.profile_image),
        modifier = modifier.clip(MaterialShapes.Cookie9Sided.toShape()),
        contentScale = ContentScale.Crop,
    )
}
```

- [ ] **Step 3: StatusPill の文言をリソース化する**

`screens/home/StatusPill.kt`:
- `text = "Available for new opportunities",` → `text = stringResource(Res.string.home_status),`
- import に `org.jetbrains.compose.resources.stringResource`、`portfolio.generated.resources.Res`、`portfolio.generated.resources.home_status` を追加する。

- [ ] **Step 4: 各タイルを作る**

`src/webMain/kotlin/jp/kukv/portfolio/screens/home/BentoTiles.kt`:

```kotlin
package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.layout.Section
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.home_ai_body
import portfolio.generated.resources.home_ai_title
import portfolio.generated.resources.home_cta_contact
import portfolio.generated.resources.home_cta_showcase
import portfolio.generated.resources.home_greeting
import portfolio.generated.resources.home_name
import portfolio.generated.resources.home_now_body
import portfolio.generated.resources.home_now_title
import portfolio.generated.resources.home_role

@Composable
fun NameTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier, containerColor = MaterialTheme.colorScheme.primary) {
        Spacer(modifier = Modifier.weight(1f))
        Text(stringResource(Res.string.home_greeting), style = MaterialTheme.typography.titleMedium)
        Text(
            stringResource(Res.string.home_name),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(Res.string.home_role), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun PhotoTile(
    index: Int,
    imageModifier: Modifier,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ProfileImage(modifier = imageModifier)
        }
    }
}

@Composable
fun AiTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    MessageTile(
        index = index,
        icon = Icons.Default.AutoAwesome,
        title = Res.string.home_ai_title,
        body = Res.string.home_ai_body,
        modifier = modifier,
    )
}

@Composable
fun NowBuildingTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    MessageTile(
        index = index,
        icon = Icons.Default.Construction,
        title = Res.string.home_now_title,
        body = Res.string.home_now_body,
        modifier = modifier,
    )
}

@Composable
private fun MessageTile(
    index: Int,
    icon: ImageVector,
    title: StringResource,
    body: StringResource,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))
        Text(stringResource(title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(body), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun StatusTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier, containerColor = MaterialTheme.colorScheme.surfaceContainer) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            StatusPill()
        }
    }
}

@Composable
fun ShowcaseTile(
    index: Int,
    onNavigate: (Section) -> Unit,
    modifier: Modifier = Modifier,
) {
    LinkTile(
        index = index,
        label = Res.string.home_cta_showcase,
        onClick = { onNavigate(Section.Showcase) },
        containerColor = MaterialTheme.colorScheme.tertiary,
        modifier = modifier,
    )
}

@Composable
fun ContactTile(
    index: Int,
    onNavigate: (Section) -> Unit,
    modifier: Modifier = Modifier,
) {
    LinkTile(
        index = index,
        label = Res.string.home_cta_contact,
        onClick = { onNavigate(Section.Contact) },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    )
}

@Composable
private fun LinkTile(
    index: Int,
    label: StringResource,
    onClick: () -> Unit,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier, containerColor = containerColor, onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(label), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}
```

- [ ] **Step 5: HomeScreen を Bento に置き換える**

`screens/home/HomeScreen.kt` を置き換える:

```kotlin
package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.layout.LayoutSize
import jp.kukv.portfolio.shared.layout.Section
import jp.kukv.portfolio.shared.layout.currentLayoutSize

private val Gap = 16.dp

@Composable
fun HomeScreen(
    onNavigate: (Section) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.widthIn(max = 1100.dp).fillMaxWidth()) {
            when (currentLayoutSize()) {
                LayoutSize.Expanded -> ExpandedBento(onNavigate)
                LayoutSize.Medium -> MediumBento(onNavigate)
                LayoutSize.Compact -> CompactBento(onNavigate)
            }
        }
    }
}

/** 3 列。名前タイルが左で縦 2 マスを占める。 */
@Composable
private fun ExpandedBento(onNavigate: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        BentoRow {
            NameTile(index = 0, modifier = Modifier.weight(1.2f).fillMaxHeight())
            Column(modifier = Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(Gap)) {
                BentoRow {
                    PhotoTile(
                        index = 1,
                        imageModifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                    AiTile(index = 2, modifier = Modifier.weight(1f).fillMaxHeight())
                }
                BentoRow {
                    NowBuildingTile(index = 3, modifier = Modifier.weight(1f).fillMaxHeight())
                    StatusTile(index = 4, modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }
        }
        BentoRow {
            ShowcaseTile(index = 5, onNavigate = onNavigate, modifier = Modifier.weight(1f).fillMaxHeight())
            ContactTile(index = 6, onNavigate = onNavigate, modifier = Modifier.weight(1f).fillMaxHeight())
        }
    }
}

/** 2 列。 */
@Composable
private fun MediumBento(onNavigate: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        BentoRow {
            NameTile(index = 0, modifier = Modifier.weight(1f).fillMaxHeight())
            PhotoTile(
                index = 1,
                imageModifier = Modifier.fillMaxWidth().aspectRatio(1f),
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )
        }
        BentoRow {
            AiTile(index = 2, modifier = Modifier.weight(1f).fillMaxHeight())
            NowBuildingTile(index = 3, modifier = Modifier.weight(1f).fillMaxHeight())
        }
        StatusTile(index = 4, modifier = Modifier.fillMaxWidth())
        BentoRow {
            ShowcaseTile(index = 5, onNavigate = onNavigate, modifier = Modifier.weight(1f).fillMaxHeight())
            ContactTile(index = 6, onNavigate = onNavigate, modifier = Modifier.weight(1f).fillMaxHeight())
        }
    }
}

/** 1 列。名前 → 写真 → 残りの順。 */
@Composable
private fun CompactBento(onNavigate: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        NameTile(index = 0, modifier = Modifier.fillMaxWidth().height(220.dp))
        PhotoTile(index = 1, imageModifier = Modifier.size(200.dp), modifier = Modifier.fillMaxWidth())
        AiTile(index = 2, modifier = Modifier.fillMaxWidth())
        NowBuildingTile(index = 3, modifier = Modifier.fillMaxWidth())
        StatusTile(index = 4, modifier = Modifier.fillMaxWidth())
        ShowcaseTile(index = 5, onNavigate = onNavigate, modifier = Modifier.fillMaxWidth())
        ContactTile(index = 6, onNavigate = onNavigate, modifier = Modifier.fillMaxWidth())
    }
}

/** 中のタイルの高さを、いちばん高いタイルにそろえる行。 */
@Composable
private fun BentoRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(Gap),
        content = content,
    )
}
```

- [ ] **Step 6: Introduction を削除し、Layout の呼び出しを直す**

```bash
git rm src/webMain/kotlin/jp/kukv/portfolio/screens/home/Introduction.kt
```

`shared/layout/Layout.kt` の `MainContent` 内の `HomeScreen(...)` を次に置き換える（`topPadding` は不要になった）:

```kotlin
        HomeScreen(
            onNavigate = onNavigate,
            modifier = Modifier.trackPosition(Section.Home, sectionPositions),
        )
```

- [ ] **Step 7: ビルドを確認する**

Run: `./gradlew check compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 8: 見た目と動きを確認する**

Run: `./gradlew wasmJsBrowserDevelopmentRun`

次を確認する:
- **1000px 以上**: 左に名前タイル（縦 2 マス）、右上に写真と「AI と一緒に開発する」、右中段に「いま作っているもの」とステータス、最下段に「作品を見る」と「連絡する」が並ぶ。
- **700px**: 2 列で、名前と写真 → AI といま作っているもの → ステータス（全幅）→ 2 つのリンクの順に並ぶ。
- **400px**: 1 列で縦に並び、写真が 200dp のクッキー形で中央にある。
- 各行のタイルの高さがそろっている。
- 読み込み時に、タイルが左上から順に時間差で現れる。
- 「作品を見る」「連絡する」を押すとタイルが少し縮み、該当セクションへスクロールする。
- ライトとダークの両方で、各タイルの文字が背景に対して読める。
- JA と EN で、タイルの文言が切り替わる。

**`IntrinsicSize.Min` の行で写真が潰れる、または高さが 0 になる場合:** `PhotoTile` の `imageModifier` を `Modifier.size(240.dp)`（Expanded）/ `Modifier.size(200.dp)`（Medium）の固定サイズに変えて再確認する。

- [ ] **Step 9: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin
git commit -m "feat: Home を Bento グリッドにし、看板の文言と Expressive の動きを入れる"
```

---

### Task 13: 検索対策と静的な要約

**Files:**
- Modify: `src/webMain/resources/index.html`（全体を置き換え）、`src/webMain/resources/styles.css`
- Create: `src/webMain/resources/og-image.png`
- Modify: `app/App.kt`（静的な要約の削除）

- [ ] **Step 1: index.html を置き換える**

Task 7 で入れた `navigator.languages` 上書きスクリプトは、そのまま残す。

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nonaka Koki | Software Engineer</title>
    <meta name="description" content="Nonaka Koki (kukv) is a software engineer who builds with AI agents, designing agent-driven development workflows to ship fast without cutting corners. AI エージェントを前提にした開発の進め方を組み立て、速く、品質を落とさずに作るソフトウェアエンジニア。">
    <link rel="canonical" href="https://kukv.jp/">

    <meta property="og:type" content="profile">
    <meta property="og:url" content="https://kukv.jp/">
    <meta property="og:site_name" content="Nonaka Koki">
    <meta property="og:title" content="Nonaka Koki | Software Engineer">
    <meta property="og:description" content="A software engineer who builds with AI agents. AI と一緒に開発するソフトウェアエンジニア。">
    <meta property="og:image" content="https://kukv.jp/og-image.png">
    <meta property="og:image:width" content="1200">
    <meta property="og:image:height" content="630">
    <meta property="og:locale" content="en_US">
    <meta property="og:locale:alternate" content="ja_JP">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:site" content="@kukv">

    <script type="application/ld+json">
        {
            "@context": "https://schema.org",
            "@type": "Person",
            "name": "Nonaka Koki",
            "alternateName": "kukv",
            "url": "https://kukv.jp/",
            "image": "https://kukv.jp/og-image.png",
            "jobTitle": "Software Engineer",
            "description": "A software engineer who builds with AI agents.",
            "sameAs": [
                "https://github.com/kukv",
                "https://x.com/kukv",
                "https://bsky.app/profile/kukv.jp",
                "https://www.instagram.com/kukv",
                "https://www.facebook.com/04x17"
            ]
        }
    </script>

    <script>
        // Compose Resources の言語をその場で切り替えるための上書き(公式ドキュメントの一時的な回避策)。
        // window.__customLocale は shared/i18n/AppEnvironment.kt が設定する。
        var currentLanguagesImplementation = Object.getOwnPropertyDescriptor(Navigator.prototype, "languages");
        var newLanguagesImplementation = Object.assign({}, currentLanguagesImplementation, {
            get: function () {
                if (window.__customLocale) {
                    return [window.__customLocale];
                } else {
                    return currentLanguagesImplementation.get.apply(this);
                }
            }
        });
        Object.defineProperty(Navigator.prototype, "languages", newLanguagesImplementation);
    </script>
    <link type="text/css" rel="stylesheet" href="styles.css">
</head>
<body>
<!-- アプリの起動後に App.kt が削除する。起動前の表示とクローラー向けの要約を兼ねる。 -->
<main id="static-summary">
    <h1>Nonaka Koki</h1>
    <p>Software Engineer / ソフトウェアエンジニア</p>
    <p>I build with AI agents, designing agent-driven development workflows to ship fast without cutting corners.</p>
    <p>AI エージェントを前提にした開発の進め方を組み立て、速く、品質を落とさずに作っています。</p>
    <p><a href="https://github.com/kukv">GitHub</a> · <a href="https://x.com/kukv">X</a> · <a href="https://bsky.app/profile/kukv.jp">Bluesky</a></p>
</main>
<noscript>
    <p>This site requires JavaScript. このサイトの表示には JavaScript が必要です。</p>
</noscript>
<script type="application/javascript" src="portfolio.js"></script>
</body>
</html>
```

`<noscript>` には要約を重複させず、案内だけを置く。JavaScript が無効でも `#static-summary` は表示されたまま残るため。

- [ ] **Step 2: styles.css に要約のスタイルを追加する**

`src/webMain/resources/styles.css` の末尾に追加する:

```css
#static-summary {
    font-family: system-ui, -apple-system, "Hiragino Sans", sans-serif;
    max-width: 640px;
    margin: 0 auto;
    padding: 30vh 16px 0;
    text-align: center;
    color: #221919;
}

#static-summary h1 {
    color: #8C1D2C;
    font-size: 2.5rem;
    margin: 0 0 8px;
}
```

- [ ] **Step 3: 起動後に要約を削除する**

`app/App.kt` の `App()` の先頭（`val viewModel` の次の行）に追加する:

```kotlin
    LaunchedEffect(Unit) { removeElementById("static-summary") }
```

import に `androidx.compose.runtime.LaunchedEffect` と `jp.kukv.portfolio.shared.lib.removeElementById` を追加する。

- [ ] **Step 4: 共有用画像を作る**

`build/og/og-image.html` を作る（`build/` は gitignore 済みのため、コミットされない）:

```html
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
  html, body { margin: 0; width: 1200px; height: 630px; }
  body {
    display: flex; flex-direction: column; justify-content: center;
    padding: 0 96px; box-sizing: border-box;
    background: #8C1D2C; color: #FFFFFF;
    font-family: "Hiragino Sans", system-ui, sans-serif;
  }
  .hi { font-size: 36px; opacity: .85; }
  .name { font-size: 112px; font-weight: 800; line-height: 1.05; margin: 8px 0 24px; }
  .role { font-size: 40px; }
  .tag { margin-top: 40px; display: inline-block; background: #9A4523; padding: 14px 28px; border-radius: 999px; font-size: 30px; align-self: flex-start; }
</style>
</head>
<body>
  <div class="hi">Hi, I'm</div>
  <div class="name">Nonaka Koki</div>
  <div class="role">Software Engineer</div>
  <div class="tag">Building with AI · AI と一緒に開発する</div>
</body>
</html>
```

Run:
```bash
mkdir -p build/og
"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" --headless=new --disable-gpu --hide-scrollbars --window-size=1200,630 --screenshot=src/webMain/resources/og-image.png build/og/og-image.html
```
Expected: `src/webMain/resources/og-image.png`（1200×630）ができる。`file src/webMain/resources/og-image.png` で `1200 x 630` と表示される。

画像を開いて、文字が切れずに収まっていることを確認する。

- [ ] **Step 5: 配布物を確認する**

Run: `./gradlew check wasmJsBrowserDistribution jsBrowserDistribution`
Expected: `BUILD SUCCESSFUL`

Run: `ls build/dist/wasmJs/productionExecutable/ | grep -E "index.html|og-image.png|styles.css"`
Expected: 3 つとも表示される。

- [ ] **Step 6: 表示を確認する**

Run: `./gradlew wasmJsBrowserDevelopmentRun`

次を確認する:
- 読み込み直後、一瞬だけ「Nonaka Koki」の静的な要約が表示され、アプリが起動すると消える。
- アプリ起動後、DevTools の Elements に `#static-summary` が存在しない。
- ページのソースを表示すると、description、OGP、JSON-LD が入っている。
- ページのソースの JSON-LD を [Schema Markup Validator](https://validator.schema.org/) の「コードスニペット」に貼り付けて、`Person` がエラーなく認識される。

- [ ] **Step 7: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/resources src/webMain/kotlin/jp/kukv/portfolio/app/App.kt
git commit -m "feat: 検索とリンクプレビュー向けに meta・OGP・構造化データ・静的な要約を追加"
```

---

### Task 14: 全体の検証

**Files:** なし（確認のみ）

- [ ] **Step 1: 自動チェック**

Run: `./gradlew clean check wasmJsBrowserDistribution jsBrowserDistribution`
Expected: `BUILD SUCCESSFUL`。`wasmJsTest` で `AppLanguageTest` 7 件と `DarkThemeTest` 5 件が成功し、`checkStringResources` も成功している。

- [ ] **Step 2: ブラウザでの確認（wasmJs）**

Run: `./gradlew wasmJsBrowserDevelopmentRun`

| # | 確認内容 | 期待 |
|---|---|---|
| 1 | 日本語ブラウザで初回表示 | JA、`<html lang="ja">`、タイトルが日本語 |
| 2 | EN に切り替え → 再読み込み | EN のまま |
| 3 | OS ダークで初回表示（localStorage を消してから） | ダーク |
| 4 | ライトに切り替え → 再読み込み | ライトのまま |
| 5 | 400 / 700 / 1000px | Bento が 1 / 2 / 3 列で崩れない |
| 6 | ヘッダーやドロワーの各リンク、Bento のリンクタイル | 該当セクションへスクロール |
| 7 | Contact で不正な入力 | エラー文言が選択中の言語で出る |
| 8 | ページのソース | description、OGP、JSON-LD、`#static-summary` がある |

- [ ] **Step 3: ブラウザでの確認（js）**

Run: `./gradlew jsBrowserDevelopmentRun`

表の 1、2、5 を確認する（js ターゲットでも `js()` のブラウザ連携と言語切り替えが動くこと）。

- [ ] **Step 4: 結果を報告する**

確認できなかった項目や想定と違った挙動があれば、そのまま報告する。
