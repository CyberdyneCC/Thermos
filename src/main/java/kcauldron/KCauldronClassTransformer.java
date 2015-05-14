package kcauldron;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import net.minecraft.launchwrapper.IClassTransformer;
import static org.objectweb.asm.Opcodes.*;

public class KCauldronClassTransformer implements IClassTransformer {
	public static boolean DEV;

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if ("net.minecraft.world.gen.ChunkProviderServer".equals(transformedName) || "net.minecraftforge.common.chunkio.ChunkIOProvider".equals(transformedName)) {
			return patchChunkCall(basicClass);
		}
		return basicClass;
	}
	
	private byte[] patchChunkCall(byte[] bytes) {
		ClassReader reader = new ClassReader(bytes);
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		reader.accept(new ClassVisitor(ASM5, writer) {
			@Override
			public FieldVisitor visitField(int access, String name,
					String desc, String signature, Object value) {
				if ("loadedChunkHashMap_vanilla".equals(name)) {
					name = DEV ? "loadedChunkHashMap" : "field_73244_f";
				}
				return super.visitField(access, name, desc, signature, value);
			}
			
			@Override
			public MethodVisitor visitMethod(int access, String name,
					String desc, String signature, String[] exceptions) {
				return new MethodVisitor(api, super.visitMethod(access, name, desc, signature, exceptions)) {
					@Override
					public void visitFieldInsn(int opcode, String owner,
							String name, String desc) {
						if ("loadedChunkHashMap_vanilla".equals(name)) {
							name = DEV ? "loadedChunkHashMap" : "field_73244_f";
						}
						super.visitFieldInsn(opcode, owner, name, desc);
					}
				};
			}
		}, 0);
		return writer.toByteArray();
	}
}
