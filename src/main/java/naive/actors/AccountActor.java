package naive.actors;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.math.BigDecimal;

public class AccountActor extends AbstractBehavior<AccountActor.Command> {

    private BigDecimal balance = BigDecimal.ZERO;

    private AccountActor(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(AccountActor::new);
    }

    public interface Command {}

    public record Deposit(BigDecimal amount) implements Command {}

    public record Withdraw(BigDecimal amount) implements Command {}

    public record PrintBalance() implements Command {}

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Deposit.class, this::onDeposit)
                .onMessage(Withdraw.class, this::onWithdraw)
                .onMessage(PrintBalance.class, this::onPrintBalance)
                .build();
    }

    private Behavior<Command> onDeposit(Deposit command) {
        balance = balance.add(command.amount);

        return this;
    }

    private Behavior<Command> onWithdraw(Withdraw command) {
        balance = balance.subtract(command.amount);

        return this;
    }

    private Behavior<Command> onPrintBalance(PrintBalance command) {
        getContext().getLog().info("Current balance: {}", balance);

        return this;
    }

    static void main() {
        ActorSystem<Command> system = ActorSystem.create(AccountActor.create(), "AccountActorSystem");

        system.tell(new Deposit(new BigDecimal("100.00")));
        system.tell(new PrintBalance());
        system.tell(new Withdraw(new BigDecimal("50.00")));
        system.tell(new PrintBalance());

        system.terminate();
    }
}
