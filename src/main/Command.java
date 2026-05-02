package main;

public abstract class Command {
    protected String url;
    
    public Command(String url) {
        this.url = url;
    }
    
    public abstract void execute();
}
