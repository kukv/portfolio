# リファクタリング計画

## 現在のアーキテクチャ方針

**Feature-First + Android公式命名規則** をベースとしたハイブリッド構成を採用。
ViewModel 移行を見越し、Layered Architecture へ段階的に移行できる構造とする。

---

## フェーズ一覧

| Phase | 内容 | 状態 |
|-------|------|------|
| Phase 1 | パッケージ構造の整理 | ✅ 完了 |
| Phase 2 | ViewModel への移行 | ✅ 完了 |
| Phase 3 | domain / data レイヤーの追加 | 🔲 未着手（必要時） |

---

## Phase 1 — パッケージ構造の整理 ✅

フラットなパッケージ構成から以下へ移行済み。

```
src/webMain/kotlin/jp/kukv/portfolio/
├── app/              # エントリポイント・グローバル設定
│   ├── App.kt
│   ├── AppState.kt
│   ├── Main.kt
│   └── Theme.kt
├── screens/          # 画面単位
│   ├── home/HomeSection.kt
│   ├── about/AboutSection.kt
│   ├── showcase/ShowcaseSection.kt
│   └── contact/ContactSection.kt
├── components/       # アプリ固有の共有UIブロック
│   ├── Header.kt
│   ├── NavigationDrawer.kt
│   └── Footer.kt
└── shared/           # 基盤
    ├── theme/        # Colors.kt, Fonts.kt
    ├── layout/       # Layout.kt
    └── lib/          # BrowserUtils.kt, _extensions, _jsmodules
```

### 配置ルール

| 置き場 | 配置する対象 |
|--------|-------------|
| `app/` | エントリポイント・グローバル状態・テーマ |
| `screens/<name>/` | 画面単位の Composable（と将来の ViewModel） |
| `components/` | `LocalAppState` 等アプリ状態を参照するアプリ固有の共有ブロック |
| `shared/ui/` | アプリ状態非依存の純粋な再利用 Composable（→ 後述） |
| `shared/theme/` | カラー・タイポグラフィ定義 |
| `shared/layout/` | レイアウト Composable |
| `shared/lib/` | ユーティリティ関数・拡張関数・JS モジュール |

---

## shared/ui/ — 共通UIコンポーネントの追加

### 追加タイミング

以下に該当するコンポーネントが発生したタイミングで `shared/ui/` を追加する。

- `LocalAppState` など **アプリ固有の状態を参照しない**
- **props のみ** で動作する純粋な Composable
- **2画面以上** で使われる、または汎用的に再利用できる

### 配置場所

```
shared/
├── theme/
├── layout/
├── lib/
└── ui/               # ← 共通UIコンポーネント置き場
    ├── SectionTitle.kt   # セクション見出し（汎用）
    ├── SkillChip.kt      # スキルタグ（汎用）
    └── StatusPill.kt     # HomeSection.kt から切り出し候補
```

### components/ との使い分け

```kotlin
// ✅ shared/ui/ に置くもの — アプリ状態に依存しない
@Composable
fun StatusPill(text: String, modifier: Modifier = Modifier) {
    // LocalAppState を参照しない
}

// ✅ components/ に置くもの — アプリ状態を参照する
@Composable
fun DesktopHeader(onNavigate: (String) -> Unit) {
    val appState = LocalAppState.current  // アプリ固有の依存
}
```

### 現時点での切り出し候補

| 現在の場所 | 切り出し先 | 理由 |
|-----------|-----------|------|
| `HomeSection.kt` 内 `StatusPill` | `shared/ui/StatusPill.kt` | アプリ状態非依存・他画面でも使える可能性 |
| `AboutSection.kt` 内スキルタグ | `shared/ui/SkillChip.kt` | 純粋な見た目パーツ |
| 各セクションの見出し `Text` | `shared/ui/SectionTitle.kt` | 全画面で同じスタイルを使用 |

---

## Phase 2 — ViewModel への移行

### 目的

現在の `AppState`（モノリシックな CompositionLocal）を解体し、
責務ごとに分離された ViewModel に置き換える。

### 前提：KMP での ViewModel 利用

```toml
# libs.versions.toml
[versions]
lifecycle = "2.8.0"   # KMP 対応は 2.8.x 以降

[libraries]
lifecycle-viewmodel = { module = "androidx.lifecycle:lifecycle-viewmodel", version.ref = "lifecycle" }
lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version.ref = "lifecycle" }
```

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)
}
```

### 移行後のパッケージ構成

```
src/webMain/kotlin/jp/kukv/portfolio/
├── app/
│   ├── App.kt
│   ├── AppViewModel.kt          # ← NEW: テーマ・ナビゲーション等のグローバル状態
│   ├── Main.kt
│   └── Theme.kt
│   # AppState.kt は Step 4 で削除
├── screens/
│   ├── home/
│   │   ├── HomeSection.kt
│   │   └── HomeViewModel.kt     # ← NEW（状態があれば）
│   ├── about/
│   │   ├── AboutSection.kt
│   │   └── AboutViewModel.kt    # ← NEW（状態があれば）
│   ├── showcase/
│   │   ├── ShowcaseSection.kt
│   │   └── ShowcaseViewModel.kt # ← NEW: visibleCount, selectedProject 等
│   └── contact/
│       ├── ContactSection.kt
│       └── ContactViewModel.kt  # ← NEW: フォーム状態・バリデーション
├── components/
└── shared/
```

### ViewModel の責務分担

#### AppViewModel（グローバル）

```kotlin
class AppViewModel : ViewModel() {
    // テーマ
    var isDarkTheme by mutableStateOf(false)

    // ウィンドウサイズ
    var windowSizeState: WindowSizeState = ...

    // ナビゲーション
    val scrollState = ScrollState(0)
    val sectionPositions = mutableStateMapOf<String, Int>()
    val drawerState = DrawerState(DrawerValue.Closed)
    val snackbarHostState = SnackbarHostState()

    fun navigate(section: String) {
        viewModelScope.launch {
            val pos = sectionPositions[section] ?: 0
            scrollState.animateScrollTo(pos)
        }
    }
}
```

#### ShowcaseViewModel（画面固有）

```kotlin
class ShowcaseViewModel : ViewModel() {
    var visibleCount by mutableStateOf(8)
    var selectedProject by mutableStateOf<Project?>(null)

    fun loadMore() { visibleCount += 8 }
    fun selectProject(project: Project) { selectedProject = project }
    fun dismissProject() { selectedProject = null }
}
```

#### ContactViewModel（画面固有）

```kotlin
class ContactViewModel : ViewModel() {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var company by mutableStateOf("")
    var email by mutableStateOf("")
    var message by mutableStateOf("")
    var agreedToPrivacyPolicy by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    val isFormValid: Boolean get() = ...

    fun submit(onSuccess: () -> Unit) {
        viewModelScope.launch { ... }
    }
}
```

### AppState の解体ロードマップ

```
Step 1: AppViewModel を作成し AppState と並行稼働
         └── App() で AppViewModel を viewModel() で取得
         └── LocalAppState は残したまま AppViewModel の値を委譲

Step 2: 各 Screen に ViewModel を追加
         └── ShowcaseSection → ShowcaseViewModel
         └── ContactSection  → ContactViewModel
         └── HomeSection / AboutSection → ローカル状態がなければ ViewModel 不要

Step 3: AppState を AppViewModel に置き換え
         └── LocalAppState を廃止
         └── グローバル状態は AppViewModel を CompositionLocal 経由で提供

Step 4: AppState.kt を削除
```

### 移行時の注意点

- `rememberCoroutineScope()` は ViewModel の `viewModelScope` に置き換える
- `remember { }` で保持していた状態は ViewModel の `mutableStateOf` へ移す
- `CompositionLocalProvider` は `AppViewModel` を渡す形に変更する
- `HomeSection` / `AboutSection` はローカル状態を持たないため ViewModel 不要の可能性が高い

---

## Phase 3 — domain / data レイヤーの追加（必要時）

### 追加タイミング

以下のいずれかに該当した時点で追加する（現時点では不要）。

- 外部 API（REST / GraphQL 等）との通信が発生する
- ビジネスロジックが複数の ViewModel をまたぐ
- データのキャッシュ・永続化が必要になる

### 追加後のパッケージ構成

```
src/webMain/kotlin/jp/kukv/portfolio/
├── app/
├── screens/
├── components/
├── shared/
├── domain/                      # ← Phase 3 で追加
│   ├── model/
│   │   ├── Project.kt           # ShowcaseSection.kt から移動
│   │   ├── SkillCategory.kt     # AboutSection.kt から移動
│   │   └── Experience.kt        # AboutSection.kt から移動
│   └── usecase/
│       ├── GetProjectsUseCase.kt
│       └── SubmitContactUseCase.kt
└── data/                        # ← Phase 3 で追加
    └── repository/
        ├── ProjectRepository.kt
        └── ContactRepository.kt
```

### 依存方向（Layered Architecture）

```
screens/ ──► domain/ ──► data/
   │
   ▼
components/
   │
   ▼
shared/
```

- `screens/` は `domain/` の UseCase のみを参照する（`data/` を直接参照しない）
- `data/` は `domain/` のインターフェースを実装する（依存性逆転）

---

## 参考：現在の状態管理と移行後の対応表

| 現在（AppState） | Phase 2 移行先 |
|-----------------|---------------|
| `theme.isDarkTheme` | `AppViewModel.isDarkTheme` |
| `windowSize.*` | `AppViewModel.windowSizeState` |
| `navigation.scrollState` | `AppViewModel.scrollState` |
| `navigation.sectionPositions` | `AppViewModel.sectionPositions` |
| `navigation.drawerState` | `AppViewModel.drawerState` |
| `navigation.snackbarHostState` | `AppViewModel.snackbarHostState` |
| `navigation.navigate()` | `AppViewModel.navigate()` |
| ShowcaseSection の `visibleCount` | `ShowcaseViewModel.visibleCount` |
| ShowcaseSection の `selectedProject` | `ShowcaseViewModel.selectedProject` |
| ContactSection のフォーム状態 | `ContactViewModel.*` |