# ポートフォリオ刷新 ② コンテンツ 実装計画

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 設計書 `docs/superpowers/specs/2026-10-09-portfolio-content-design.md` を実装する。About / Skills / Experience の中身を `files/content/{ja,en}.json` に出し、仮の値を入れる。

**Architecture:**
- 中身は `@Serializable` のデータ型 `AboutContent` で表し、`kotlinx-serialization-json` で JSON から変換する。
- `AboutScreen` は `LocalAppViewModel.current.language` に合わせて `files/content/${language.tag}.json` を `Res.readBytes` で読む。失敗したら本文を空にし、原因を `console.error` に出す。
- Karma は Compose Resources のファイルを配信しないため、Gradle のタスクで JSON の中身をテスト用の Kotlin ソースに書き出し、実ファイルが解析できることを wasmJs のテストで確かめる。

**Tech Stack:** Kotlin 2.4.20 / Compose Multiplatform 1.12.1 / Compose Resources / kotlinx-serialization-json 1.11.0 / js・wasmJs ターゲット / spotless(ktlint)

---

## 共通ルール

- 作業ディレクトリはリポジトリのルート。コマンドはすべてルートから実行する。
- このリポジトリのパスには `github.com` が含まれるため、worktree のガードが複雑なシェルコマンドを拒否することがある。コマンドは 1 つずつ実行し、パスはルートからの相対パスで書く。パイプ、`&&`、ヒアドキュメント、`while` ループ、`$PWD` や `$HOME` を使ったコマンドは避ける。ファイルの作成と編集はエディタ（Write / Edit ツール）で行う。
- コミットの前に必ず `./gradlew spotlessApply` を実行する。
- コミットメッセージの末尾に次の行を付ける（以下のコマンド例では省略している）。
  ```
  Co-Authored-By: Claude Opus 5.5 (1M context) <noreply@anthropic.com>
  ```
- テストは `src/webTest/kotlin/` に置き、`./gradlew wasmJsTest` で実行する（js のブラウザテストは無効化してある）。`wasmJsTest` は `--tests` オプションに対応していないため、常に全テストを実行する。
- 実装前に確認した事項:
  - wasmJs のテストから `Res.readBytes` を呼ぶと `/composeResources/...` が 404 になり `MissingResourceException` が出る。
  - 依存グラフには kotlinx-datetime 経由で `kotlinx-serialization-core:1.7.3` が入っている。`kotlinx-serialization-json:1.11.0` を足すと core も上がる。

## ファイル構成

| ファイル | 役割 |
|---|---|
| `gradle/libs.versions.toml` | シリアライゼーションのプラグインとライブラリを追加 |
| `build.gradle.kts` | プラグインと依存の追加。JSON をテスト用ソースに書き出すタスク `generateContentFixtures` |
| `src/webMain/composeResources/files/content/ja.json` / `en.json` | About / Skills / Experience の中身（仮の値） |
| `src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutContent.kt` | データ型、JSON の変換、ファイルの読み込み |
| `src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutScreen.kt` | ダミーデータを消し、JSON の中身を表示する |
| `src/webMain/kotlin/jp/kukv/portfolio/screens/about/SkillCategoryCard.kt` | `skills` → `items` の名前変更に合わせる |
| `src/webMain/kotlin/jp/kukv/portfolio/shared/lib/Browser.kt` | `console.error` を呼ぶ `logError` を追加 |
| `src/webTest/kotlin/jp/kukv/portfolio/screens/about/AboutContentTest.kt` | JSON の変換の単体テスト |
| `src/webTest/kotlin/jp/kukv/portfolio/screens/about/ContentFilesTest.kt` | 実際の `ja.json` / `en.json` のテスト |

---

### Task 1: シリアライゼーションを追加する

依存の相性の問題があれば、データ型を書く前にここで分かるようにする。

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `build.gradle.kts`

- [ ] **Step 1: バージョンカタログにプラグインとライブラリを追加する**

`gradle/libs.versions.toml` の `[plugins]` で、`compose-compiler` の行の直後に追加する。

```toml
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin_version" }
```

`[libraries]` で、`kotlinx-datetime` の行の直後に追加する。

```toml
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version = { strictly = "1.11.0" } }
```

- [ ] **Step 2: build.gradle.kts でプラグインと依存を有効にする**

`plugins { }` の `alias(libs.plugins.compose.compiler)` の直後に追加する。

```kotlin
    alias(libs.plugins.kotlin.serialization)
```

`commonMain.dependencies { }` の `implementation(libs.kotlinx.datetime)` の直後に追加する。

```kotlin
            implementation(libs.kotlinx.serialization.json)
```

- [ ] **Step 3: 両ターゲットでビルドが通ることを確かめる**

Run: `./gradlew compileKotlinWasmJs compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

失敗した場合は、エラーの内容を報告して止まる（バージョンを勝手に変えない）。

- [ ] **Step 4: 既存のテストが通ることを確かめる**

Run: `./gradlew wasmJsTest`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 5: コミット**

```bash
./gradlew spotlessApply
git add gradle/libs.versions.toml build.gradle.kts
git commit -m "build: kotlinx-serialization-json を追加する"
```

`kotlin-js-store/` の lock ファイルが変わっていたら、それもコミットに含める（`git status` で確認する）。

---

### Task 2: データ型と JSON の変換

**Files:**
- Create: `src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutContent.kt`
- Modify: `src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutScreen.kt`（古いデータ型を消し、ダミーデータを新しい項目名に合わせる）
- Modify: `src/webMain/kotlin/jp/kukv/portfolio/screens/about/SkillCategoryCard.kt`
- Test: `src/webTest/kotlin/jp/kukv/portfolio/screens/about/AboutContentTest.kt`

- [ ] **Step 1: 失敗するテストを書く**

`src/webTest/kotlin/jp/kukv/portfolio/screens/about/AboutContentTest.kt`:

```kotlin
package jp.kukv.portfolio.screens.about

import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AboutContentTest {
    @Test
    fun decodesAllFields() {
        val text =
            """
            {
              "about": "First paragraph.\n\nSecond paragraph.",
              "skills": [
                { "label": "Languages", "items": ["Kotlin", "TypeScript"] }
              ],
              "experiences": [
                {
                  "period": "2022.04 — Present",
                  "role": "Software Engineer",
                  "organization": "SaaS company (300 employees)",
                  "description": "Built things.",
                  "technologies": ["Kotlin"]
                }
              ]
            }
            """.trimIndent()

        val expected =
            AboutContent(
                about = "First paragraph.\n\nSecond paragraph.",
                skills = listOf(SkillCategory(label = "Languages", items = listOf("Kotlin", "TypeScript"))),
                experiences =
                    listOf(
                        Experience(
                            period = "2022.04 — Present",
                            role = "Software Engineer",
                            organization = "SaaS company (300 employees)",
                            description = "Built things.",
                            technologies = listOf("Kotlin"),
                        ),
                    ),
            )
        assertEquals(expected, decodeAboutContent(text))
    }

    @Test
    fun missingFieldFails() {
        val text = """{ "about": "Hi", "skills": [] }"""

        assertFailsWith<SerializationException> { decodeAboutContent(text) }
    }

    @Test
    fun unknownFieldFails() {
        val text = """{ "about": "Hi", "skills": [], "experiences": [], "company": "X" }"""

        assertFailsWith<SerializationException> { decodeAboutContent(text) }
    }
}
```

`unknownFieldFails` は、JSON の項目名の書き間違い（例: `organization` を `company` と書く）に気づけるようにするためのテスト。`Json` の既定の設定では未知の項目はエラーになる。

- [ ] **Step 2: テストが失敗することを確かめる**

Run: `./gradlew wasmJsTest`
Expected: コンパイルエラー（`AboutContent` / `decodeAboutContent` が見つからない）

- [ ] **Step 3: データ型と変換関数を書く**

`src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutContent.kt`:

```kotlin
package jp.kukv.portfolio.screens.about

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** About セクションの中身。files/content/{言語}.json に置く。 */
@Serializable
data class AboutContent(
    val about: String,
    val skills: List<SkillCategory>,
    val experiences: List<Experience>,
)

@Serializable
data class SkillCategory(val label: String, val items: List<String>)

@Serializable
data class Experience(
    val period: String,
    val role: String,
    // 社名は出さず、業界と規模を書く。
    val organization: String,
    val description: String,
    val technologies: List<String>,
)

fun decodeAboutContent(text: String): AboutContent = Json.decodeFromString(text)
```

- [ ] **Step 4: AboutScreen.kt の古いデータ型を消し、ダミーデータを合わせる**

`src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutScreen.kt` から次の 2 つの定義を削除する（`AboutContent.kt` の定義を使う）。

```kotlin
data class SkillCategory(val label: String, val skills: List<String>)

data class Experience(
    val period: String,
    val role: String,
    val company: String,
    val description: String,
)
```

ダミーの `experiences` の 3 件それぞれで、`company = ` を `organization = ` に変え、`description = ...,` の直後に `technologies = emptyList(),` を追加する。ダミーデータは Task 4 で消すので、ここでは最小限の変更にとどめる。`skillCategories` は位置引数で書かれているので変更不要。

- [ ] **Step 5: SkillCategoryCard.kt を新しい項目名に合わせる**

`src/webMain/kotlin/jp/kukv/portfolio/screens/about/SkillCategoryCard.kt` の

```kotlin
                category.skills.forEach { skill ->
```

を次に変える。

```kotlin
                category.items.forEach { skill ->
```

- [ ] **Step 6: テストが通ることを確かめる**

Run: `./gradlew wasmJsTest`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 7: js ターゲットもコンパイルできることを確かめる**

Run: `./gradlew compileKotlinJs`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 8: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin/jp/kukv/portfolio/screens/about src/webTest/kotlin/jp/kukv/portfolio/screens/about
git commit -m "feat: About の中身を JSON から変換するデータ型を追加する"
```

---

### Task 3: JSON ファイルと実ファイルのテスト

**Files:**
- Modify: `build.gradle.kts`（`generateContentFixtures` タスク）
- Create: `src/webMain/composeResources/files/content/ja.json`
- Create: `src/webMain/composeResources/files/content/en.json`
- Test: `src/webTest/kotlin/jp/kukv/portfolio/screens/about/ContentFilesTest.kt`

- [ ] **Step 1: 失敗するテストを書く**

`src/webTest/kotlin/jp/kukv/portfolio/screens/about/ContentFilesTest.kt`:

```kotlin
package jp.kukv.portfolio.screens.about

import jp.kukv.portfolio.shared.i18n.AppLanguage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** files/content/ の実ファイルを確かめる。contentFixtures は build.gradle.kts の generateContentFixtures が生成する。 */
class ContentFilesTest {
    @Test
    fun everyLanguageHasContentFile() {
        assertEquals(AppLanguage.entries.map { it.tag }.toSet(), contentFixtures.keys)
    }

    @Test
    fun everyContentFileDecodes() {
        for ((name, text) in contentFixtures) {
            val result = runCatching { decodeAboutContent(text) }
            assertTrue(result.isSuccess, "$name.json を解析できません: ${result.exceptionOrNull()?.message}")
        }
    }
}
```

- [ ] **Step 2: テストが失敗することを確かめる**

Run: `./gradlew wasmJsTest`
Expected: コンパイルエラー（`contentFixtures` が見つからない）

- [ ] **Step 3: JSON をテスト用ソースに書き出すタスクを追加する**

`build.gradle.kts` の末尾（`tasks.named("check") { dependsOn(checkStringResources) }` の後）に追加する。

```kotlin
// wasmJs のブラウザテスト(Karma)は Compose Resources のファイルを配信しないため、
// files/content/*.json の中身をテスト用の Kotlin ソースに書き出し、実ファイルが解析できることをテストで確かめる。
val generateContentFixtures =
    tasks.register("generateContentFixtures") {
        val contentDir = layout.projectDirectory.dir("src/webMain/composeResources/files/content")
        val outputDir = layout.buildDirectory.dir("generated/contentFixtures")
        inputs.dir(contentDir)
        outputs.dir(outputDir)
        doLast {
            val entries =
                contentDir.asFile
                    .listFiles { file -> file.extension == "json" }
                    .orEmpty()
                    .sortedBy { it.name }
                    .joinToString("") { file ->
                        // raw string の中で $ がテンプレートとして解釈されないようにする。
                        val text = file.readText().replace("$", "\${'$'}")
                        "    \"${file.nameWithoutExtension}\" to \"\"\"$text\"\"\",\n"
                    }
            val output = outputDir.get().file("jp/kukv/portfolio/screens/about/ContentFixtures.kt").asFile
            output.parentFile.mkdirs()
            output.writeText(
                "package jp.kukv.portfolio.screens.about\n\n" +
                    "/** ファイル名(拡張子なし) → JSON の中身。 */\n" +
                    "val contentFixtures: Map<String, String> = mapOf(\n$entries)\n",
            )
        }
    }

kotlin.sourceSets.named("webTest") { kotlin.srcDir(generateContentFixtures) }
```

`kotlin.srcDir(タスク)` にするとタスクの出力ディレクトリがソースになり、テストのコンパイル前にタスクが自動で実行される。

- [ ] **Step 4: JSON がまだ無いのでテストが失敗することを確かめる**

Run: `./gradlew wasmJsTest`
Expected: `ContentFilesTest.everyLanguageHasContentFile` が FAILED（`contentFixtures` が空）。コンパイルエラーではないこと。

- [ ] **Step 5: ja.json を作る**

`src/webMain/composeResources/files/content/ja.json`:

```json
{
  "about": "（仮）ここに自己紹介の本文が入ります。AI エージェントと一緒に開発を進めるソフトウェアエンジニアであることを、最初の段落で伝えます。\n\n（仮）2 段落目には、これまでの経験と、いま取り組んでいることを書きます。",
  "skills": [
    { "label": "Languages", "items": ["Kotlin", "TypeScript", "Java"] },
    { "label": "Frontend / Mobile", "items": ["Compose Multiplatform", "React"] },
    { "label": "Backend / Infra", "items": ["Ktor", "PostgreSQL", "Docker", "GitHub Actions"] },
    { "label": "AI", "items": ["Claude Code", "Codex"] }
  ],
  "experiences": [
    {
      "period": "2022.04 — 現在",
      "role": "（仮）ソフトウェアエンジニア",
      "organization": "（仮）SaaS 企業（社員 300 名規模）",
      "description": "（仮）担当したことと、その成果を書きます。",
      "technologies": ["Kotlin", "TypeScript", "AWS"]
    },
    {
      "period": "2018.04 — 2022.03",
      "role": "（仮）ソフトウェアエンジニア",
      "organization": "（仮）受託開発企業（社員 50 名規模）",
      "description": "（仮）担当したことと、その成果を書きます。",
      "technologies": ["Java", "Spring Boot"]
    }
  ]
}
```

- [ ] **Step 6: en.json を作る**

`src/webMain/composeResources/files/content/en.json`:

```json
{
  "about": "(Placeholder) An introduction goes here. The first paragraph explains that I am a software engineer who builds with AI agents.\n\n(Placeholder) The second paragraph covers my experience so far and what I am working on now.",
  "skills": [
    { "label": "Languages", "items": ["Kotlin", "TypeScript", "Java"] },
    { "label": "Frontend / Mobile", "items": ["Compose Multiplatform", "React"] },
    { "label": "Backend / Infra", "items": ["Ktor", "PostgreSQL", "Docker", "GitHub Actions"] },
    { "label": "AI", "items": ["Claude Code", "Codex"] }
  ],
  "experiences": [
    {
      "period": "2022.04 — Present",
      "role": "(Placeholder) Software Engineer",
      "organization": "(Placeholder) SaaS company (about 300 employees)",
      "description": "(Placeholder) What I worked on and what it achieved.",
      "technologies": ["Kotlin", "TypeScript", "AWS"]
    },
    {
      "period": "2018.04 — 2022.03",
      "role": "(Placeholder) Software Engineer",
      "organization": "(Placeholder) Contract development company (about 50 employees)",
      "description": "(Placeholder) What I worked on and what it achieved.",
      "technologies": ["Java", "Spring Boot"]
    }
  ]
}
```

- [ ] **Step 7: テストが通ることを確かめる**

Run: `./gradlew wasmJsTest`
Expected: `BUILD SUCCESSFUL`

- [ ] **Step 8: テストが実ファイルの誤りを検出することを確かめる**

`en.json` の `"organization"` を 1 か所だけ `"company"` に書き換え、`./gradlew wasmJsTest` を実行する。
Expected: `ContentFilesTest.everyContentFileDecodes` が FAILED になり、メッセージに `en.json を解析できません` が含まれる。

確認したら `en.json` を元に戻し、もう一度 `./gradlew wasmJsTest` が `BUILD SUCCESSFUL` になることを確かめる。

- [ ] **Step 9: コミット**

```bash
./gradlew spotlessApply
git add build.gradle.kts src/webMain/composeResources/files src/webTest/kotlin/jp/kukv/portfolio/screens/about/ContentFilesTest.kt
git commit -m "feat: About の中身を JSON ファイルに出し、仮の値を入れる"
```

---

### Task 4: About 画面で JSON を読み込んで表示する

**Files:**
- Modify: `src/webMain/kotlin/jp/kukv/portfolio/shared/lib/Browser.kt`
- Modify: `src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutContent.kt`
- Modify: `src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutScreen.kt`（全体を置き換える）

読み込みと表示は Compose とブラウザに依存するため、単体テストは書かない。Task 5 でブラウザで確かめる。

- [ ] **Step 1: console.error を呼ぶ関数を追加する**

`src/webMain/kotlin/jp/kukv/portfolio/shared/lib/Browser.kt` の末尾に追加する。

```kotlin

fun logError(message: String) {
    js("console.error(message)")
}
```

- [ ] **Step 2: ファイルを読み込む関数を追加する**

`src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutContent.kt` の import を次のように変え、

```kotlin
import jp.kukv.portfolio.shared.i18n.AppLanguage
import jp.kukv.portfolio.shared.lib.logError
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import portfolio.generated.resources.Res
import kotlin.coroutines.cancellation.CancellationException
```

ファイルの末尾に追加する。

```kotlin

/** 表示中の言語の JSON を読む。取得や解析に失敗したら原因をコンソールに出して null を返す。 */
@OptIn(ExperimentalResourceApi::class)
suspend fun loadAboutContentOrNull(language: AppLanguage): AboutContent? {
    val path = "files/content/${language.tag}.json"
    return try {
        decodeAboutContent(Res.readBytes(path).decodeToString())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logError("$path を読み込めませんでした: ${e.message}")
        null
    }
}
```

`CancellationException` を先に投げ直すのは、言語の切り替えで読み込みが中断されたときにエラーとして扱わないため。

- [ ] **Step 3: AboutScreen.kt を置き換える**

`src/webMain/kotlin/jp/kukv/portfolio/screens/about/AboutScreen.kt` の中身を次に置き換える。ダミーデータ（`skillCategories` と `experiences`）は削除する。レイアウトは今のものを引き継ぎ、変更点は次の 3 つ。
- 各セクションは中身を引数で受け取る。中身が無い（読み込み中・失敗）ときは見出しだけを表示する。
- 自己紹介の本文を `\n\n` で段落に分ける。
- 経歴の各項目の末尾に `technologies` を表示する。

```kotlin
package jp.kukv.portfolio.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.app.LocalAppViewModel
import jp.kukv.portfolio.shared.layout.LayoutSize
import jp.kukv.portfolio.shared.layout.currentLayoutSize
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.about_title
import portfolio.generated.resources.experience_title
import portfolio.generated.resources.skills_title

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val language = LocalAppViewModel.current.language
    // 読み込み中と失敗時は null。そのあいだは各セクションの見出しだけを表示する。
    val content by produceState<AboutContent?>(initialValue = null, language) {
        value = loadAboutContentOrNull(language)
    }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AboutMeSection(content?.about)

        Spacer(modifier = Modifier.height(60.dp))

        SkillAndStacksSection(content?.skills.orEmpty())

        Spacer(modifier = Modifier.height(60.dp))

        ExperienceSection(content?.experiences.orEmpty())
    }
}

@Composable
fun AboutMeSection(about: String?) {
    Text(
        text = stringResource(Res.string.about_title),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    if (about == null) return
    Spacer(modifier = Modifier.height(24.dp))
    Surface(
        modifier = Modifier.widthIn(max = 800.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            about.split("\n\n").forEach { paragraph ->
                Text(text = paragraph, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun SkillAndStacksSection(skills: List<SkillCategory>) {
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
        skills.chunked(columns).forEach { rowCategories ->
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

@Composable
fun ExperienceSection(experiences: List<Experience>) {
    Text(
        text = stringResource(Res.string.experience_title),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Column(modifier = Modifier.widthIn(max = 800.dp).fillMaxWidth()) {
        experiences.forEachIndexed { index, exp ->
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(24.dp).fillMaxHeight(),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .padding(top = 6.dp)
                                .size(10.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                    )
                    if (index < experiences.size - 1) {
                        Box(
                            modifier =
                                Modifier
                                    .width(1.dp)
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        )
                    }
                }
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(
                                start = 16.dp,
                                bottom = if (index < experiences.size - 1) 32.dp else 0.dp,
                            ),
                ) {
                    Text(
                        text = exp.period,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exp.role,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = exp.organization,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exp.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                    if (exp.technologies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = exp.technologies.joinToString(" · "),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }
}
```

- [ ] **Step 4: 両ターゲットでビルドとテストが通ることを確かめる**

Run: `./gradlew check`
Expected: `BUILD SUCCESSFUL`（spotlessCheck、checkStringResources、wasmJsTest を含む）

- [ ] **Step 5: コミット**

```bash
./gradlew spotlessApply
git add src/webMain/kotlin
git commit -m "feat: About・Skills・Experience を言語ごとの JSON から読み込んで表示する"
```

---

### Task 5: ブラウザで確かめる

**Files:** なし（問題が見つかったときだけ修正する）

- [ ] **Step 1: 開発サーバーを起動する**

Run（バックグラウンドで実行する）: `./gradlew wasmJsBrowserDevelopmentRun`
Expected: `http://localhost:8080/` で表示できる（ポートはログに出る値を使う）。

- [ ] **Step 2: 表示を確かめる**

ブラウザで開き、About セクションまでスクロールして次を確かめる。
- 自己紹介が 2 段落で表示される。
- Skills に 4 つのカード、Experience に 2 件が表示され、各経歴の末尾に技術名が `·` 区切りで出る。
- ヘッダーの言語切り替えで日英を行き来すると、中身がその言語の JSON に切り替わる。
- 開発者ツールのコンソールに `を読み込めませんでした` のエラーが出ていない。

- [ ] **Step 3: 失敗時の表示を確かめる**

`src/webMain/composeResources/files/content/en.json` の先頭の `{` を一時的に消し、英語表示で開き直す。
Expected: 見出し（About Me / Skills & Stack / Experience に相当する英語の見出し）だけが表示され、ページの他の部分は普通に表示される。コンソールに `files/content/en.json を読み込めませんでした` が出る。

確認したら `en.json` を元に戻す。`git status` で `en.json` に差分が無いことを確かめる。

- [ ] **Step 4: 開発サーバーを止める**
