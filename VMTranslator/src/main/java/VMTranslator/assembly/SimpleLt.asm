// INITIALISE
@256
D=A
@0
M=D
A=M
M=0
A=A+1
M=0
A=A+1
M=0
A=A+1
M=0
A=A+1
M=0
A=A+1
M=0
@0
// push constant 10
@10
D=A
@0
A=M
M=D
@0
M=M+1
// push constant 15
@15
D=A
@0
A=M
M=D
@0
M=M+1
// gt
@0
D=M
D=D-1
A=D
D=M
A=A-1
M=M-D
D=M
M=0
@DI
D;JLE
@0
D=M
D=D-1
D=D-1
A=D
M=M-1
(DI)
@0
M=M-1
(END)
@END
0;JMP