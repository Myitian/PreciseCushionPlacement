# Precise Cushion Placement

[中文](#中文) | [English](#English)

---

<span id="中文"></span>

在精准的Y坐标位置放置坐垫。

默认会依次尝试从手持坐垫物品名称、副手物品名称、命令设置的值获取坐垫的方块内Y坐标偏移量，如果发现有效值（0.0~1.0），则会在放置坐垫时计算新的Y值（ $y_{\mathrm{new}}=\left\lfloor y_0\right\rfloor+y_{\mathrm{offset}}$ ）并使用新y值和`Direction.UP`合成一个新的`BlockHitResult`，从而实现精准的Y坐标放置。

> [!WARNING]
> 
> 由于这种方式修改了`BlockHitResult`使其偏离了玩家视线所指位置，在服务器中可能会触发反作弊机制。使用前请咨询管理员是否允许此类辅助模组。

---

<span id="English"></span>

Place cushions with precise Y position.

By default, it will try to get the Y coordinate offset of the cushion from the held cushion item name, offhand item name, or the value set by command in order. If a valid value (0.0~1.0) is found, a new Y value will be calculated when placing the cushion ($y_{\mathrm{new}}=\left\lfloor y_0\right\rfloor+y_{\mathrm{offset}}$) and a new `BlockHitResult` will be synthesized using the new Y value and `Direction.UP`, thus achieving precise Y coordinate placement.

> [!WARNING]
>
> Since this method modifies the `BlockHitResult` to deviate from the position pointed by the player's line of sight, it may trigger anti-cheat mechanisms on the server. Please consult the administrator before using such auxiliary mods.