package main;

public class ListCommand extends Command
{
    private String format;
    
    public ListCommand(String url, String format)
	{
        super(url);
        this.format = format;
    }
    
    public void execute() {}
    public String getUrl(){return url;}
    public String getFormat(){return format;}
}
