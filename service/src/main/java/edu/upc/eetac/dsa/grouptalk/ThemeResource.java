package edu.upc.eetac.dsa.grouptalk;

import edu.upc.eetac.dsa.grouptalk.DAO.*;
import edu.upc.eetac.dsa.grouptalk.entity.Theme;
import edu.upc.eetac.dsa.grouptalk.entity.ThemeCollection;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

/**
 * Created by mbmarkus on 12/04/16.
 */
@Path("theme")
public class ThemeResource {

    @Context
    private SecurityContext securityContext;

    @Path("/{idgrupo}/{idusuario}")
    @GET
    @Produces(GroupTalkMediaType.grouptalk_THEMECOLLECTION)
    public ThemeCollection getThemes(@PathParam("idgrupo") String idgrupo, @PathParam("idusuario") String idusuario) throws URISyntaxException {

        ThemeDAO themeDAO = new ThemeDAOImpl();
        ThemeCollection themeCollection = null;

        if(idgrupo==null || idusuario==null)
            throw new BadRequestException("all parameters are mandatory");

        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        try {
            themeCollection = themeDAO.getThemesByGroup(idgrupo, idusuario);
            if(themeCollection == null)
                throw new NotFoundException("Ningún tema encontrado");
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserNotInscribedException e) {
            throw new ForbiddenException("operation not allowed");
        }

        return themeCollection;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GroupTalkMediaType.grouptalk_THEME)
    public Response createTheme(@FormParam("idusuario") String idusuario, @FormParam("idgrupo") String idgrupo, @FormParam("idgrupo") String subject, @Context UriInfo uriInfo) throws URISyntaxException {
        if (idusuario == null || idgrupo == null || subject == null)
            throw new BadRequestException("all parameters are mandatory");
        ThemeDAO themeDAO = new ThemeDAOImpl();
        Theme theme = null;
        URI uri = null;

        String userid = securityContext.getUserPrincipal().getName();
        if (!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");

        try {
            theme = themeDAO.createTheme(subject, idusuario, idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserNotInscribedException e) {
            throw new ForbiddenException("Usuario no inscrito");
        } catch (ThemeAlreadyExistsException e) {
            throw new ForbiddenException("Tema ya creado");

        }

        return Response.created(uri).type(GroupTalkMediaType.grouptalk_THEME).entity(theme).build();
    }

    @Path("/{idgrupo}/{idusuario}")
    @DELETE
    public void deleteTheme(@PathParam("idgrupo") String idgrupo, @PathParam("idusuario") String idusuario) throws URISyntaxException {
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");
        ThemeDAO themeDAO = new ThemeDAOImpl();

        try {
            themeDAO.deleteThemeandStingsbyUser(idusuario, idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        } catch (UserNoGenuineUpdateStingException e) {
            throw new ForbiddenException("operation not allowed");
        }
    }

    @Path("/{idgrupo}/{idusuario}/")
    @DELETE
    public void deleteThemeAdmin(@PathParam("idgrupo") String idgrupo, @PathParam("idusuario") String idusuario) throws URISyntaxException {
        String userid = securityContext.getUserPrincipal().getName();
        if(!userid.equals(idusuario))
            throw new ForbiddenException("operation not allowed");
        if(securityContext.isUserInRole("admin"))
            throw new ForbiddenException("operation not allowed");
        ThemeDAO themeDAO = new ThemeDAOImpl();

        try {
            themeDAO.deleteThemeandStingsbyAdmin(idusuario, idgrupo);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
    }

    }
}
