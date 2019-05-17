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
@3000
D=A
@3
M=D
@3010
D=A
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
// pop local 0
@0
M=M-1
A=M
D=M
@300
M=D
// label LOOP_START
(LOOP_START)
// push argument 0
@400
D=M
@0
A=M
M=D
@0
M=M+1
// push local 0
@300
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
// pop local 0
@0
M=M-1
A=M
D=M
@300
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
// push argument 0
@400
D=M
@0
A=M
M=D
@0
M=M+1
// if-goto LOOP_START
@0
M=M-1
A=M
D=M
@LOOP_START
D;JGT
// push local 0
@300
D=M
@0
A=M
M=D
@0
M=M+1
(END)
@END
0;JMP
