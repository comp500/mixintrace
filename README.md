# MixinTrace
This is a Fabric mod that adds a list of mixins (and Mixin configuration names) to classes in the stack trace to crash reports. 

Example output:

```
	Mixins in Stacktrace: 
		net.minecraft.class_465:
			dev.emi.bunchotrinkets.mixin.AbstractContainerScreenMixin (bunchotrinkets.mixins.json)
			net.backslot.mixin.HandledScreenMixin (backslot.mixins.json)
			dev.emi.trinkets.mixin.HandledScreenMixin (trinkets.mixins.json)
		net.minecraft.class_485:
			dev.emi.trinkets.mixin.AbstractInventoryScreenMixin (trinkets.mixins.json)
		net.minecraft.class_481:
			dev.emi.trinkets.mixin.CreativeInventoryScreenMixin (trinkets.mixins.json)
			net.fabricmc.fabric.mixin.item.group.client.MixinCreativePlayerInventoryGui (fabric-item-groups-v0.mixins.json)
		net.minecraft.class_757:
			com.jamieswhiteshirt.reachentityattributes.mixin.client.GameRendererMixin (mixins.reach-entity-attributes.json)
		net.minecraft.class_310:
			net.fabricmc.fabric.mixin.networking.MixinMinecraftClient (fabric-networking-v0.mixins.json)
			net.fabricmc.fabric.mixin.resource.loader.MixinMinecraftGame (fabric-resource-loader-v0.mixins.json)
			net.fabricmc.fabric.mixin.event.interaction.MixinMinecraftClient (fabric-events-interaction-v0.mixins.json)
			net.fabricmc.fabric.mixin.event.lifecycle.client.MinecraftClientMixin (fabric-lifecycle-events-v1.mixins.json)
			net.fabricmc.fabric.mixin.registry.sync.client.MixinMinecraftClient (fabric-registry-sync-v0.mixins.json)
			dev.emi.bunchotrinkets.mixin.MinecraftClientMixin (bunchotrinkets.mixins.json)
```

Tested on Minecraft 1.16.2 and 1.16.1, but is likely to work on many different versions.
