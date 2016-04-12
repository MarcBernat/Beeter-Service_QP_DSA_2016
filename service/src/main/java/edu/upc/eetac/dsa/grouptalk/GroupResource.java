package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.DAO.*;
import edu.upc.eetac.dsa.grouptalk.entity.Group;
import edu.upc.eetac.dsa.grouptalk.entity.GroupCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by mbmarkus on 12/04/16.
 */

@Path("grupos")
public class GroupResource {

    @Context
    private SecurityContext securityContext;

    @GET
    @Produces(GroupTalkMediaType.grouptalk_GROUPCOLLECTION)
    public GroupCollection getGrupos() throws URISyntaxException {
        GroupDAO groupDAO = new GroupDAOImpl();
        GroupCollection groupCollection = null;
        try {
            groupCollection = groupDAO.getGroups();
            if(groupCollection == null)
                throw new NotFoundException("No encontrados");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
        return groupCollection;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.grouptalk_GROUP)
    public Response createGrupo(@FormParam("fullname") String nombretema, @Context UriInfo uriInfo) throws URISyntaxException {
        if(nombretema==null)
            throw new BadRequestException("all parameters are mandatory");
        GroupDAO groupDAO = new GroupDAOImpl();
        Group group = null;

        if(securityContext.isUserInRole("admin"))
            throw new ForbiddenException("operation not allowed");
        try {
            group = groupDAO.createGroup(nombretema);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (GroupAlreadyExistsException e) {
            throw new ForbiddenException("Grupo ya creado");
        }
        URI uri = new URI(uriInfo.getAbsolutePath().toString() + "/" + group.getId());
        return Response.created(uri).type(GroupTalkMediaType.grouptalk_GROUP).entity(group).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void EnterGroup (@FormParam("idgrupo") String idgrupo, @FormParam("idusuario") String idusuario) throws URISyntaxException
    {
        if(idgrupo==null || idusuario==null)
            throw new BadRequestException("all parameters are mandatory");

        UserDAO userDAO = new UserDAOImpl();
        GroupDAO groupDAO = new GroupDAOImpl();

        try {
            if (groupDAO.isInGroup(idusuario, idgrupo))
                throw new  BadRequestException("Not in Group");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        try {
            userDAO.signUpGroup(idusuario, idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void borrarGrupo(@FormParam("idgrupo") String idgrupo, @FormParam("idusuario") String idusuario) throws URISyntaxException
    {
        if(idgrupo==null || idusuario==null)
            throw new BadRequestException("all parameters are mandatory");

        UserDAO userDAO = new UserDAOImpl();
        GroupDAO groupDAO = new GroupDAOImpl();

        try {
            if (!groupDAO.isInGroup(idusuario, idgrupo))
                throw new  BadRequestException("Not in Group");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        try {
            userDAO.signDownGroup(idusuario, idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }
}
