package edu.upc.eetac.dsa.beeter;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by marc on 25/02/16.
 */
public class BeeterResourceConfig extends ResourceConfig {

    public BeeterResourceConfig() {
        packages("edu.upc.eetac.dsa.beeter");
    }
}
