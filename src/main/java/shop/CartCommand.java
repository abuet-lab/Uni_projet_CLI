package shop;

import picocli.CommandLine.ParentCommand;

@picocli.CommandLine.Command(name = "cart", description = "Gère le panier")
public class CartCommand extends Command implements Runnable {

  @ParentCommand private CLI parent;

  // Constructeur sans argument OBLIGATOIRE pour picocli
  public CartCommand() {
    super(null);
  }

  @Override
  public void run() {
    this.url = parent.getUrl();
    if (this.url == null) this.url = System.getenv("URL");
    try {
      execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void execute() throws Exception {
    System.out.println("Cart command - pas encore implémenté");
  }
}
