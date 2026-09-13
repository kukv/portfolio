import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.kotlin.multiplatform)

    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.spotless)
}

kotlin {
    val javaVersion = libs.versions.java.get()
    jvmToolchain(javaVersion.toInt())

    js(IR) {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.material.icons.extended)
            implementation(libs.compose.adaptive)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.material3.adaptive.navigation.suite)

            implementation(libs.compose.ui.tooling.preview)

            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.compose)

            implementation(npm("@js-joda/timezone", "2.3.0"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// Kotlin/JS ツールチェーンが生成する yarn.lock 内の脆弱な npm 依存を
// Yarn resolutions で修正版へ固定する。OSV(security ゲート)が kotlin-js-store/yarn.lock で
// 検出した既知脆弱性への対処。lockfile は
// `./gradlew kotlinUpgradeYarnLock --rerun-tasks` で再生成する(--rerun-tasks が無いと
// Gradle が UP-TO-DATE と判定して lock が更新されない)。
// 各バージョンは OSV が提示する fixed version(複数該当は最大値)を採用。
val jsYarnResolutions =
    mapOf(
        "baseline-browser-mapping" to "2.11.0", // GHSA-w5vr-8v7q-w6rv
        "body-parser" to "1.20.6", // GHSA-v422-hmwv-36x6
        // 1.x(minimatch@3 経由)と 2.x(minimatch@9 経由)が併存するが、
        // yarn v1 の resolutions はバージョン別に書き分けられないため 2.x に一本化する。
        // brace-expansion 2.x は 1.x と同一の API(expand)で drop-in 互換。
        "brace-expansion" to "2.1.4", // GHSA-f886-m6hf-6m8v / GHSA-3jxr-9vmj-r5cp / GHSA-mh99-v99m-4gvg / GHSA-rgw5-rvv9-x895
        "browserslist" to "4.28.7", // GHSA-73wf-gq98-2v4g
        "diff" to "8.0.3", // GHSA-73rr-hh4g-fpgx
        "engine.io" to "6.6.7", // GHSA-gr94-w7qr-f4j3
        // GHSA-q3j6-qgpj-74h6 / GHSA-v39h-62p7-jpjc / GHSA-4c8g-83qw-93j6 /
        // GHSA-v2hh-gcrm-f6hx / GHSA-7p8r-x3mc-p8w7 / GHSA-f65p-4m7j-42xc /
        // GHSA-5jgf-p345-68v8 / GHSA-qw65-cvwx-89v3
        // GHSA-qw65-cvwx-89v3 はまだ global advisory DB に未掲載で OSV は検知しない。
        // 上流(fastify/fast-uri)の v3.1.7 リリースノートを根拠に先行して上げる。
        "fast-uri" to "3.1.7",
        "flatted" to "3.4.2", // GHSA-25h7-pfq9-p65f / GHSA-rf6f-7fwh-wjgh
        "follow-redirects" to "1.16.0", // GHSA-r4q5-vmmm-2653
        "http-proxy-middleware" to "2.0.10", // GHSA-64mm-vxmg-q3vj
        "js-yaml" to "4.3.2", // GHSA-h67p-54hq-rp68 / GHSA-52cp-r559-cp3m / GHSA-5p4m-2wfm-xmqj / GHSA-2883-xcg3-v3hh
        "launch-editor" to "2.14.1", // GHSA-v6wh-96g9-6wx3
        "lodash" to "4.18.0", // GHSA-f23m-r3pf-42rh
        // 3.x と 9.x が併存するが書き分けられないため 9.x に一本化する。
        "minimatch" to "9.0.7", // GHSA-3ppc-4f35-3m26 / GHSA-23c5-xmqv-rm74
        "node-forge" to "1.4.0", // GHSA-2328-f5f3-gj25
        "path-to-regexp" to "0.1.13", // GHSA-37ch-88jc-xwx2
        "picomatch" to "2.3.2", // GHSA-3v7f-55p6-f55p
        "qs" to "6.16.0", // GHSA-q8mj-m7cp-5q26 / GHSA-4mjr-xmp4-gh2g
        "serialize-javascript" to "7.0.5", // GHSA-5c6j-r48x-rmvq / GHSA-qj8w-gfj5-8c6v
        "shell-quote" to "1.9.0", // GHSA-w7jw-789q-3m8p / GHSA-395f-4hp3-45gv
        "socket.io-parser" to "4.2.7", // GHSA-677m-j7p3-52f9 / GHSA-2m8v-j782-fhvr
        "tmp" to "0.2.7", // GHSA-ph9p-34f9-6g65 / GHSA-7c78-jf6q-g5cm
        // GHSA-qmq6-f8pr-cx5x(low / Duplicate Advisory)は 14.0.0 でしか修正されず、
        // resolution でのメジャー跨ぎは webpack 系ビルドツールを壊すリスクが大きいため見送る。
        "uuid" to "11.1.1", // GHSA-w5hq-g745-h8pq
        "webpack" to "5.104.1", // GHSA-38r7-794h-5758 / GHSA-8fgc-7cc6-rx7x
        "webpack-dev-server" to "5.2.6", // GHSA-79cf-xcqc-c78w / GHSA-mx8g-39q3-5c79 / GHSA-f5vj-f2hx-8m93
        "websocket-driver" to "0.7.5", // GHSA-mp7j-qc5w-4988
        "ws" to "8.21.0", // GHSA-58qx-3vcg-4xpx / GHSA-96hv-2xvq-fx4p
    )

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().apply {
        jsYarnResolutions.forEach { (name, version) -> resolution(name, version) }
    }
}

spotless {
    kotlin {
        ktlint()
        target("**/*.kt")
        targetExclude("build/**/*.kt", "bin/**/*.kt")
    }

    kotlinGradle {
        ktlint()
        target("**/*.kts")
        targetExclude("build/**/*.kts", "bin/**/*.kts")
    }
}
