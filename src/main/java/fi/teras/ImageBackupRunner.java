package fi.teras;

import java.io.File;
import java.io.IOException;

public class ImageBackupRunner {
    static public void main(String[] args) {
        try {
            ImageBackupHelper helper = new ImageBackupHelper();
            helper.backupImages(new File(args[0]).toPath(), new File(args[1]).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
