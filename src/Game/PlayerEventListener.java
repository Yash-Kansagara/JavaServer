package Game;

public interface PlayerEventListener {
    public void messageReceived(PlayerEvent e);
    public void messageReceivedUnreliable(PlayerEvent e);
}
