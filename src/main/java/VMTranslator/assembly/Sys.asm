// INITIALISE
// function Sys.init 0
// push constant 4000
@4000
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
@R14
M=D
@3
D=A
@0
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// push constant 5000
@5000
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
@R14
M=D
@3
D=A
@1
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// call Sys.main 0
// pop temp 1
@0
M=M-1
A=M
D=M
@R14
M=D
@null
D=M
@1
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// label LOOP
(LOOP)
// goto LOOP
@LOOP
0;JMP
// function Sys.main 5
// push constant 4001
@4001
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
@R14
M=D
@3
D=A
@0
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// push constant 5001
@5001
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
@R14
M=D
@3
D=A
@1
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// push constant 200
@200
D=A
@0
A=M
M=D
@0
M=M+1
// pop local 1
@0
M=M-1
A=M
D=M
@R14
M=D
@1
D=M
@1
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// push constant 40
@40
D=A
@0
A=M
M=D
@0
M=M+1
// pop local 2
@0
M=M-1
A=M
D=M
@R14
M=D
@1
D=M
@2
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// push constant 6
@6
D=A
@0
A=M
M=D
@0
M=M+1
// pop local 3
@0
M=M-1
A=M
D=M
@R14
M=D
@1
D=M
@3
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// push constant 123
@123
D=A
@0
A=M
M=D
@0
M=M+1
// call Sys.add12 1
// pop temp 0
@0
M=M-1
A=M
D=M
@R14
M=D
@null
D=M
@0
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
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
// push local 2
@1
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
// push local 3
@1
D=M
@3
D=D+A
A=D
D=M
@0
A=M
M=D
@0
M=M+1
// push local 4
@1
D=M
@4
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
// return
// function Sys.add12 0
// push constant 4002
@4002
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
@R14
M=D
@3
D=A
@0
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
// push constant 5002
@5002
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
@R14
M=D
@3
D=A
@1
D=D+A
@R15
M=D
@R14
D=M
@R15
A=M
M=D
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
// push constant 12
@12
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
// return
(END)
@END
0;JMP
