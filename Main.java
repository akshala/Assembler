import javax.crypto.Mac;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

class Assembler{
    private final Opcodes OpCode;
    private LabelTable LabTable;
    private SymbolTable SymTable;
    private MacroTable MacTable;
    private LiteralTable LitTable;
    private OpCodeTable OpTable;
    private int lineNo;

    Assembler(){
        OpCode = new Opcodes();
        LabTable = new LabelTable();
        MacTable = new MacroTable();
        LitTable = new LiteralTable();
        OpTable = new OpCodeTable();
        lineNo = 0;
    }

    void passOne(File Code, File intermediate) throws IOException{
        String last = "";
        HashMap<String,Integer> foundSymbols = new HashMap<String,Integer>();
        FileWriter fw = new FileWriter(intermediate);
        int locationCounter = 0;
        Scanner sn = new Scanner(Code);
        boolean STP = false;
        while(sn.hasNextLine() && STP == false) {
            String line = sn.nextLine();lineNo++;
            if(line.contains("STP"))
                STP = true;

            //Removing comments if any from the given line
            String[] RemoveComment = line.split("/");
            line = RemoveComment[0];

            if(line == "\n" || line == " " || line == null || line.isEmpty() || line.isEmpty()){ // if it is a blank line
                continue;
            }
            if(line.toUpperCase().contains("MACRO")){ // finding if macro definition is present
//                System.out.println("macro " + line);
                String[] split = line.split("\\s");  // splitting on spaces
                String MacroName = split[0].trim();  // name of the macro
                MacroName = MacroName.replaceAll("^\\s+", "");
                MacroName = MacroName.replaceAll(" ", "");
                if(MacroName.charAt(0) == ' '){
                    MacroName = MacroName.substring(1, MacroName.length());
                }
                if(OpCode.valid(MacroName)){  // if the opcode of this name is already present
                    ///////////////////////////////////////////             ERROR   HANDLED         //////////////////////////////////////
                    System.out.println("ERROR in line " + lineNo+" : OPCODE " + MacroName + " IS ALREADY PRESENT.");
                    line = sn.nextLine();lineNo++;
//                    System.out.println(line);
                    while(!line.toUpperCase().contains("MEND")) { // end of macro
                        line = sn.nextLine();lineNo++;
//                        System.out.println(line);
                    }
                    continue;
                }
                else if(MacTable.valid(MacroName)){  // if macro of this name has already been defined
                    ///////////////////////////////////////////             ERROR   HANDLED         //////////////////////////////////////
                    System.out.println("ERROR in line " + lineNo +" "+ MacroName + " HAS ALREADY BEEN DEFINED.");
                    line = sn.nextLine();lineNo++;
                    while(!line.toUpperCase().contains("MEND")) { // end of macro
                        line = sn.nextLine();lineNo++;
                    }
                    continue;
                }
                else{
                    int parametersNo = split.length - 2;
                    int size = 0;
                    int address = locationCounter;
                    ArrayList<String> parameters = new ArrayList<String>();
                    for(int i = 0; i < parametersNo; i ++) {
                        parameters.add(split[2+i]);
                    }
                    StringBuilder def = new StringBuilder();
                    line = sn.nextLine();lineNo++;
//                    System.out.println(line);
                    while(!line.toUpperCase().contains("MEND")) { // end of macro
                        def.append(line).append("\n");   // macro definition
                        try {
                            line = sn.nextLine();lineNo++;
                        }
                        catch(Exception e){
                            System.out.println("ERROR : MEND not found after Macro declaration!");
                            break;
                        }
//                        System.out.println(line);
                        size += 12;
                    }
                    ArrayList<Object> Macro = new ArrayList<Object>();
                    Macro.add(MacroName);  // adding macro name
                    Macro.add(address);   // adding macro address
                    Macro.add(size);   // adding macro size
                    Macro.add(def.toString());   // adding macro definition
                    Macro.add(parametersNo);
                    Macro.add(parameters);
                    MacTable.add(Macro);  // adding to macro table
                    continue;
                }
            }
            String[] split = line.split("\\s+");  // splitting the lines in the code by spaces

            if ((!split[0].isEmpty() && split[0] != " " && split[0].charAt(0) != '/') || split[0].isEmpty()){
                int pos;
                System.out.println(split[0]);
                if(split[0].equals("START")) {
                    try{
                        locationCounter = Integer.parseInt(split[1]); // if start location is given
                    }
                    catch(ArrayIndexOutOfBoundsException e){  // if no start location start at address 0
                        locationCounter = 0;
                    }
                    continue;
                }
                else{
                    String labelName="";
                    //This is not a comment in the code, taking comments as lines starting with /
                    System.out.println(split[0]);
                    if(split[0].substring(split[0].length() - 1).equals(":")){ // label is present
                        pos = 1;
                        ArrayList<Object> labelType = new ArrayList<Object>();
                        labelName = split[0].substring(0, split[0].length()-1);
                        if(!LabTable.valid(labelName)){
                            labelType.add(split[0].substring(0, split[0].length()-1));  // adding label name
                            int offset = locationCounter;
                            labelType.add(offset); //  adding offset
                            labelType.add("Label");  // the type is label
                            labelType.add(" ");  // value is null
                            labelType.add(" ");  // size is null //******CHECK******
                            LabTable.add(labelType); // add to symbol table
                        }
                        else{  // if label is already defined
                            ///////////////////////////////////////////             ERROR   HANDLED         //////////////////////////////////////
                            System.out.println("ERROR in line " + lineNo + " : " + labelName +" is already defined");
                        }
                    }

                    else{ // label is not present
                        pos = 0;
                    }
                    int parameters = split.length-pos-1;
                    ArrayList<Object> opcodeType = new ArrayList<Object>();
                    if(!OpCode.valid(split[pos])) {  // if opcode is not present in the valid opcodes
                        if(!MacTable.valid(split[pos])) {   // if the opcode is not a macrotable either
                            ///////////////////////////////////////////             ERROR   HANDLED         //////////////////////////////////////
                            System.out.println("ERROR in line " + lineNo + " : " + split[pos] +" IS NEITHER AN OPCODE NOR A MACRO!");
                        }
                        else {
                            // Class Cast Exception
                            int MacroParameters = MacTable.getParameters(split[pos]);
                            if(parameters==MacroParameters) {
                                locationCounter += MacTable.getSize(split[pos]);
                                ArrayList<String> operands = new ArrayList<String>();
                                for(int i = pos+1; i<=MacroParameters; i++) {
                                    operands.add(split[i]);
                                }
                                String MacroExpansion = labelName+MacTable.expand(split[pos],operands);
                                fw.write(labelName+MacroExpansion);
                            }
                            else {
                                System.out.println("ERROR in line " + lineNo + " : " + split[pos] +" takes "+MacroParameters + " Parameters");
                            }
                        }
                    }
                    else {
                        opcodeType.add(split[pos]); // adding opcode
                        int OpCodeParameters = OpCode.getParameters(split[pos]);
                        if(parameters != OpCodeParameters) {
                            System.out.println("ERROR in line " + lineNo + " : " + split[pos] +" takes "+OpCodeParameters + " Parameters");
                        }
                        try {
                            opcodeType.add(split[pos + 1]); // adding operand 1
                            try{
                                int operand1 = Integer.valueOf(split[pos + 1]);
                            }
                            catch(NumberFormatException e){
                                String operand1 = split[pos + 1];
                                foundSymbols.put(operand1, lineNo);
                            }
                            if(split[pos + 1].charAt(0) == '\'' && split[pos + 1].charAt(1) == '='){
                                // this is a literal. Literal is of the form '=[value]'
                                ArrayList<Object> literalType = new ArrayList<Object>();
                                literalType.add(""); // name
                                literalType.add(0); // address
                                String val_str = split[pos + 1].substring(2,split[pos+1].length());
                                int value = Integer.parseInt(val_str);
                                literalType.add(value); // value
                                literalType.add(4);  // size  //******CHECK******
                                LitTable.add(literalType);
                            }
                            else {
                                // if it is a variable
                                if (SymTable.find(split[pos + 1]) == -1) {   // if variable was not already in the table
                                    ArrayList<Object> variableType = new ArrayList<Object>();
                                    variableType.add(split[pos + 1]); // adding symbol
                                    variableType.add(0); // take address which was already stored Offset    //******CHECK******
                                    variableType.add("Variable"); // type is variable
                                    variableType.add(0); // value
                                    int size = 4; // size ******CHECK******
                                    variableType.add(size);
                                    SymTable.add(variableType);
                                }
                            }
                        }
                        catch(ArrayIndexOutOfBoundsException e){
                            // in case there is no operand
                        }
                        OpTable.add(opcodeType); // add to opcode table
                        fw.write(line+"\n");
                        locationCounter += 12;   //******CHECK******
                    }
                }
            }
            last = line;
        }
        for(int i=0; i<LitTable.count; i++){
            LitTable.modifyAddress(i, locationCounter);
            locationCounter++;
        }
        for(int i=0; i<SymTable.count; i++){
            SymTable.modifyAddress(i, locationCounter);
            locationCounter++;
        }
        for(Map.Entry<String, Integer> entry : foundSymbols.entrySet()){
            String key = entry.getKey();
            Integer value = entry.getValue();
            if(!LabTable.valid(key)){
                ///////////////////////////////////////////             ERROR   HANDLED         //////////////////////////////////////
                System.out.println("ERROR in line " + value + " : " + "label " + key + " is not defined");
            }
        }
        if(!last.contains("STP")){
            ///////////////////////////////////////////             ERROR   HANDLED         //////////////////////////////////////
            System.out.println("ERROR in line " + lineNo + " : " + "STP statement is not present!");
        }
        fw.close();
//        System.out.print("Symbol Table");
//        LabTable.printTable();
//        System.out.print("Literal Table");
//        LitTable.printTable();
//        System.out.print("Opcode Table");
//        OpTable.printTable();
//        System.out.println("Macro Table");
//        MacTable.printTable();
        sn.close();
    }
    void passTwo(File Code, File Output) throws IOException{
        lineNo = 0;
        FileWriter fw = new FileWriter(Output);
        int locationCounter = 0;
        String operand;
        String OPCode;
        Scanner sn = new Scanner(Code);
        while(sn.hasNextLine()) {
            String address, opcodeBinary, operandBinary;
            String line = sn.nextLine();
            String[] split = line.split("\\s+");  // splitting the lines in the code by spaces

            //Getting Address
            address = Integer.toBinaryString(locationCounter);
            while(address.length()<10) {
                address = "0"+address;
            }
            int pos=0;
            //Getting Opcode Position label is present
            if(split[0].substring(split[0].length() - 1).equals(":")){ // label is present
                pos = 1;
            }
            //Converting Opcode to binary
            OPCode = split[pos];
            opcodeBinary = OpCode.getBitcode(OPCode);

//            fw.write(locationCounter+" "+ OPCode +"\n");
            fw.write(address+" "+opcodeBinary+" ");

            //Getting operand and their binaries
            for(int i = pos+1; i < split.length; i++) {
                operand = split[i];
                int val = 0;
                try {
                    val = Integer.parseInt(operand);
                    if(val >= 512) {
                        System.out.println("Error : size of operand cannot be stored it 8 bits. Must be <= 511");
                    }
                }
                catch(Exception e) {
                    val = LabTable.findAddress(operand);
                    if(val == -1){
                        val = LitTable.find(operand);
                    }
                    if(val == -1){
                        val = SymTable.find(operand);
                    }
                }
                finally {
                    operandBinary = Integer.toBinaryString(val);
                    while(operandBinary.length() < 8) {
                        String inter = "0"+operandBinary;
                        operandBinary = inter;
                    }
                    fw.write(operandBinary+" ");
                }
            }
            fw.write("\n");
            locationCounter += 12;   //******CHECK******
        }

        fw.close();
        sn.close();
    }
}

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        Assembler A = new Assembler();
        File Code = new File("/home/akshala/Downloads/CO_final/src/input.txt");
        File Intermediate = new File("/home/akshala/Downloads/CO_final/src/Intermediate.txt");
        File Output = new File("/home/akshala/Downloads/CO_final/src/output.txt");
        try {
//            A.Assemble(Code);
            A.passOne(Code, Intermediate);
            A.passTwo(Intermediate, Output);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Wrong File: Cannot Assemble");
            e.printStackTrace();
        }
    }
}



