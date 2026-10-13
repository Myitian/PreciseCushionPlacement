# Precise Cushion Placement

[中文](#中文) | [English](#English)

---

<span id="中文"></span>

在精准的Y坐标位置放置坐垫。

默认会依次尝试从手持坐垫物品自定义名称、副手物品自定义名称、命令设置的值获取坐垫的方块内Y坐标偏移量，如果发现有效值（0.0~1.0），则会在放置坐垫时计算新的Y值（ $y_{\mathrm{new}}=\left\lfloor y_0\right\rfloor+y_{\mathrm{offset}}$ ）并使用新Y值和`Direction.UP`合成一个新的`BlockHitResult`，从而实现精准的Y坐标放置。

> [!WARNING]
>
> 由于这种方式修改了`BlockHitResult`使其偏离了玩家视线所指位置，在服务器中可能会触发反作弊机制。使用前请咨询管理员是否允许此类辅助模组。

Y偏移量的来源顺序可以通过配置文件或命令进行修改。

命令列表（子命令均以`/precise_cushion_placement`为前缀）：

| 子命令                       | 参数             | 说明        |
|---------------------------|----------------|-----------|
| `offset clear`            | 无              | 清除Y偏移量    |
| `offset get`              | 无              | 获取Y偏移量    |
| `offset set`              | 双精度浮点数，0.0~1.0 | 设置Y偏移量    |
| `config reload`           | 无              | 重新加载配置    |
| `config reset`            | 无              | 重置配置      |
| `config get`              | 无              | 显示当前配置    |
| `config set valueSources` | 枚举值集合          | 设置Y偏移量的来源 |

---

<span id="English"></span>

Place cushions with precise Y position.

By default, it will try to get the Y coordinate offset of the cushion from the held cushion item custom name, offhand item custom name, or the value set by command in order. If a valid value (0.0~1.0) is found, a new Y value will be calculated when placing the cushion ($y_{\mathrm{new}}=\left\lfloor y_0\right\rfloor+y_{\mathrm{offset}}$) and a new `BlockHitResult` will be synthesized using the new Y value and `Direction.UP`, thus achieving precise Y coordinate placement.

> [!WARNING]
>
> Since this method modifies the `BlockHitResult` to deviate from the position pointed by the player's line of sight, it may trigger anti-cheat mechanisms on the server. Please consult the administrator before using such auxiliary mods.

Y offset sources can be modified through configuration file or command.

Command list (subcommands are prefixed with `/precise_cushion_placement`):

| Subcommand                | Parameters      | Description                |
|---------------------------|-----------------|----------------------------|
| `offset clear`            | None            | Clear Y offset             |
| `offset get`              | None            | Get Y offset               |
| `offset set`              | Double, 0.0~1.0 | Set Y offset               |
| `config reload`           | None            | Reload configuration       |
| `config reset`            | None            | Reset configuration        |
| `config get`              | None            | Show current configuration |
| `config set valueSources` | Enum set        | Set Y offset sources       |