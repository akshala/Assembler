import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

class Assembler{
    private final Opcodes OpCode;
    private SymbolTable SymTable;
    private MacroTable MacTable;
    private LiteralTable LitTable;
    private OpCodeTable OpTable;

    Assembler(){
        OpCode = new Opcodes();
        SymTable = new SymbolTable();
        MacTable = new MacroTable();
        LitTable = new LiteralTable();
        OpTable = new OpCodeTable();
    }

    void Assemble(File Code) throws FileNotFoundException{
        //Use any file iterator as per your convenience. Using scanner just to the code
        Scanner sn = new Scanner(Code);
        while(sn.hasNextLine()) {
            String line = sn.nextLine();
            System.out.println(line);
        }
    }

    void passOne(File Code) throws FileNotFoundException{
        int locationCounter = 0;
        Scanner sn = new Scanner(Code);
        while(sn.hasNextLine()) {
            String line = sn.nextLine();
            if(line == "\n" || line == " " || line == null || line.isEmpty() || line.isEmpty()){ // if it is a blank lne
                continue;
            }
            String[] split = line.split("\\s+");  // splitting the lines in the code by spaces
//            for(String element: split){
//                System.out.print(element + " ");
//            }
//            System.out.println();
            if ((!split[0].isEmpty() && split[0] != " " && split[0].charAt(0) != '/') || split[0].isEmpty()){    //This is not a comment in the code, taking comments as lines starting with /
//                    split[0] can be a space " " but this would not be a label but an opcode instruction
//                System.out.println(line);
                if(split[0].equals("START")) {
                    try{
                        locationCounter = Integer.parseInt(split[1]); // if start location is given
                    }
                    catch(ArrayIndexOutOfBoundsException e){  // if no start location start at address 0
                        locationCounter = 0;
                    }
                    continue;
                }
                try{
                    if(!split[1].isEmpty()){
                        if(split[1].equals("DW")){ // this is a variable declaration
                            if(SymTable.find(split[0]) != -1){ // if symbol is already present in the table, update it with value
                                ArrayList<Object> variableType = new ArrayList<Object>();
                                variableType.add(split[0]); // adding symbol
                                variableType.add(SymTable.findAddress(split[2])); // take address which was already stored Offset
                                variableType.add("Variable"); // type is variable
                                variableType.add(split[2]); // value
                                int size = 4; // size ******CHECK******
                                variableType.add(size);
                                SymTable.ModifyDetails(SymTable.find(split[2]), variableType);
                            }
                            else{ // if variable was not already in the table
                                ArrayList<Object> variableType = new ArrayList<Object>();
                                variableType.add(split[0]); // adding symbol
                                variableType.add(locationCounter); // take address which was already stored Offset    //******CHECK******
                                variableType.add("Variable"); // type is variable
                                variableType.add(split[2]); // value
                                int size = 4; // size ******CHECK******
                                variableType.add(size);
                                SymTable.add(variableType);
                            }
                            locationCounter += 12;   //******CHECK******
                        }
                        else{ // this is an instruction with an opcode
                            if(!split[0].isEmpty()){ // this would be a label
                                ArrayList<Object> labelType = new ArrayList<Object>();
                                labelType.add(split[0]);  // adding symbol
                                int offset = locationCounter;
                                labelType.add(offset); //  adding offset
                                labelType.add("Label");  // the type is label
                                labelType.add(" ");  // value is null
                                labelType.add(" ");  // size is null //******CHECK******
                                SymTable.add(labelType); // add to symbol table
                            }
                            ArrayList<Object> opcodeType = new ArrayList<Object>();
                            opcodeType.add(split[1]); // adding opcode
                            try {
                                opcodeType.add(split[2]); // adding operand 1
                                if(split[2].charAt(0) == '\''){
                                    // this is a literal. Literal is of the form '=[value]'
                                    ArrayList<Object> literalType = new ArrayList<Object>();
                                    literalType.add(""); // name
                                    literalType.add(locationCounter); // address
                                    String val_str = split[2].replace("\'", "");
                                    val_str = split[2].replace("=", "");
                                    int value = Integer.parseInt(val_str);
                                    literalType.add(value); // value
                                    literalType.add(4);  // size  //******CHECK******
                                }
                                else {
                                    // if it is a variable
                                    if (SymTable.find(split[2]) == -1) {   // if variable was not already in the table
                                        ArrayList<Object> variableType = new ArrayList<Object>();
                                        variableType.add(split[0]); // adding symbol
                                        variableType.add(locationCounter); // take address which was already stored Offset    //******CHECK******
                                        variableType.add("Variable"); // type is variable
                                        variableType.add(split[2]); // value
                                        int size = 4; // size ******CHECK******
                                        variableType.add(size);
                                    }
                                }
                            }
                            catch(StringIndexOutOfBoundsException e){
                                // in case there is no operand
                            }
                            OpTable.add(opcodeType); // add to opcode table
                            locationCounter += 12;   //******CHECK******
                        }
                    }
                }
                catch(IndexOutOfBoundsException e){

                }
            }
        }
        System.out.print("Symbol Table");
        SymTable.printTable();
        System.out.print("Literal Table");
        LitTable.printTable();
        System.out.print("Opcode Table");
        OpTable.printTable();
    }
}
public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Assembler A = new Assembler();
        File Code = new File("/home/akshala/Documents/IIITD/thirdSem/CO/Project/Assembler-master/AssemblyCode.txt");
        try {
//            A.Assemble(Code);
            A.passOne(Code);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Wrong File: Cannot Assemble");
            e.printStackTrace();
        }
    }
}
