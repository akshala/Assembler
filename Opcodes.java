import java.util.HashMap;

public final class Opcodes {
    private final HashMap<String,String> Opcodes = new HashMap<String,String>();
    // hash map containing all the names of the operations and the opcodes
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
    public String getBitcode(String Opcode) {   // returns the binary opcode value
        return Opcodes.get(Opcode);
    }
    public boolean valid(String Opcode) {    // checking if the input opcode is present in the given opcode list or not
        if(Opcodes.containsKey(Opcode))
            return true;
        else
            return false;
    }
    public int getParameters(String string) {
        // TODO Auto-generated method stub
        if(string.equals("CLA") || string.equals("STP")) {
            return 0;
        }
        return 1;
    }
}