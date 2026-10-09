# ポートフォリオ刷新 ② コンテンツ 設計書

- 作成日: 2026-10-09
- 対象: サブプロジェクト ② コンテンツ（About Me / Skills & Stack / Experience）
- 前提: [① 基盤 設計書](2026-10-09-portfolio-foundation-design.md) の「サイトの方向性」

## 背景と方針の変更

① の設計書では、② で About / Skills / Experience の本番の中身と Home タイルの本番文言を確定させる予定だった。しかし実際の経歴などを用意するには時間がかかるため、② の範囲を次のように変更する。

- **② でやること**: About / Skills / Experience の中身を JSON ファイルに出し、仮の値を入れる。本番の文章が用意できたら、JSON を書き換えるだけで差し替えられるようにする。
- **② でやらないこと**:
  - 本番の文章の作成（準備ができたら JSON を書き換える）
  - Home タイルの文言（今のまま strings.xml に残す）
  - `index.html` の description・OGP・noscript の要約（静的な HTML なので実行時に JSON を読めない。本番の文章を決めるときに手で直す）

Home の文言（`home_*`）は `screens/home/` の中でしか参照されていない。ただし `index.html` の要約は `home_ai_body` と同じ文章を、`app_title` は `home_name` と `home_role` を組み合わせた文字列を、それぞれ手でコピーしている。本番の文章に差し替えるときは、この 2 か所もあわせて直す。

### 公開範囲

経歴には在籍企業の名前を一切出さない。業界・規模・役割で書く（例:「医療系 SaaS 企業（社員 300 名規模）」）。

## ②-1 ファイルとデータ構成

- 置き場所: `src/webMain/composeResources/files/content/ja.json` と `en.json`。2 つのファイルは同じ構成とし、中身を言語ごとに書く。
- 構成:

```json
{
  "about": "自己紹介の本文（段落は \n\n で区切る）",
  "skills": [
    { "label": "Languages", "items": ["Kotlin", "TypeScript"] }
  ],
  "experiences": [
    {
      "period": "2022.04 — 現在",
      "role": "ソフトウェアエンジニア",
      "organization": "医療系 SaaS 企業（社員 300 名規模）",
      "description": "担当したことと成果",
      "technologies": ["Kotlin", "AWS"]
    }
  ]
}
```

- `skills` と `experiences` の件数・並び順は JSON の中で自由に変えられる。コードは件数を前提にしない。
- `organization`: 社名を伏せる方針のため、`company` ではなくこの名前にする。業界と規模を書く。
- `technologies`: 今の画面にない項目。技術スタックを「裏付けとして後ろに置く」方針に沿って、各経歴の末尾に小さく表示する。
- 仮の値: 今のダミー（Kotlin 中心の英文）は使わず、社名を伏せた形で日英の仮データを新しく作る。仮であることが分かる文言にする。

## ②-2 データ型

`screens/about/AboutContent.kt` に `@Serializable` のデータ型を置く。

```kotlin
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
    val organization: String,
    val description: String,
    val technologies: List<String>,
)
```

- 今 `AboutScreen.kt` にある `SkillCategory` と `Experience` はここへ移す。`SkillCategory.skills` は JSON に合わせて `items` に、`Experience.company` は `organization` に名前を変え、`technologies` を追加する。
- 依存として `kotlinx-serialization-json` と Kotlin のシリアライゼーションプラグインを追加する。

## ②-3 読み込みと言語の切り替え

- Compose Resources の `files/` は `values-ja` のような言語別の仕組みに対応していない。そのため表示中の言語に合わせて `files/content/${language.tag}.json` を自分で選び、`Res.readBytes` で読む。
- 今の言語は、既存の `LocalAppViewModel.current.language` から取る（`Layout.kt` と同じやり方）。言語のための CompositionLocal は新しく作らない。
- `AboutScreen` では `produceState` で JSON を読み込み、デコードする。言語を切り替えると `AppEnvironment` の `key(language)` で中身が作り直され、新しい言語の JSON を読み直す。
- 本文の表示は今のレイアウト（自己紹介のカード、Skills のグリッド、Experience のタイムライン）を引き継ぐ。変更点は次の 2 つ。
  - 自己紹介の本文は `\n\n` で段落に分けて表示する。
  - Experience の各項目に `technologies` を表示する。

## ②-4 読み込み中と失敗時

- **読み込み中**: セクション見出し（自己紹介・スキルと技術・経歴）はそのまま表示し、本文の部分は空にする。ファイルは小さく一瞬で読み終わるため、ローディング表示は付けない。
- **失敗時**（ファイルを取得できない、JSON を解析できない）: 本文は空のまま表示し、原因を `console.error` に出す。ページ全体は壊さない。

## ②-5 テスト

- **デコードの単体テスト**: サンプルの JSON 文字列が `AboutContent` に正しく変換されることを確かめる。
- **実ファイルの確認**: wasmJs のブラウザテスト（Karma）は Compose Resources のファイルを配信しない（`Res.readBytes` が 404 になることを確認済み）。そのため Gradle のタスクで `files/content/*.json` の中身を Kotlin の定数としてテスト用のソースに書き出し、テストから読む。テストでは次の 2 点を確かめる。
  - 各ファイルが `AboutContent` として解析できる。
  - ファイルが `AppLanguage` のすべての言語の分そろっている（アプリが読みに行く `${language.tag}.json` が必ずある）。
