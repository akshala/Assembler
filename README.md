# Assembler
Accumulator Architecture Based Assembler

We have implemented a two pass assembler for a 12 bit accumulator architechture. Java programming language has been used for writing the code of the assembler. The input would be a txt file which contains the assembly code. The output is a txt file which contains the object code(machine code).

 ### Basic Tables used
 #### 1. Label Table
 This is for handling labels. It stores the label, offset, type, value, size of the symbol. 
 #### 1. Symbol Table
 This is for handling variables. It stores the variable, offset, type, value, size of the symbol. 
 #### 2. Macro Table
 This is for handling macros. It stores the name, address, size and definition of the macro. 
 #### 3. Literal Table
 This is for handling literals. It stores the name, address, value and size of the literal.
 #### 4. Opcode Table
 This is for handling literals. It stores the name and operands associated with the operation.

 ### First Pass
In the first pass, we iterate over the assembly code once and fill the tables that were explained above. The address of the first instruction is zero. The length of the instruction is added to the location counter. The address of the next instruction is the value that is stored in the location counter. And this process continues till addresses are assigned to all the instructions. 
* A line starting with '//' is treated as a comment and no further processing is done for this line. 
* A macro definition would be a line containing the name of the macro followed by the keyword MACRO and the parameters if they are present. The end of the macro is indicated by the keyword MEND. The name, address, size and definition of the macro are stored in the macro table. 
* If the macro has already been defined then an error message is shown. If the name of the macro defined is the same as one of the given opcode names then also an error message would be shown.
* If the assembly code starts with the word START followed by a number then the location counter is set to that number. If no such statement is present then the location counter is started from 0. 
* A label would end with the symbol ':'. After the label an instruction would follow. When a label is encountered it would be stored in the label table along with its address.
* If a label is defined more than once then an error message is shown.
* For an instruction, we identify if the opcode is either present in the macro table or it is one of the given opcodes. If it is neither then an error message is shown. 
* If it is a macro that has already been defined then the location couter is incremented by the size of the macro and nothing else is done. 
* For a macro call, the definition of the macro is taken from the macro table and a macro expansion is done.
* If the opcode is one of the given opcodes, then it is added to the opcode table along with the operands if they are present.
* Forward referencing is done in the first pass. If there is a symbol which has not been found even at the end of the first pass then an error message is shown. 
* If the operand is a variable then it is added to the symbol table. The address of the variable is assigned at the end of the program. It is assigned a one word address at the end.
* If the operand is a literal then it is added to the literal table. The address of the literal is assigned at the end of the program. It is assigned a one word address at the end. 
* If the input file does not have a stop statement in the end which is indicated by the keyword 'STP' then an error is shown.

### Second Pass
The final object code is generated in the second pass. The values of opcodes and operands in binary are fetched from the opcode table. For labels, the address of the label is taken from the symbol table. The object code is written in a txt file. If in the first pass all symbols are not found then signals error.

### Error Handled
* Check if the opcode is valid.
* Check if the called variable is declared or not.
* Ensure that each label is declared only once.
* Ensure that macro name is not same as that of opcode.
* Ensure no two macros have the same name.
* Ensure that each opcode is given the correct number of opcodes. Neither less nor more.
* Ensure that label is declared before being called by branching opcodes. To ensure that each called label is declared, forward referencing is used.
* STP is missing

### Type of Error handling
The program does not terminate after reporting one error. It continues to assemble the rest of the code and It reports all the errors in all the lines at once.
