package test;

import main.CLI;
import main.ListCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class test {

    //Tests ListCommand
    @Test
    public void testListCommandCreation() {
        ListCommand cmd = new ListCommand(
                "http://localhost:3000/shop-api",
                "table"
        );
        assertNotNull(cmd);
    }

    @Test
    public void testListCommandUrlStored() {
        ListCommand cmd = new ListCommand(
                "http://localhost:3000/shop-api",
                "table"
        );
        assertEquals("http://localhost:3000/shop-api", cmd.getUrl());
    }

    @Test
    public void testListCommandFormatTable() {
        ListCommand cmd = new ListCommand(
                "http://localhost:3000/shop-api",
                "table"
        );
        assertEquals("table", cmd.getFormat());
    }

    @Test
    public void testListCommandFormatJson() {
        ListCommand cmd = new ListCommand(
                "http://localhost:3000/shop-api",
                "json"
        );
        assertEquals("json", cmd.getFormat());
    }

    @Test
    public void testListCommandInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ListCommand(
                    "http://localhost:3000/shop-api",
                    "xml"
            );
        });
    }

    // Tests URL

    @Test
    public void testListCommandNullUrl() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ListCommand(null, "table");
        });
    }

    @Test
    public void testListCommandEmptyUrl() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ListCommand("", "table");
        });
    }


    //Tests CLI principal

    @Test
    public void testCLINoArgs() {
        assertThrows(IllegalArgumentException.class, () -> {
            CLI cli = new CLI(new String[]{});
            cli.run();
        });
    }

    @Test
    public void testCLIWithUrlBeforeCommand() {
        CLI cli = new CLI(new String[]{
                "--url", "http://localhost:3000/shop-api", "list"
        });
        assertNotNull(cli);
    }

    @Test
    public void testCLIWithUrlAfterCommand() {
        CLI cli = new CLI(new String[]{
                "list", "--url", "http://localhost:3000/shop-api"
        });
        assertNotNull(cli);
    }
}