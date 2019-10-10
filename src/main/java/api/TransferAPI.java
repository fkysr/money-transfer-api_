package api;

import logic.TransferLogic;
import model.Transfer;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransferAPI {
    @POST()
    public Response createTransfer(Transfer transfer){
        TransferLogic transferLogic=TransferLogic.getInstance();
        int result= transferLogic.transferMoney(transfer);
        if(result==1)
            return Response.ok().build();
        else{
            //Response.ok().build();
            return Response.ok().build();
        }
    }
}
