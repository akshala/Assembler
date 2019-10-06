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
//            System.out.println(split[0]);
            if ((!split[0].isEmpty() && split[0] != " " && split[0].charAt(0) != '/') || split[0].isEmpty()){    //This is not a comment in the code, taking comments as lines starting with /
//                    split[0] can be a space " " but this would not be a label but an opcode instruction
                try{
                    if(!split[0].isEmpty()){ // first character is not empty
                        if(split[0].substring(split[0].length() - 1).equals(":")){ // label is present
                            ArrayList<Object> labelType = new ArrayList<Object>();
                            labelType.add(split[0]);  // adding symbol
                            int offset = locationCounter;
                            labelType.add(offset); //  adding offset
                            labelType.add("Label");  // the type is label
                            labelType.add(" ");  // value is null
                            labelType.add(" ");  // size is null //******CHECK******
                            SymTable.add(labelType); // add to symbol table
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
                            }
                            catch(ArrayIndexOutOfBoundsException e){
                                // in case there is no operand
                            }
                            OpTable.add(opcodeType); // add to opcode table
                            locationCounter += 12;   //******CHECK******
                        }
                        else{ // label is not present
                            ArrayList<Object> opcodeType = new ArrayList<Object>();
                            opcodeType.add(split[0]); // adding opcode
                            try {
                                opcodeType.add(split[1]); // adding operand 1
                                if(split[1].charAt(0) == '\''){
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
        File Code = new File("/home/akshala/Documents/IIITD/thirdSem/CO/Project/Assembler-master/input.txt");
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