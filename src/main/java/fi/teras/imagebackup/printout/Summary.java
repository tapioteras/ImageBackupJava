package fi.teras.imagebackup.printout;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import fi.teras.imagebackup.calculator.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public class Summary {
    private static final Logger log = LoggerFactory.getLogger("fi.teras.imagebackup.Summary");

    public static void printSummary(final List<Path> succeeded, final List<Path> failed) {
        try {
            log.info("\n\n##### Image backup completed #####");
            V2_AsciiTable at = new V2_AsciiTable();
            at.addRule();
            at.addRow("Success percent", Calculator.countSuccessPercent(succeeded, failed) + " %");
            at.addRule();
            at.addRow("Succeeded", succeeded.size() + " files");
            at.addRule();
            at.addRow("Failed", failed.size() + " files");
            at.addStrongRule();
            at.addRow("Total", succeeded.size() + failed.size() + " files");
            at.addRule();
            V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
            rend.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
            rend.setWidth(new WidthAbsoluteEven(50));
            RenderedTable rt = rend.render(at);
            log.info("\n" + String.valueOf(rt));
        } catch (Exception e) {
            log.warn("Image backup summary cannot be printed because exception: " + e.getMessage());
        }
    }
}
