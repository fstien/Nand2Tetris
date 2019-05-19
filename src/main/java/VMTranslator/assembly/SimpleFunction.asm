// function SimpleFunction.test 2
(SimpleFunction.test)
// push constant 0
@0
D=A
@0
A=M
M=D
@0
M=M+1
// push constant 0
@0
D=A
@0
A=M
M=D
@0
M=M+1
// push local 0
@1
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
// push local 1
@1
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
// not
@0
D=M
D=D-1
A=D
M=!M
// push argument 0
@2
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
// push argument 1
@2
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
// return
@0
A=M
A=A-1
D=M
@R12
M=D
@1
D=M
@R15
M=D
@1
A=M
// Moving: -5 pos
A=A-1
A=A-1
A=A-1
A=A-1
A=A-1
D=M
@R14
M=D
@R12
D=M
@2
A=M
M=D
@2
D=M
D=D+1
@0
M=D
@R15
A=M
// Moving: -1 pos
A=A-1
D=M
@4
M=D
@R15
A=M
// Moving: -2 pos
A=A-1
A=A-1
D=M
@3
M=D
@R15
A=M
// Moving: -3 pos
A=A-1
A=A-1
A=A-1
D=M
@2
M=D
@R15
A=M
// Moving: -4 pos
A=A-1
A=A-1
A=A-1
A=A-1
D=M
@1
M=D
(END)
@END
0;JMP
