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
// push constant 20
@20
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
// neg
@0
D=M
D=D-1
A=D
M=-M
(END)
@END
0;JMP
