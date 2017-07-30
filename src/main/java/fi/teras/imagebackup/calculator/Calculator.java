package fi.teras.imagebackup.calculator;

import java.nio.file.Path;
import java.util.List;

public class Calculator {
    public static int getSucceededSize(final List<Path> succeeded) {
        return succeeded.size() > 0 ? succeeded.size() : 1;
    }

    public static Double countSuccessPercent(final List<Path> succeeded, final List<Path> failed) {
        int totalCount = succeeded.size() + failed.size();
        return Double.valueOf(100 * (totalCount - failed.size()) / (double) getSucceededSize(succeeded));
    }
}
