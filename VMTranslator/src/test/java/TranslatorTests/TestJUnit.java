package TranslatorTests;

import VMTranslator.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestJUnit {


    @Test
    void parse_SimpleAdd() {
        Parser p = new Parser("SimpleAdd.vm");

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(7, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(8, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("add", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertFalse(p.hasMoreCommands());
    }

    @Test
    void parse_StackTest() {
        Parser p = new Parser("StackTest.vm");

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(17, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(17, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("eq", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();



        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(17, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(16, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("eq", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();



        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(16, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(17, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("eq", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();




        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(892, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(891, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("lt", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());



        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(891, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(892, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("lt", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());




        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(891, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(891, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("lt", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());



        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(32767, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(32766, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("gt", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());






        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(32766, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(32767, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("gt", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());





        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(32766, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(32766, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("gt", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());




        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(57, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());



        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(31, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());


        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(53, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());



        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("add", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());


        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(112, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());



        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("sub", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("neg", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("and", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());




        p.advance();

        Assertions.assertEquals("C_PUSH", p.commandType);
        Assertions.assertEquals("constant", p.arg1);
        Assertions.assertEquals(82, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());


        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("or", p.arg1);
        Assertions.assertEquals(null, p.arg2);
        Assertions.assertTrue(p.hasMoreCommands());

        p.advance();

        Assertions.assertEquals("C_ARITHMETIC", p.commandType);
        Assertions.assertEquals("not", p.arg1);
        Assertions.assertEquals(null, p.arg2);

        Assertions.assertFalse(p.hasMoreCommands());


    }

}
