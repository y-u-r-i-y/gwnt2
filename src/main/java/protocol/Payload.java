package protocol;

/**
 * Created by ysidorov on 25.09.15.
 */
public class Payload {
    public Command command;
    public Object target;

    public Payload() {}

    public Payload(Command command, Object target) {
        this.command = command;
        this.target = target;
    }

    @Override
    public String toString() {
        return "Response{" +
                "command=" + command +
                ", target=" + target +
                '}';
    }
}
