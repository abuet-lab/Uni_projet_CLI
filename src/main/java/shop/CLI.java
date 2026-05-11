package shop;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "cli",
    subcommands = {ListCommand.class, CartCommand.class})
public class CLI implements Runnable {

  @Option(names = "--url")
  private String url;

  public static void main(String[] args) {
    if (args.length == 0) throw new IllegalArgumentException("Aucun argument");
    new CommandLine(new CLI()).execute(args);
  }

  public void run() {}

  public String getUrl() {
    return url;
  }
}
