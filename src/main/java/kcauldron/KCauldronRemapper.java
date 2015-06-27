package kcauldron;

import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;

public class KCauldronRemapper extends JarRemapper {
    public KCauldronRemapper(JarMapping jarMapping) {
        super(jarMapping);
    }

    @Override
    public String mapSignature(String signature, boolean typeSignature) {
        try {
            return super.mapSignature(signature, typeSignature);
        } catch (Exception e) {
            return signature;
        }
    }
}
