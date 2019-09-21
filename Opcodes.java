import java.util.HashMap;

public final class Opcodes {
	private final HashMap<String,String> Opcodes = new HashMap<String,String>();
	Opcodes(){
		Opcodes.put("CLA","0000");
		Opcodes.put("LAC","0001");
		Opcodes.put("SAC","0010");
		Opcodes.put("ADD","0011");
		Opcodes.put("SUB","0100");
		Opcodes.put("BRZ","0101");
		Opcodes.put("BRN","0110");
		Opcodes.put("BRP","0111");
		Opcodes.put("INP","1000");
		Opcodes.put("DSP","1001");
		Opcodes.put("MUL","1010");
		Opcodes.put("DIV","1011");
		Opcodes.put("STP","1100");
	}
	public String getBitcode(String Opcode) {
		return Opcodes.get(Opcode);
	}
}
