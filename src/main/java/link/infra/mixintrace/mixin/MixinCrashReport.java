package link.infra.mixintrace.mixin;

import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Take that, NEC!
@Mixin(value = CrashReport.class, priority = 499)
public class MixinCrashReport {
	@Shadow @Final private Throwable cause;
	@Shadow @Final private CrashReportSection systemDetailsSection;

	private StackTraceElement[] mixintrace_unmappedStackTrace;

	@Inject(method = "fillSystemDetails", at = @At("HEAD"))
	private void beforeFillSystemDetails(CallbackInfo ci) {
		mixintrace_unmappedStackTrace = this.cause.getStackTrace();
	}

	@SuppressWarnings("unchecked")
	@Inject(method = "fillSystemDetails", at = @At("TAIL"))
	private void afterFillSystemDetails(CallbackInfo ci) {
		systemDetailsSection.add("Mixins in Stacktrace", () -> {
			StringBuilder builder = new StringBuilder();

			List<String> classNames = new ArrayList<>();
			for (StackTraceElement el : mixintrace_unmappedStackTrace) {
				if (!classNames.contains(el.getClassName())) {
					classNames.add(el.getClassName());
				}
			}

			for (String className : classNames) {
				ClassInfo classInfo = ClassInfo.fromCache(className);
				if (classInfo != null) {
					// Workaround for bug in Mixin, where it adds to the wrong thing :(
					Method getMixins = ClassInfo.class.getDeclaredMethod("getMixins");
					getMixins.setAccessible(true);
					Set<IMixinInfo> mixinInfoSet = (Set<IMixinInfo>) getMixins.invoke(classInfo);
					if (mixinInfoSet.size() > 0) {
						builder.append("\n\t\t");
						builder.append(className);
						builder.append(":");
						for (IMixinInfo info : mixinInfoSet) {
							builder.append("\n\t\t\t");
							builder.append(info.getClassName());
							builder.append(" (");
							builder.append(info.getConfig().getName());
							builder.append(")");
						}
					}
				}
			}

			if (builder.length() == 0) {
				return "None found";
			}
			return builder.toString();
		});
	}
}
