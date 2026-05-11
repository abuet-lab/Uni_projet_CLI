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

  private String[] args;

  public CLI() {}

  public static void main(String[] args) {
    if (args.length == 0) throw new IllegalArgumentException("Aucun argument");
    new CommandLine(new CLI()).execute(args);
  }

  @Override
  public void run() {
    // Si run() est appelé directement sans sous-commande ni args, on lève une exception
    throw new IllegalArgumentException("Aucun argument");
  }

  public String getUrl() {
    return url;
  }
}
