# SeamlessCarouselComposer (Android MVP)

## 功能
- Photo Picker 一次导入 2~10 张静态图片。
- 预设输出比例：1080x1440、1080x1080、1080x1920。
- 基于 Bitmap/Canvas/Matrix 的横向分段硬切合成。
- 每张图独立调节 offsetX/offsetY/scale/rotation（-5°~+5°）。
- 导出 1 张 full 合成图 + N 张轮播分页图。
- MediaStore 保存到 `Pictures/SeamlessCarouselComposer/`。

## 构建与运行
1. 确保本机安装 JDK 17 与 Android SDK（建议直接使用最新版 Android Studio）。
2. 使用 Android Studio 打开项目并等待 Gradle Sync 完成。
3. 首次命令行构建前，给 Wrapper 增加执行权限：
   ```bash
   chmod +x gradlew
   ```
4. 命令行构建 Debug APK：
   ```bash
   gradle wrapper
   ./gradlew assembleDebug
   ```
   > 说明：仓库不提交 `gradle-wrapper.jar`（二进制文件），首次构建前请先执行 `gradle wrapper` 生成。
5. 连接 Android 10+ 设备后，可在 Android Studio 直接运行 `app` 模块。

## CI
- 仓库内置 GitHub Actions Android CI：`.github/workflows/android-ci.yml`。
- CI 会先执行 `gradle wrapper` 生成 `gradle-wrapper.jar`，再运行 `./gradlew --no-daemon assembleDebug`。
- 每次 push / pull request 都会执行 `assembleDebug`，并自动上传 `app-debug-apk` artifact 供下载。

## MVP 限制
- 当前接缝默认硬切；feather 参数已预留。
- 预览暂为基础版本，后续可增强分割线/标签叠加。
- 当前未实现项目持久化、手势编辑。

## 手工测试（3 张图）
1. 导入 3 张同背景照片。
2. 选择“小红书 3:4”。
3. 点击“刷新预览”再点击“导出”。
4. 在相册中检查输出：
   - full: `3240 x 1440`
   - page_01/page_02/page_03: `1080 x 1440`

## 常见问题
- 导入无响应：需选择至少 2 张图。
- 导出后看不到：在系统相册 `Pictures/SeamlessCarouselComposer` 下查找。
- 边界明显：MVP 为硬切，后续可启用 feather/自动对齐。
