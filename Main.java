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
	
	public void Assemble(File Code) throws FileNotFoundException{
		//Use any file iterator as per your convenience. Using scanner just to the code
		Scanner sn = new Scanner(Code);
		while(sn.hasNextLine()) {
			String line = sn.nextLine();
			System.out.println(line);
		}
		
	}
}
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Assembler A = new Assembler();
		File Code = new File("C:\\Users\\Harsh Kumar Sethi\\Desktop\\AssemblyCode.txt");
		try {
			A.Assemble(Code);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Wrong File: Cannot Assemble");
			e.printStackTrace();
		}
	}
}
