import java.util.ArrayList;

public class Table {
    private final int column;
    protected ArrayList<Object> Rows[];
    protected int count;
    Table(int c){
        Rows = (ArrayList<Object>[]) new ArrayList<?>[100];
        column = c;
        count = 0;
    }
    public void add(ArrayList<Object> A) {
        Rows[count] = new ArrayList<Object>();
        Rows[count] = A;
        count ++;
    }
    public ArrayList<Object> get(int i) {
        return Rows[i];
    }
    public int find(String name) {
        for(int i = 0; i < count; i ++) {
            String s = (String)Rows[i].get(0);
            if(s.equals(name)) {
                return i;	//If you need virtual address change this to (Integer)Rows[i].get(1). Currently this gives address in the table
            }
        }
        return -1;
    }
    public int findAddress(String name){  //finding the address of this particular string from the table
        for(int i=0; i<count; i++){
            String s = (String)Rows[i].get(0);
            if(s.equals(name)){
                return (Integer)Rows[i].get(1);
            }
        }
        return -1;
    }
    public void printTable() {
        for(int i = 0; i < count; i ++) {
            System.out.println(Rows[i]);
        }
    }
}

class SymbolTable extends Table{

    SymbolTable() {
        super(5);
        // TODO Auto-generated constructor stub
        ArrayList<Object> A = new ArrayList<Object>();
        A.add("Symbol");
        A.add("Offset");
        A.add("Type");
        A.add("Value");
        A.add("Size");
        add(A);
    }

    public void GetLabels(){
        for(int i = 1; i < count; i ++) {
            System.out.println(Rows[i].get(0));
        }
    }
    public void ModifyDetails(int index, ArrayList<Object> A) {
        Rows[index] = A;
    }

}

class MacroTable extends Table{

    MacroTable() {
        super(3);
        // TODO Auto-generated constructor stub
        ArrayList<Object> A = new ArrayList<Object>();
        A.add("Name");
        A.add("Address");
        A.add("Size");
        add(A);
    }

}

class LiteralTable extends Table{
    LiteralTable(){
        super(3);
        ArrayList<Object> A = new ArrayList<Object>();
        A.add("Name");   // ******DO NOT NEED THIS**********
        A.add("Address");
        A.add("Value");
        A.add("Size");
        add(A);
    }
}

class OpCodeTable extends Table{
    OpCodeTable(){
        super(3);
        ArrayList<Object> A = new ArrayList<Object>();
        A.add("Name");
        A.add("Operand1");
        A.add("Operand2");    // ******DO NOT NEED THIS**********
        add(A);
    }
}