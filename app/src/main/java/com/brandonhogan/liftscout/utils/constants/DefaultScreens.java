package com.brandonhogan.liftscout.utils.constants;

import java.util.ArrayList;

/**
 * Created by Brandon on 2/23/2017.
 * Description :
 */

public class DefaultScreens {
    public static final String CALENDAR = "Calendar";
    public static final String TODAY = "Today";

    public static final ArrayList<String> SCREENS = new ArrayList<String>() {{
        add(TODAY);
        add(CALENDAR);
    }};
}
