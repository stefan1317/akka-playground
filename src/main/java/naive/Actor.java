package naive;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Actor {

    private final String name;
    private final Deque<String> mailbox;

    public Actor(String name) {
        this.name = name;
        this.mailbox = new ArrayDeque<>();
    }

    public void sendMessage(String message) {
        mailbox.addLast(message);

        String messageFromQue = this.mailbox.removeFirst();

        // process the messageFromQue
        System.out.println("Actor " + this.name + " processed the message.");
    }

    static void main() {
        Actor firstActor = new Actor("First Actor");
        Actor secondActor = new Actor("Second Actor");

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 2; i++) {
            executorService.execute(() -> {
                firstActor.sendMessage("Hello, Actor! - 1");
                firstActor.sendMessage("Hello, Actor! - 1");
                secondActor.sendMessage("Hello, Actor! - 2");
            });
        }
    }
}
