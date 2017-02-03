package Game;

import HomeServer.HomeServerOperationCode;
import Util.Container;

public abstract class GameServerEventListener implements EventListener{

    @Override
    public void messageReceived(Event e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void messageReceivedUnreliable(Event e) {
        Container c = e.container;
        byte code = (byte)c.table.get(ParameterCodes.operationCode);
        if(code == HomeServerOperationCode.ACK){
            byte operation = (byte)c.table.get(ParameterCodes.operationCodeACK);
            if(operation == HomeServerOperationCode.REGISTER_GAMESERVER){
                RegisteredOnHomeServer(e);
            }
        }
    }
    
    
    public abstract void RegisteredOnHomeServer(Event e);
}
