package model;

import model.commands.Command;

/**
 * Created by ysidorov on 25.09.15.
 */
public class Response {
    public Command command;
    public Object target;

    public Response() {}

    public Response(Command command, Object target) {
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
