package fi.teras.imagebackup.helper;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;

public class FileHelper {
    private static final Logger log = LoggerFactory.getLogger("fi.teras.imagebackup.helper.FileHelper");

    private static Directory getFileMimeType(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        return metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    }

    public static boolean isImageOrVideo(Path path) {
        try {
            ImageIO.read(new File(path.toString()));
            return !path.toString().contains(".DS_Store");
        } catch (Exception e) {
            return false;
        }
    }

    public static String getMonthFolderName(Integer monthNumber, String monthFolderName) {
        monthNumber++;
        return (monthNumber.toString().length() < 2 ? "0" + monthNumber : monthNumber) + "_" + monthFolderName;
    }

    public static boolean hasDateTaken(Path imagePath) {
        return getDateTaken(imagePath) != null;
    }

    public static boolean hasCreationTime(Path filePath) {
        return getCreationDate(filePath) != null;
    }

    public static Calendar getDateTaken(Path imagePath) {
        Date dateTaken;
        try {
            dateTaken = getFileMimeType(imagePath.toFile()).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTaken);
            return cal;
        } catch (Exception e) {
            log.warn("Error on image processing when getting date taken: ", e.getMessage());
            return null;
        }
    }

    public static Calendar getCreationDate(Path filePath) {
        try {
            BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
            attr.creationTime().toInstant();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(attr.creationTime().toInstant().toEpochMilli()));
            return cal;
        } catch (IOException e) {
            log.warn("IO error on getting file creation date", e.getMessage());
        }
        return null;
    }
}
