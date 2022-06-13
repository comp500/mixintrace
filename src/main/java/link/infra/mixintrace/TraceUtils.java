package link.infra.mixintrace;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TraceUtils {
	public static void printTrace(StackTraceElement[] stackTrace, StringBuilder crashReportBuilder) {
		if (stackTrace != null && stackTrace.length > 0) {
			crashReportBuilder.append("\nMixins in Stacktrace:");

			try {
				List<String> classNames = new ArrayList<>();
				for (StackTraceElement el : stackTrace) {
					if (!classNames.contains(el.getClassName())) {
						classNames.add(el.getClassName());
					}
				}

				boolean found = false;
				for (String className : classNames) {
					ClassInfo classInfo = ClassInfo.fromCache(className);
					if (classInfo != null) {
						// Workaround for bug in Mixin, where it adds to the wrong thing :(
						Object mixinInfoSetObject;
						try {
							Method getMixins = ClassInfo.class.getDeclaredMethod("getMixins");
							getMixins.setAccessible(true);
							mixinInfoSetObject = getMixins.invoke(classInfo);
						} catch (Exception e) {
							// Fabric loader >=0.12.0 proguards out this method; use the field instead
							var mixinsField = ClassInfo.class.getDeclaredField("mixins");
							mixinsField.setAccessible(true);
							mixinInfoSetObject = mixinsField.get(classInfo);
						}
						
						@SuppressWarnings("unchecked") Set<IMixinInfo> mixinInfoSet = (Set<IMixinInfo>) mixinInfoSetObject;
						
						if (mixinInfoSet.size() > 0) {
							crashReportBuilder.append("\n\t");
							crashReportBuilder.append(className);
							crashReportBuilder.append(":");
							for (IMixinInfo info : mixinInfoSet) {
								crashReportBuilder.append("\n\t\t");
								crashReportBuilder.append(info.getClassName());
								crashReportBuilder.append(" (");
								crashReportBuilder.append(info.getConfig().getName());
								crashReportBuilder.append(")");
							}
							found = true;
						}
					}
				}

				if (!found) {
					crashReportBuilder.append(" None found");
				}
			} catch (Exception e) {
				crashReportBuilder.append(" Failed to find Mixin metadata: ").append(e);
			}
		}
	}
}
