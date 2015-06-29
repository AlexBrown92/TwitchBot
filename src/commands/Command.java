package commands;

/**
 *
 * @author Alex Brown
 */
public class Command {

    int id;
    String prefix;
    String text;
    boolean audience;

    public Command(int id, String prefix, String text, boolean audience) {
        this.id = id;
        this.prefix = prefix;
        this.text = text;
        this.audience = audience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isAudience() {
        return audience;
    }

    public void setAudience(boolean audience) {
        this.audience = audience;
    }
}
