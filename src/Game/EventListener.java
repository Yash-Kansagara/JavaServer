package Game;

public interface EventListener {
    public void messageReceived(Event e);
    public void messageReceivedUnreliable(Event e);
}
