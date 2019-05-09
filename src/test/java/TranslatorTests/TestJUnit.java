package TranslatorTests;

import VMTranslator.CodeWriter;
import VMTranslator.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.utilities.Assert;


public class TestJUnit {


    @Test
    void parse_first_command() {
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
        Assertions.assertFalse(p.hasMoreCommands());
    }


}
