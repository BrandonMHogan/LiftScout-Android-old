package rx;

/**
 * Created by Brandon on 3/28/2017.
 * Description : This is a workout for a Robolectric bug: robolectric/robolectric#2208
 * Basically, it gives robolectric access to Observable, even though its not using it.
 * This is not working in 3.1.2, needed to revert to 3.0. Waiting for a fix in a later version
 */

public class Observable {
}
