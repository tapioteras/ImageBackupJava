package fi.teras.imagebackup.calculator;

import java.nio.file.Path;
import java.util.List;

public class Calculator {

    public final static Double countSuccessPercent(final List<Path> succeeded, final List<Path> failed) {
        return countSuccessPercent(succeeded.size(), failed.size());
    }

    public final static Double countSuccessPercent(int succeeded, int failed) {
        int total = succeeded + failed;
        return 100 * new Double(total - failed) / new Double(total == 0 ? 1 : total);
    }
}
