# SeamlessCarouselComposer (Android MVP)

## 功能
- Photo Picker 一次导入 2~10 张静态图片。
- 预设输出比例：1080x1440、1080x1080、1080x1920。
- 基于 Bitmap/Canvas/Matrix 的横向分段硬切合成。
- 每张图独立调节 offsetX/offsetY/scale/rotation（-5°~+5°）。
- 导出 1 张 full 合成图 + N 张轮播分页图。
- MediaStore 保存到 `Pictures/SeamlessCarouselComposer/`。

## 运行
1. 用 Android Studio 打开项目。
2. Sync Gradle。
3. 连接 Android 10+ 设备运行 `app`。

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
