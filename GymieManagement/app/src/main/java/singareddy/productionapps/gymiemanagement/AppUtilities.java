package singareddy.productionapps.gymiemanagement;

import java.util.HashMap;

public class AppUtilities {

    public static Integer GYM_ID = 456;
    public static HashMap<String, String> ROLE_LOOKUP;

    static {
        ROLE_LOOKUP = new HashMap<>();
        ROLE_LOOKUP.put("R","Receptionist");
    }
}
