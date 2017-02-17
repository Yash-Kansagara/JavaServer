package HomeServer;

import Util.Container;
import Constants.Const;
import Game.*;

public abstract class HomeServerEventListener implements EventListener {

    @Override
    public void messageReceived(Event e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void messageReceivedUnreliable(Event e) {
        // TODO Auto-generated method stub
         Container c = e.container;
        //byte code = (byte)c.table.get(ParameterCodes.operationCode);
        if(e.code == HomeServerOperationCode.ACK){
            byte operation = (byte)c.table.get(ParameterCodes.operationCodeACK);
            if(operation == GameServerOperationCode.CREATE_GAME){
                boolean success = Const.RESULT_OK == (byte)c.table.get(ParameterCodes.result);
                
                if(success){
                    OnRoomCreatedSuccess(e);
                }else{
                    OnRoomCreateFailed(e);
                }
            }
        }
    }
    
    public abstract void OnRoomCreatedSuccess(Event roomName);
    
    public abstract void OnRoomCreateFailed(Event roomName);
}
