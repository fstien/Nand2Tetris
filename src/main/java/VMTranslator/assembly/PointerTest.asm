// INITIALISE
// push constant 3030
@3030
D=A
@0
A=M
M=D
@0
M=M+1
// pop pointer 0
@0
M=M-1
A=M
D=M
@3
D=M
@0
D=D+A
A=D
M=D
// push constant 3040
@3040
D=A
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
@3
D=M
@1
D=D+A
A=D
M=D
// push constant 32
@32
D=A
@0
A=M
M=D
@0
M=M+1
// pop this 2
@0
M=M-1
A=M
D=M
@null
D=M
@2
D=D+A
A=D
M=D
// push constant 46
@46
D=A
@0
A=M
M=D
@0
M=M+1
// pop that 6
@0
M=M-1
A=M
D=M
@null
D=M
@6
D=D+A
A=D
M=D
// push pointer 0
@3
D=M
@0
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
// push pointer 1
@3
D=M
@1
D=D+A
A=D
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
// push this 2
@null
D=M
@2
D=D+A
A=D
D=M
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
// push that 6
@null
D=M
@6
D=D+A
A=D
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
(END)
@END
0;JMP
