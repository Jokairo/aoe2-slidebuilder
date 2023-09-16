package slidebuilder.util;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

/*

    Passing a method as parameter
    https://www.w3docs.com/snippets/java/java-pass-method-as-parameter.html

    CountDownLatch is used to make so that the program waits that the task finishes
    https://thesoftwareprogrammer.com/2018/01/26/how-can-i-run-something-in-another-thread-and-wait-for-the-result-in-a-different-thread-using-java-javafx/

 */

public class UpdateUIFromOtherThread {
    public static void call(UpdateUIFromOtherThreadFunction f) {
        final CountDownLatch latchToWaitForJavaFx = new CountDownLatch(1);
        try {
            Platform.runLater(() -> {
                try {
                    f.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latchToWaitForJavaFx.countDown();
                }
            });
            latchToWaitForJavaFx.await();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
