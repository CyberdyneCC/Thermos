package kcauldron;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;

import pw.prok.imagine.api.Pair;
import pw.prok.imagine.asm.ImagineASM;
import pw.prok.imagine.asm.ImagineMethod;
import pw.prok.imagine.asm.ImagineRemapper;
import pw.prok.imagine.asm.Transformer;

import com.avaje.ebean.annotation.Transactional;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import static org.objectweb.asm.Opcodes.*;

@Transformer.RegisterTransformer
public class KCauldronClassTransformer implements Transformer {
	@Override
	public void transform(final ImagineASM asm) {
		if (asm.is("net.minecraft.world.gen.ChunkProviderServer")
				|| asm.is("net.minecraftforge.common.chunkio.ChunkIOProvider")
				|| asm.is("org.bukkit.craftbukkit.CraftWorld")
				|| asm.is("org.bukkit.craftbukkit.v1_7_R4.CraftWorld")) {
			FMLLog.log(Level.INFO, "KCauldron: Patching " + asm.getActualName() + " for compatibility with Mystcraft");
			ClassReader reader = new ClassReader(asm.build());
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			reader.accept(new ClassVisitor(ASM5, writer) {
				@Override
				public FieldVisitor visitField(int access, String name,
						String desc, String signature, Object value) {
					if ("loadedChunkHashMap_vanilla".equals(name)) {
						name = asm.isDev() ? "loadedChunkHashMap" : "field_73244_f";
					}
					return super.visitField(access, name, desc, signature,
							value);
				}

				@Override
				public MethodVisitor visitMethod(int access, String name,
						String desc, String signature, String[] exceptions) {
					return new MethodVisitor(api, super.visitMethod(access,
							name, desc, signature, exceptions)) {
						@Override
						public void visitFieldInsn(int opcode, String owner,
								String name, String desc) {
							if ("loadedChunkHashMap_vanilla".equals(name)) {
								name = asm.isDev() ? "loadedChunkHashMap" : "field_73244_f";
							}
							super.visitFieldInsn(opcode, owner, name, desc);
						}
					};
				}
			}, 0);
			asm.loadClass(writer.toByteArray());
		}
		if (asm.is("climateControl.utils.ChunkGeneratorExtractor")) {
			boolean undergroundBiomesInstalled = false;
			try {
				Class.forName("exterminatorJeff.undergroundBiomes.worldGen.ChunkProviderWrapper");
				undergroundBiomesInstalled = true;
			} catch (Exception ignored) {
			}
			if (!undergroundBiomesInstalled) {
				FMLLog.log(Level.INFO, "KCauldron: Patching " + asm.getActualName() + " for compatibility with Climate Control");
				extractFrom(asm, asm.method("extractFrom",
						"(Lnet/minecraft/world/WorldServer;)Lnet/minecraft/world/chunk/IChunkProvider;").instructions());
			}
		}
	}

	public void extractFrom(ImagineASM asm, InsnList list) {
		//Pair<String, String> fieldChunkProvider = asm.field("net/minecraft/world/World", "chunkProvider");
		list.clear();
		list.add(new IntInsnNode(ALOAD, 1));
		list.add(new FieldInsnNode(GETFIELD, "ahb", "v", "Lapu;"));
		list.add(new InsnNode(ARETURN));
	}
}
