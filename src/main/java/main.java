import model.Currency;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import java.io.IOException;
import java.net.URI;

public class main {

    public static final String BASE_URI = "http://localhost:8090/";
    private static HttpServer server;

    public static HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("api");
        rc.property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        server = startServer();
        System.out.println("Money transfer API running at "+ BASE_URI);
        System.in.read();
        server.shutdownNow();
    }

}
