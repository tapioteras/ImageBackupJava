package fi.teras.imagebackup;

import fi.teras.imagebackup.helper.ImageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Runner {
    private static final Logger log = LoggerFactory.getLogger("fi.teras.imagebackup.Runner");

    public static void main(String[] args) {
        try {
            ImageHelper.backupImages(new File(args[0]).toPath(), new File(args[1]).toPath());
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
    }
}
