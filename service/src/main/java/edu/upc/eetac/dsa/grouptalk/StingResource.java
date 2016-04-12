package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.DAO.StingDAO;
import edu.upc.eetac.dsa.grouptalk.DAO.StingDAOImpl;
import edu.upc.eetac.dsa.grouptalk.DAO.UserNoGenuineUpdateStingException;
import edu.upc.eetac.dsa.grouptalk.entity.Sting;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by mbmarkus on 12/04/16.
 */
@Path("stings")
public class StingResource {
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.grouptalk_STING)
    public Response createMensaje(@FormParam("idtema") String idtema, @FormParam("idusuario") String idusuario, @FormParam("content") String content, @Context UriInfo uriInfo) throws URISyntaxException {
        if(idtema==null || content == null)
            throw new BadRequestException("all parameters are mandatory");
        StingDAO stingDAO = new StingDAOImpl();
        Sting sting = null;

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");
        try {
             sting = stingDAO.createSting(idusuario, idtema, content);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + sting.getId());
        return Response.created(uri).type(GroupTalkMediaType.grouptalk_STING).entity(sting).build();
    }

    @PUT
    @Consumes(GroupTalkMediaType.grouptalk_STING)
    @Produces(GroupTalkMediaType.grouptalk_STING)
    public Sting updateUSting(Sting sting) {
        StingDAO stingDAO = new StingDAOImpl();

        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(sting.getUserid()))
            throw new ForbiddenException("operation not allowed");
        try {

            if (sting == null)
                throw new BadRequestException("Sting vacío");

            sting = stingDAO.updateSting(sting.getId(), userid, sting.getContent());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserNoGenuineUpdateStingException e) {
            throw new ForbiddenException("operation not allowed");
        }
        return sting;
    }
}
