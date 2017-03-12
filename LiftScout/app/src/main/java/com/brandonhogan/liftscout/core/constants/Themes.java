package com.brandonhogan.liftscout.core.constants;

import java.util.ArrayList;

public class Themes {
    public static final String ORIGINAL_DARK = "Dark";
    public static final String GREEN_LIGHT = "Light Green";
    public static final String PURPLE_LIGHT = "Light Purple";

    public static final String ORIGINAL_LIGHT = "Light";
    public static final String GREEN_DARK = "Dark Green";
    public static final String PURPLE_DARK = "Dark Purple";
    public static final String TEAL_DARK = "Dark Teal";
    public static final String BLUE_GRAY_DARK = "Dark Blue Gray";
    public static final String BLACK_DARK = "Dark Black";

    public static final ArrayList<String> THEMES = new ArrayList<String>() {{
        add(ORIGINAL_LIGHT);
        add(GREEN_LIGHT);
        add(PURPLE_LIGHT);
        add(ORIGINAL_DARK);
        add(BLACK_DARK);
        add(GREEN_DARK);
        add(PURPLE_DARK);
        add(TEAL_DARK);
        add(BLUE_GRAY_DARK);
    }};
}
