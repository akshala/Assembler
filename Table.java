import java.util.ArrayList;
import java.util.HashMap;

public class Table {
    private final int column;
    ArrayList<Object> Rows[];
    int count;
    Table(int c){
        Rows = (ArrayList<Object>[]) new ArrayList<?>[100];
        column = c;
        count = 0;  // count of the number of objects in the table
    }
    public void add(ArrayList<Object> A) {
        Rows[count] = new ArrayList<Object>();
        Rows[count] = A;
        count ++;
    }
    public ArrayList<Object> get(int i) {
        return Rows[i];
    }
    public int find(String name) {  // get index of the element to be found in table
        for(int i = 0; i < count; i ++) {
            String s = (String)Rows[i].get(0);
            if(s.equals(name)) {
                return i;
            }
        }
        return -1;
    }
    public int findAddress(String name){   // get address of the element to be found in the table
        for(int i=0; i<count; i++){
            String s = (String)Rows[i].get(0);
            if(s.equals(name)){
                return (Integer)Rows[i].get(1);
            }
        }
        return -1;
    }
    public void printTable() {  // prints the entire table
        for(int i = 0; i < count; i ++) {
            System.out.println(Rows[i]);
        }
    }
}

class SymbolTable extends Table{

    SymbolTable() {  // mainly for labels
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

    public boolean valid(String string) {   // if a particular label is present in the macro table or not
        // TODO Auto-generated method stub
        boolean found = false;
        for(ArrayList<Object> A : Rows) {
            if(A != null) {
                if(A.get(0).equals(string)) {
                    return true;
                }
            }
            else
                return false;
        }
        return false;
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
        A.add("Definition");
        A.add("ParametersNo");
        A.add("Operands");
        add(A);
    }

    public boolean valid(String string) {   // if a particular macro is present in the macro table or not
        // TODO Auto-generated method stub
        boolean found = false;
        for(ArrayList<Object> A : Rows) {
            if(A != null) {
                String check = (String)A.get(0);
                check = check.trim();
                if(check.equals(string)) {
                    return true;
                }
            }
            else
                return false;
        }
        return false;
    }

    public int getSize(String string) {   // get the size of the macro
        for(ArrayList<Object> A : Rows) {
            if(A.get(0).equals(string)) {
                return (int) A.get(2);
            }
        }
        return -1;
    }

    public int getParameters(String string) {
        // TODO Auto-generated method stub
        for(ArrayList<Object> A : Rows) {
            if(A.get(0).equals(string)) {
                return (int) A.get(4);
            }
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public String expand(String string, ArrayList<String> operands) {
        // TODO Auto-generated method stub
        HashMap<String,String> Parameters = new HashMap<String,String>();
        ArrayList<String> Actual = new ArrayList<String>();
        int num = 0;
        String definition = "";
        System.out.println(Rows);
        for(ArrayList<Object> A : Rows) {
            if(A == null)
                break;
            else {
                System.out.println(A);
                if(A.get(0).equals(string)) {
                    Actual = (ArrayList<String>) A.get(5);
                    num = (int) A.get(4);
                    definition = (String) A.get(3);
                }
            }
        }
        for(int i = 0; i < num; i ++) {
            Parameters.put(Actual.get(i), operands.get(i));
        }
        String[] lines = definition.split("\\s+");
        String output="";
        for(int i = 0; i < lines.length; i ++) {
            if(Parameters.containsKey(lines[i])) {
                lines[i]=Parameters.get(lines[i]);
            }
            output+=lines[i];
        }
        return output;
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