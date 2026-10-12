
# Eight Wastelands

**Eight Wastelands** 是一个基于 **Minecraft 1.21.1 NeoForge** 开发的内容型 Mod。

本 Mod 以“灾劫、仙器、鬼器”为核心设计方向，为玩家加入具有特殊代价与机制的装备、状态效果、武器以及独立的装备系统。

与单纯增加高属性装备不同，Eight Wastelands 更强调装备本身带来的规则变化、风险与特殊玩法。

---

## ✨ Features

### 九重天罚

“九重天罚”是本 Mod 的核心特殊装备之一。

装备后，玩家会受到多种灾劫效果影响，并被迫改变原本的生存方式。

主要机制包括：

- 周期性天雷攻击
- 限制玩家跳跃
- 降低玩家造成的伤害
- 持续获得采掘疲劳
- 限制部分食物恢复效果
- 受到伤害后获得反胃效果
- 改变部分生物掉落规则
- 限制村民交易
- 多种独立灾劫状态效果

目前包含九种灾劫：

- 天劫
- 地灾
- 因果
- 心魔
- 绝灵
- 枯血
- 夺运
- 风煞
- 寿尽

九重天罚只能装备在 Mod 提供的特殊 **Calamity** 槽位中。

---

## ⚔️ Special Equipment

### 春剪

一件以樱花与剪刀为设计元素的特殊武器。

主要能力包括：

- 首次有效命中可对目标造成高额伤害
- 对目标施加特殊状态
- 具有独立冷却机制

适合对付高生命值敌人，但不能无条件连续触发。

---

### 遗忘

“遗忘”是一件后期鬼器。

它拥有独特的生命回溯机制：

- 玩家受到非致命、非虚空伤害后
- 一段时间后恢复至受伤前的生命状态

同时，“遗忘”会排斥部分正面状态效果，使玩家无法单纯依赖大量增益效果进行战斗。

---

## 👻 Ghost Artifacts

鬼器是一类具有特殊自我修复机制的装备。

鬼器拥有耐久，但耐久耗尽后不会直接消失，而是进入失效状态。

鬼器会尝试通过以下方式修复自身：

1. 优先消耗玩家经验
2. 经验不足时消耗玩家生命
3. 修复过程不会直接杀死玩家

鬼器无法通过普通方式或楼砧进行维修。

---

## ☁️ Immortal Artifacts

仙器同样拥有耐久系统。

当仙器耐久耗尽后：

- 装备不会消失
- 装备暂时失效
- 必须通过特殊方块进行修复

---

## 🔨 楼砧

楼砧是 Eight Wastelands 新增的特殊功能方块。

其定位类似于 Minecraft 原版铁砧，但主要用于处理本 Mod 中的仙器。

主要用途：

- 修复损坏的仙器
- 恢复已经失效的仙器装备

鬼器无法通过楼砧进行修复。

---

## 💀 Calamity Slot

Eight Wastelands 使用 Curios API 提供独立装备系统。

新增特殊装备槽：

`calamity`

该槽位专门用于装备灾劫类物品。

特点：

- 普通饰品无法装备至该槽位
- 灾劫装备无法放入普通 Curios 槽位
- 特定灾劫装备在生存模式下无法随意移除
- 创造模式下可以进行管理

---

## 📦 Requirements

运行本 Mod 需要：

- Minecraft `1.21.1`
- NeoForge
- Curios API
- Eight Wastelands

请确保安装正确版本的 Curios API，否则部分特殊装备与槽位功能将无法正常运行。

---

## 📥 Installation

1. 安装 Minecraft 1.21.1
2. 安装对应版本的 NeoForge
3. 下载并安装 Curios API
4. 下载 Eight Wastelands
5. 将 Mod 文件放入：

```text
.minecraft/mods/
```

6. 启动游戏

如果游戏能够正常加载，并且 Mod 列表中出现 Eight Wastelands，即代表安装成功。

---

## 🛠 Development

Eight Wastelands 使用：

- Java
- Minecraft 1.21.1
- NeoForge
- Curios API
- Gradle

项目主要涉及：

- 自定义物品
- 自定义武器
- 自定义方块
- Curios 自定义槽位
- MobEffect 状态系统
- 玩家事件监听
- 战斗机制
- 掉落规则修改
- 装备耐久系统
- 自定义修复机制

---

## 📂 Build

克隆项目：

```bash
git clone <https://github.com/piaoxue8042-coder/eightwastelands_26.1.2>
```

进入项目目录：

```bash
cd EightWastelands
```

Windows：

```bash
gradlew build
```

Linux / macOS：

```bash
./gradlew build
```

构建完成后的 Mod 文件通常可以在：

```text
build/libs/
```

中找到。

---

## 🐛 Bug Reports

如果你遇到了：

- 游戏崩溃
- 装备机制异常
- Curios 槽位异常
- 状态效果未正常触发
- 多人游戏兼容问题
- 物品或方块异常

可以通过 GitHub Issues 提交问题。

提交 Bug 时建议附带：

- Minecraft 版本
- NeoForge 版本
- Curios 版本
- Eight Wastelands 版本
- 完整错误日志
- 问题复现步骤

---

## 📌 Project Status

Eight Wastelands 的主要内容开发已经完成。

当前阶段主要进行：

- Bug 修复
- 稳定性测试
- 平衡性调整
- 文档完善
- 发布准备

除必要修复外，项目暂时不会继续进行大规模功能扩展。

---

## ⚠️ Compatibility

Eight Wastelands 修改了部分：

- 玩家状态
- 战斗行为
- 生物掉落
- Curios 装备逻辑
- 生命恢复规则

因此，与修改相同机制的 Mod 同时使用时，可能产生兼容性问题。

如果发现冲突，欢迎提交 Issue。

---

## 📷 Screenshots

> Screenshots coming soon.

建议展示：

- 九重天罚
- Calamity 装备槽
- 九种灾劫状态
- 春剪
- 遗忘
- 楼砧
- 仙器 / 鬼器
- 实际战斗场景

---

## 📜 Licens

```text
MIT License
```


---

## 👤 Author

Developed by **<PX_8042>**

GitHub:

`<https://github.com/piaoxue8042-coder>`

QQ:

`<848489192>`

discord:

`<https://discord.gg/6hfbjqkme>`

---

## ❤️ About

Eight Wastelands 是我独立开发的 Minecraft Mod 项目。

这个项目不仅用于实现新的 Minecraft 内容，也用于实践 Java、NeoForge Mod 开发、自定义游戏机制设计、事件系统、装备系统以及 Git 版本管理。

项目从最初的灾劫装备系统逐步扩展到仙器、鬼器、特殊武器和独立修复机制，并最终形成了相对完整的游戏内容体系。

此项目大量借鉴了网络游戏"一念逍遥",此项目目前只跟新到了筑基阶段,往后不会再跟新,有任何疑问或者意愿可以联系我