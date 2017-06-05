package fi.teras;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ImageBackupRunner {
    private static final Logger log = LoggerFactory.getLogger("fi.teras.ImageBackupRunner");

    static public void main(String[] args) {
        try {
            ImageBackupHelper helper = new ImageBackupHelper();
            helper.backupImages(new File(args[0]).toPath(), new File(args[1]).toPath());
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
    }
}
