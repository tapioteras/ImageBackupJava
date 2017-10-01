package fi.teras.imagebackup.resolver;

import fi.teras.imagebackup.Configuration;
import fi.teras.imagebackup.copier.Copier;
import fi.teras.imagebackup.helper.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;

public class Resolver {
    private static final Logger log = LoggerFactory.getLogger("fi.teras.imagebackup.Resolver");

    public static String resolveMonthPart(int month) {
        return Configuration.MONTH_FOLDER_NAME_MAP.entrySet()
                .stream()
                .filter(e -> e.getKey() == month)
                .map(e -> FileHelper.getMonthFolderName(e.getKey(), e.getValue()))
                .findFirst()
                .orElse(null);
    }

    public static void resolvePathTo(Path imagePath, Path to) throws IOException {
        Calendar cal;
        if (FileHelper.hasDateTaken(imagePath)) {
            cal = FileHelper.getDateTaken(imagePath);
        } else if (FileHelper.hasCreationTime(imagePath)) {
            cal = FileHelper.getCreationDate(imagePath);
        } else {
            throw new NullPointerException("no suitable date found");
        }
        Copier.copy(to, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), imagePath);
    }
}
