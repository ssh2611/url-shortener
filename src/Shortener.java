import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/shortener")
public class Shortener {

    Map<Integer, String> urls= new HashMap<Integer, String>();
    String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    int i;

    public Shortener() {
        i = 1;
        urls.put(i, "http://www.google.com/");
        i+=1;
        urls.put(i, "http://www.youtube.com/");

    }

    @GET
    @Path("{shortenedUrl}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRealUrl (@PathParam("shortenedUrl") int shortenedUrl) {
        String real_url = urls.get(shortenedUrl);
        System.out.println(real_url);
        if (real_url == null)
        {
            return Response.status(404).entity("").build();
        }
        try {
            return Response.status(Response.Status.MOVED_PERMANENTLY).location(new URI(real_url)).build();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GET
    @Path("/")
    public String getAll () {
        return urls.keySet().toString();
    }
//
//    @PUT
//    @Path("/{id}")
//    public Response getById (@PathParam("id") int id) {
//        try {
//            String find_url = urls.get(id);
//
//            if (find_url == null) {
//                return Response.status(404).entity("").build();
//            }
//            return Response.status(Response.Status.OK).entity(find_url).build();
//        } catch (Exception e){
//            throw new BadRequestException("error");
//        }
//    }

    @POST
    @Path("/")
    public Response postUrl(@QueryParam("url") String url)
    {
        if (IsMatch(url, regex)) {
            i += 1;
            urls.put(i, url);
            return Response.status(Response.Status.CREATED).entity(Integer.toString(i)).build();
        }
        else {
            return Response.status(400).entity("error").build();
        }
    }



    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") int id)
    {
        String find_url = urls.get(id);
        if (find_url == null)
        {
            return Response.status(404).entity("").build();
        }
        urls.remove(id);
        return Response.status(204).entity("").build();
    }

    @DELETE
    @Path("/")
    public void deleteAll()
    {
        urls = new HashMap<Integer, String>();
        i = 0;
    }

    private static boolean IsMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }

}


//public class Shortener {
//	@GET
//	@Produces("application/xml")
//	public String convertCtoF() {
// 
//		Double fahrenheit;
//		Double celsius = 36.8;
//		fahrenheit = ((celsius * 9) / 5) + 32;
// 
//		String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
//		return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
//	}
// 
//	@Path("{c}")
//	@GET
//	@Produces("application/xml")
//	public String convertCtoFfromInput(@PathParam("c") Double c) {
//		Double fahrenheit;
//		Double celsius = c;
//		fahrenheit = ((celsius * 9) / 5) + 32;
// 
//		String result = "@Produces(\"application/xml\") Output: \n\nC to F Converter Output: \n\n" + fahrenheit;
//		return "<ctofservice>" + "<celsius>" + celsius + "</celsius>" + "<ctofoutput>" + result + "</ctofoutput>" + "</ctofservice>";
//	}
//}