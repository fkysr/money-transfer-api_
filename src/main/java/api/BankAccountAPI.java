
package api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

import logic.BankAccountLogic;
import model.*;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class BankAccountAPI {

    @GET
    public Response getAllBankAccounts(){

        Collection<BankAccount> bankBankAccounts;
        BankAccountLogic bankAccountLogic = BankAccountLogic.getInstance();
        bankBankAccounts = bankAccountLogic.getAllBankAccounts();
        if (bankBankAccounts ==null) {
            Response.noContent().build();
        }
        return Response.ok(bankBankAccounts).build();
    }

    @GET
    @Path("{id}")
    public Response getBankAccountById(@PathParam("id") Long id) {
        BankAccount bankAccount;
        bankAccount = BankAccountLogic.getInstance().getBankAccountById(id);

        if (bankAccount == null) {
            throw new WebApplicationException("The bank account is not exists", Response.Status.NOT_FOUND);
        }
        return Response.ok(bankAccount).build();
    }

    @POST
    public Response createBankAccount(BankAccount bankAccount) {
        //BankAccount result= accountLogic.createBankAccount("henry",new BigDecimal(100),Currency.GBP);
        BankAccount result=  BankAccountLogic.getInstance().createBankAccount(
                bankAccount.getAccountOwnerName(),bankAccount.getBalance(),Currency.GBP);
        return Response.ok(result).build();
    }
}