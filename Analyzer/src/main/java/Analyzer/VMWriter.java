package Analyzer;


public class VMWriter {

    public VMWriter(String fileName) {}

    public void WritePush(Segment segment) {}

    public void writePop(Segment segment) {}

    public void writeArithmetic(Command command) {}

    public void writeLabel(String label) {}

    public void writeGoto(String label) {}

    public void writeIf(String label) {}

    public void writeCall(String name, int nArgs) {}

    public void writeFunction(String name, int nLocals) {}

    public void writeReturn() {}

    public void close() {}
}
