package shop;

import static org.junit.jupiter.api.Assertions.*;

public class test {

  // Tests ListCommand
  @org.junit.jupiter.api.Test
  public void testListCommandCreation() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "table");
    assertNotNull(cmd);
  }

  @org.junit.jupiter.api.Test
  public void testListCommandUrlStored() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "table");
    assertEquals("http://localhost:3000/shop-api", cmd.getUrl());
  }

  @org.junit.jupiter.api.Test
  public void testListCommandFormatTable() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "table");
    assertEquals("table", cmd.getFormat());
  }

  @org.junit.jupiter.api.Test
  public void testListCommandFormatJson() {
    ListCommand cmd = new ListCommand("http://localhost:3000/shop-api", "json");
    assertEquals("json", cmd.getFormat());
  }

  @org.junit.jupiter.api.Test
  public void testListCommandInvalidFormat() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ListCommand("http://localhost:3000/shop-api", "xml");
        });
  }

  // Tests URL

  @org.junit.jupiter.api.Test
  public void testListCommandNullUrl() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ListCommand(null, "table");
        });
  }

  @org.junit.jupiter.api.Test
  public void testListCommandEmptyUrl() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new ListCommand("", "table");
        });
  }

  // Tests CLI principal

  @org.junit.jupiter.api.Test
  public void testCLINoArgs() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          CLI cli = new CLI();
          cli.run();
        });
  }

  @org.junit.jupiter.api.Test
  public void testCLIWithUrlBeforeCommand() {
    // Utilise le constructeur sans argument qui existe réellement
    CLI cli = new CLI();

    // Pour tester si picocli accepte les arguments :
    int exitCode =
        new picocli.CommandLine(cli).execute("--url", "http://localhost:3000/shop-api", "list");

    assertEquals(0, exitCode); // 0 signifie que l'exécution a réussi
    assertNotNull(cli);
  }

  @org.junit.jupiter.api.Test
  public void testCLIWithUrlAfterCommand() {
    CLI cli = new CLI();

    int exitCode =
        new picocli.CommandLine(cli).execute("list", "--url", "http://localhost:3000/shop-api");

    assertEquals(0, exitCode);
    assertNotNull(cli);
  }
}
