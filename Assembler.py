import sys
import re


f = open(sys.argv[1], "r")
lines = []
for line in f.readlines(): 
    lines.append(line)
    
noWS = []

def rep(st):
    return st.replace(" ", "").replace("\n","")

for line in lines:
    sp = line.split("//")
    if len(rep(sp[0])) > 0:
        noWS.append(rep(sp[0]))
        
symbolTable = {
    # Pre-defined symbols
    'R0': '0',
    'R1': '1',
    'R2': '2',
    'R3': '3',
    'R4': '4',
    'R5': '5',
    'R6': '6',
    'R7': '7',
    'R8': '8',
    'R9': '9',
    'R1O': '10',
    'R11': '11',
    'R12': '12',
    'R13': '13',
    'R14': '14',
    'R15': '15',
    'SCREEN': '16384',
    'KBD': '24576',
    'SP': '0',
    'LCL': '1',
    'ARG': '2',
    'THIS': '3',
    'THAT': '4',
}

linesToRemove = []
lineNumber = 0
for i in range(len(noWS)):
    line = noWS[i]
    if line[0] == '(':
        symbolTable[line[1:len(line)-1]] = str(lineNumber)
        linesToRemove.append(line)
    else:
        lineNumber = lineNumber + 1

for line in linesToRemove:
    noWS.remove(line)

def RepresentsInt(s):
    try: 
        int(s)
        return True
    except ValueError:
        return False
    
cAddress = 16

for line in noWS:
    if line[0] == '@':
        sym = line[1:len(line)]        
        if sym not in symbolTable:
            if not(RepresentsInt(sym)):
                symbolTable[sym] = str(cAddress)
                cAddress = cAddress + 1

for i in range(len(noWS)):
    line = noWS[i]
    if line[0] == '@':
        sym = line[1:len(line)]        
        if not(RepresentsInt(sym)):
            noWS[i] = noWS[i].replace(sym, symbolTable[sym])
            

def binary(num):
    num = int(num)
    bStr = bin(num).split('b')[1]
    bLen = len(bStr)
    padLen = 15-bLen
    pad = '0'*padLen
    pad += bStr
    return pad

for i in range(len(noWS)):
    if noWS[i][0] == "@":
        noWS[i] = "0" + binary(noWS[i][1:])
        
jumpTable = {
    '': '000',
    'JGT': '001',
    'JEQ': '010',
    'JGE': '011',
    'JLT': '100',
    'JNE': '101',
    'JLE': '110',
    'JMP': '111',
}

def jumpBin(jump):
    return jumpTable[jump]


destTable = {
    '': '000',
    'M': '001',
    'D': '010',
    'MD': '011',
    'A': '100',
    'AM': '101',
    'AD': '110',
    'AMD': '111',
}

def destBin(dest):
    return destTable[dest]

compTable = {
    '0': '0101010',
    '1': '0111111',
    '-1': '0111010',
    'D': '0001100',
    'A': '0110000',
    '!D': '0001101',
    '!A': '0110001',
    '-D': '0001111',
    '-A': '0110011',
    'D+1': '0011111',
    'A+1': '0110111',
    'D-1': '0001110',
    'A-1': '0110010',
    'D+A': '0000010',
    'D-A': '0010011',
    'A-D': '0000111',
    'D&A': '0000000',
    'D|A': '0010101',
    
    'M': '1110000',
    '!M': '1110001',
    '-M': '1110011',
    'M+1': '1110111',
    'M-1': '1110010',
    'D+M': '1000010',
    'D-M': '1010011',
    'M-D': '1000111',
    'D&M': '1000000',
    'D|M': '1010101',
}
def compBin(comp):
    return compTable[comp]

def cParse(cString):
    cVec = re.split('=|;', cString)

    if '=' not in cString:
        cVec = [''] + cVec

    if ';' not in cString:
        cVec = cVec + ['']
        
    dest = cVec[0]
    comp = cVec[1]
    jump = cVec[2]
        
    return {
        'dest': dest,
        'comp': comp,
        'jump': jump
    }

def cBinary(cBits):
    destBits = destBin(cBits['dest'])
    compBits = compBin(cBits['comp'])
    jumpBits = jumpBin(cBits['jump'])
    
    cInst = '111'
    cInst += compBits
    cInst += destBits
    cInst += jumpBits
   
    return cInst
    

for i in range(len(noWS)):
    if len(noWS[i]) != 16:
        cParsed = cParse(noWS[i])
        cInst = cBinary(cParsed)
        noWS[i] = cInst

fName = sys.argv[1].split('.')[0] + '.hack'

open(fName, 'w').close()

f = open(fName, "a")

for line in noWS:
    f.write(line + '\n')
    
f.close()