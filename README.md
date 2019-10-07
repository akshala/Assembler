# Assembler
Accumulator Architecture Based Assembler

We have implemented a two pass assembler. Java programming language has been used for writing the code of the assembler. The input would be a txt file which contains the assembly code. The output is a txt file which contains the object code(machine code).

 ### Basic Tables used
 #### 1. Symbol Table
 This is for handling labels. It stores the symbol, offset, type, value, size of the symbol. 
 #### 2. Macro Table
 This is for handling macros. It stores the name, address, size and definition of the macro. 
 #### 3. Literal Table
 This is for handling literals. It stores the name, address, value and size of the literal.
 #### 4. Opcode Table
 This is for handling literals. It stores the name and operands associated with the operation.

 ### First Pass
 In the first pass, we iterate over the assembly code once and fill the tables that were explained above. The address of the first instruction is zero. The length of the instruction is added to the location counter. The address of the next instruction is the value that is stored in the location counter. And this process continues till addresses are assigned to all the instructions. 

### Second Pass
