// INITIALISE
@256
D=A
@0
M=D
@300
D=A
@1
M=D
@400
D=A
@2
M=D
// push argument 1
@401
D=M
@0
A=M
M=D
@0
M=M+1
// pop pointer 1
@0
M=M-1
A=M
D=M
@4
M=D
// push constant 0
@0
D=A
@0
A=M
M=D
@0
M=M+1
// pop that 0
@0
M=M-1
A=M
D=M
@1
M=D
// push constant 1
@1
D=A
@0
A=M
M=D
@0
M=M+1
// pop that 1
@0
M=M-1
A=M
D=M
@2
M=D
// push argument 0
@400
D=M
@0
A=M
M=D
@0
M=M+1
// push constant 2
@2
D=A
@0
A=M
M=D
@0
M=M+1
// sub
@0
D=M
D=D-1
A=D
D=M
A=A-1
M=M-D
@0
M=M-1
// pop argument 0
@0
M=M-1
A=M
D=M
@400
M=D
// label MAIN_LOOP_START
(MAIN_LOOP_START)
// push argument 0
@400
D=M
@0
A=M
M=D
@0
M=M+1
// if-goto COMPUTE_ELEMENT
@0
M=M-1
A=M
D=M
@COMPUTE_ELEMENT
D;JGT
// goto END_PROGRAM
@END_PROGRAM
0;JMP
// label COMPUTE_ELEMENT
(COMPUTE_ELEMENT)
// push that 0
@1
D=M
@0
A=M
M=D
@0
M=M+1
// push that 1
@2
D=M
@0
A=M
M=D
@0
M=M+1
// add
@0
D=M
D=D-1
A=D
D=M
A=A-1
M=D+M
@0
M=M-1
// pop that 2
@0
M=M-1
A=M
D=M
@3
M=D
// push pointer 1
@4
D=M
@0
A=M
M=D
@0
M=M+1
// push constant 1
@1
D=A
@0
A=M
M=D
@0
M=M+1
// add
@0
D=M
D=D-1
A=D
D=M
A=A-1
M=D+M
@0
M=M-1
// pop pointer 1
@0
M=M-1
A=M
D=M
@4
M=D
// push argument 0
@400
D=M
@0
A=M
M=D
@0
M=M+1
// push constant 1
@1
D=A
@0
A=M
M=D
@0
M=M+1
// sub
@0
D=M
D=D-1
A=D
D=M
A=A-1
M=M-D
@0
M=M-1
// pop argument 0
@0
M=M-1
A=M
D=M
@400
M=D
// goto MAIN_LOOP_START
@MAIN_LOOP_START
0;JMP
// label END_PROGRAM
(END_PROGRAM)
(END)
@END
0;JMP
