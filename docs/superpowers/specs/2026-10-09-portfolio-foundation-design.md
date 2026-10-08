# ポートフォリオ刷新 ① 基盤 設計書

- 作成日: 2026-10-09
- 対象: サブプロジェクト ① 基盤（コード整理・日英対応・配色とデザイン・Home・検索対策）

## 背景と全体計画

ポートフォリオサイトを刷新する。技術スタック（Kotlin Multiplatform + Compose Multiplatform + Material 3 Expressive、js / wasmJs ターゲット）は変更しない。

刷新の要望は次の 4 つのサブプロジェクトに分け、この順に「設計 → 計画 → 実装」を回す。本書は ① のみを扱う。

| # | サブプロジェクト | 内容 |
|---|---|---|
| ① | 基盤 | コード整理、日英対応の仕組み、配色・デザイン刷新、Home の Bento 化、検索対策 |
| ② | コンテンツ | About Me / Skills & Stack / Experience の中身。Home タイルの本番文言 |
| ③ | Showcase | 作り直し |
| ④ | Contact | フォームを廃止し、GitHub・X・Zenn などへの導線を中心とした形に置き換え |

### サイトの方向性（② 以降の前提）

- **想定する読み手**: 転職先の採用担当者と、コミュニティの関係者。目的は「見つけてもらう」ことと「つながりを広げる」こと。
- **看板**: 「AI と一緒に開発するエンジニア」。AI エージェントを前提にした開発の進め方を自分で組み立て、速く、品質を落とさずに作れることを一番に打ち出す。
- **これからの軸**: AI を組み込んだプロダクト開発。完成品はまだないため看板にはせず、「いま取り組んでいること」として控えめに見せる。作品ができたら Showcase に載せ、看板に加える。
- **技術スタックの扱い**: 「何を使って作れるか」を示す裏付けとして後ろに置く。Kotlin 以外の開発もあるため、特定の言語を中心にした書き方はしない。

## ①-1 構成とコード整理

1 ページの縦スクロール構成（Home → About → Showcase → Contact）は維持する。

- **セクションの識別**: `"home"` などの文字列キーを `enum class Section { Home, About, Showcase, Contact }` に置き換える。ヘッダー、ドロワー、スクロール位置の管理（`sectionPositions`）はすべてこの enum で扱う。
- **画面サイズの判定**: `App.kt` の独自の `WindowSizeClass`（600 / 893dp）と `WindowSizeState` を廃止する。依存関係にある `compose-adaptive` の `currentWindowAdaptiveInfo().windowSizeClass` を使う（境界は標準の 600 / 840dp）。
- **Skills カードの重複解消**: `AboutScreen.SkillAndStacksSection` で 3 回コピペされているカードを `SkillCategoryCard` 1 つにまとめ、並べ方だけを画面サイズで切り替える。
- **設定の保持**: `AppViewModel` で「テーマ」と「言語」の 2 つの設定を管理する。
  - 初期値: `localStorage` に保存値があればそれを使い、なければブラウザの設定（テーマは `prefers-color-scheme`、言語は `navigator.language`）に従う。
  - ユーザーが切り替えたら `localStorage` に保存する。
  - ブラウザ API は既存の `shared/lib/BrowserUtils` の expect / actual（js / wasmJs）に追加する。
- **index.html**: 言語を切り替えたら `<html lang>` と `document.title` を更新する。
- **対象外**: 依存ライブラリのバージョン更新（Renovate に任せる）。Showcase と Contact の作り直し（③ と ④ で行う）。

## ①-2 多言語化

Compose Resources を使う。

- **リソース**: 英語を `values/strings.xml`（既定）、日本語を `values-ja/strings.xml` に置く。ブラウザの言語が日本語でも英語でもない場合は英語にする。
- **その場での切り替え**: 公式ドキュメント（Compose Multiplatform「Resource environment」）の方法に沿って実装する。
  - `index.html` で、アプリのスクリプトより前に `Navigator.prototype.languages` の getter を書き換え、`window.__customLocale` が設定されていればそれを返すようにする。
  - Kotlin 側に `LocalAppLocale` と `AppEnvironment` を用意する。言語が変わったら `window.__customLocale` を更新して `languagechange` イベントを発火させ、`key(locale)` で再構成する。
  - 公式が「一時的な回避策」としている方法のため、この処理は `shared/i18n/` に閉じ込め、公式 API ができたら差し替えやすくしておく。
- **切り替え UI**: デスクトップとタブレットはヘッダーのテーマ切り替えの隣に言語ボタン（`EN` / `JA`）を置く。モバイルはドロワー内に置く。
- **① で移す範囲**: 画面上の UI ラベルをすべてリソースに移し、日英両方を用意する。対象はナビゲーション、セクション見出し、ボタン（More / Close / Submit など）、フォームのラベル、入力エラーの文言、Snackbar、Home の各タイル。
- **① で移さないもの**: About・Experience・Showcase のダミーデータは今のコードのまま残す。② と ③ で実データに差し替えるとき、各項目が `StringResource` をキーとして持つ形でリソース化する。

## ①-3 テーマ

- **配色**: 主色 `#8C1D2C`（ワインレッド）、第 3 色（tertiary）`#B85C38`（テラコッタ）。Material Theme Builder 相当の手順で、ライトとダークの全カラーロール（`surfaceContainer*` と `tertiary*` を含む）を生成し、`shared/theme/Colors.kt` に定数で書く。
- **ダークモード**: ①-1 のとおり、OS の設定に従い、切り替えたら保存する。
- **Expressive の要素**:
  - `MaterialExpressiveTheme` に `MotionScheme.expressive()` を指定する。
  - 角丸は大きめにする（Bento のタイルは `extraLarge` 相当、28dp 前後）。
  - 写真は Material 3 Expressive のシェイプ（クッキー形など）で切り抜く。
- **フォント**: Noto Sans JP を Regular / Medium / Bold / ExtraBold の 4 つに絞る。Thin / ExtraLight / Light / SemiBold / Black のファイルと参照を削除する。名前や見出しには ExtraBold を使う。

## ①-4 Home（Bento グリッド）

| タイル | 中身 |
|---|---|
| 名前（大・縦 2 マス） | `Hi, I'm` / 名前 / 肩書き。主色の背景に白文字 |
| 写真 | 既存の `ProfileImage` を Expressive のシェイプで切り抜く |
| AI との開発 | 看板の一言と、その実践例への導線 |
| いま作っているもの | AI を組み込んだプロダクトの取り組み。未公開の間は「準備中」と表示する |
| ステータス | 既存の `StatusPill` を移す |
| Showcase へ | 押すと Showcase までスクロールする。テラコッタ色 |
| Contact へ | 押すと Contact までスクロールする |

- **画面幅による並び**: デスクトップ（840dp 以上）は 3 列、タブレット（600〜840dp）は 2 列、モバイルは 1 列で、名前 → 写真 → 残りの順に縦に並べる。
- **高さ**: 画面の高さに固定する今の作りをやめ、中身に合わせる。
- **動き**: 表示時にタイルが時間差でフェードとスケールで現れる。押したタイルは少し縮んで反応する。どちらも `MotionScheme` の値を使う。
- **文言**: 各タイルの文言は ①-2 の仕組みでリソース化する。① では暫定の文言を日英で入れ（今の「Software enggner」の誤字は直す）、本番の文章は ② で差し替える。

## ①-5 見つけてもらうための対応

Compose の Web 版は canvas に描画するため、検索エンジンや SNS のリンクプレビューは画面内の文章を読めない。そのため、`index.html` に静的な情報を書き込む。

- `<title>` と `<meta name="description">` に、名前と看板の一言を入れる。
- OGP タグと Twitter Card のタグ、共有用の画像 1 枚（1200×630）を用意する。
- JSON-LD の `Person` 構造化データ（名前、肩書き、`sameAs` に GitHub などのアカウント）を入れる。
- 自己紹介の要約を HTML のテキストとして `<div id="static-summary">` に置く（読み込み中の表示を兼ねる）。アプリの起動が終わったら Kotlin 側でこの要素を削除する。JavaScript が無効な環境向けに、同じ内容を `<noscript>` にも置く。
- 静的な HTML は言語切り替えに対応しないため、日英を併記する。
- 文言は ② で確定させる。① では枠組みと暫定の文言を入れる。

## ①-6 テストと検証

- **単体テスト**（`commonTest`）: 判定ロジックを純粋な関数に切り出してテストする。
  - 言語の決定: 保存値 → ブラウザの言語 → 英語、の順に決まること（例: `ja-JP` → JA、`en-US` → EN、`fr` → EN、保存値 `ja` → JA）。
  - テーマの決定: 保存値 → OS の設定、の順に決まること。
- **訳し漏れのチェック**: `values/strings.xml` と `values-ja/strings.xml` のキーの集合が一致するか確かめる Gradle タスクを作り、`check` に依存させて CI で失敗させる。
- **ビルド**: `wasmJsBrowserDistribution`、`jsBrowserDistribution`、`spotlessCheck` が通ること。
- **ブラウザでの確認**（開発サーバー）:
  - 日英の切り替えと、再読み込み後も設定が残ること。
  - ダークモードの切り替えと、再読み込み後も設定が残ること。
  - モバイル、タブレット、デスクトップの 3 幅で Bento が崩れないこと。
  - ページのソースに meta、OGP、JSON-LD が入っていること。
