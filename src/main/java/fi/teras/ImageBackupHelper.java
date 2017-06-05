package fi.teras;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthAbsoluteEven;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageBackupHelper {
    private List<Path> succeeded = new ArrayList<>();
    private List<Path> failed = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger("fi.teras.ImageBackupHelper");

    private final static Map<Integer, String> MONTH_FOLDER_NAME_MAP = Collections.unmodifiableMap(
            Stream.of(new AbstractMap.SimpleEntry<>(0, "tammi"), new AbstractMap.SimpleEntry<>(1, "helmi"), new AbstractMap.SimpleEntry<>(2, "maalis"),
                    new AbstractMap.SimpleEntry<>(3, "huhti"), new AbstractMap.SimpleEntry<>(4, "touko"), new AbstractMap.SimpleEntry<>(5, "kesa"),
                    new AbstractMap.SimpleEntry<>(6, "heina"), new AbstractMap.SimpleEntry<>(7, "elo"), new AbstractMap.SimpleEntry<>(8, "syys"),
                    new AbstractMap.SimpleEntry<>(9, "loka"), new AbstractMap.SimpleEntry<>(10, "marras"), new AbstractMap.SimpleEntry<>(11, "joulu"))
                    .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));

    public void backupImages(Path from, Path to) throws IOException {
        log.info("from: " + from + ", to: " + to + "\n");
        Collection<Path> images = getImages(from);
        images.stream().forEach(imagePath -> {
            try {
                log.info("\n\n----- Processing file " + imagePath + "... -----");
                resolvePathTo(imagePath, to);
                succeeded.add(imagePath);
            } catch (Exception e) {
                failed.add(imagePath);
            }
        });
        printSummary();

    }

    private boolean isImageOrVideo(Path path) {
        try {
            ImageIO.read(new File(path.toString()));
            return !path.toString().contains(".DS_Store");
        } catch (Exception e) {
            return false;
        }
    }

    private void copy(Path from, Path to) throws IOException {
        if (!Files.exists(to)) {
            Files.createDirectories(to);
        }
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    private String getMonthFolderName(Integer monthNumber, String monthFolderName) {
        monthNumber++;
        return (monthNumber.toString().length() < 2 ? "0" + monthNumber : monthNumber) + "_" + monthFolderName;
    }

    private String resolveMonthPart(int month) {
        return MONTH_FOLDER_NAME_MAP.entrySet()
                .stream()
                .filter(e -> e.getKey() == month)
                .map(e -> getMonthFolderName(e.getKey(), e.getValue()))
                .findFirst()
                .orElse(null);
    }

    private Directory getFileMimeType(File file) throws ImageProcessingException, IOException {
        Metadata metadata = ImageMetadataReader.readMetadata(file);
        return metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
    }

    private Calendar getDateTaken(Path imagePath) {
        try {
            Date dateTaken = getFileMimeType(imagePath.toFile()).getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateTaken);
            log.debug("Got date taken " + dateTaken + " / " + imagePath.getFileName());
            return cal;
        } catch (Exception e) {
            return null;
        }
    }

    private Calendar getCreationDate(Path filePath) {
        try {
            BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
            attr.creationTime().toInstant();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date(attr.creationTime().toInstant().toEpochMilli()));
            log.debug("Got file created time " + attr.creationTime() + " / " + filePath.getFileName());
            return cal;
        } catch (IOException e) {
            return null;
        }
    }

    private void copy(Path to, Integer year, Integer month, Path imagePath) throws IOException {
        String monthFolderName = resolveMonthPart(month);
        Path imageTo = Paths.get(to.toString(), year.toString(), monthFolderName, imagePath.getFileName().toString());
        copy(imagePath, imageTo);
    }

    private boolean hasDateTaken(Path imagePath) {
        return getDateTaken(imagePath) != null;
    }

    private boolean hasCreationTime(Path filePath) {
        return getCreationDate(filePath) != null;
    }

    private int getSucceededSize() {
        return succeeded.size() > 0 ? succeeded.size() : 1;
    }

    private Double countSuccessPercent() {
        int totalCount = succeeded.size() + failed.size();
        return Double.valueOf(100 * (totalCount - failed.size()) / getSucceededSize());
    }

    private void printSummary() {
        try {
            log.info("\n\n##### Image backup completed #####");
            V2_AsciiTable at = new V2_AsciiTable();
            at.addRule();
            at.addRow("Success percent", countSuccessPercent() + " %");
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
            log.info(String.valueOf(rt));
        } catch (Exception e) {
            log.warn("Image backup summary cannot be printed because exception: " + e.getMessage());
        }
    }

    private void resolvePathTo(Path imagePath, Path to) throws IOException {
        Calendar cal;
        if (hasDateTaken(imagePath)) {
            cal = getDateTaken(imagePath);
        } else if (hasCreationTime(imagePath)) {
            log.debug("No date taken found, trying to find file created time...");
            cal = getCreationDate(imagePath);
        } else {
            log.debug("File created time not found");
            throw new NullPointerException("no suitable date found");
        }
        copy(to, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), imagePath);
    }

    private Collection<Path> getImages(Path from) {
        try {
            return Files.walk(Paths.get(from.toString())).filter(path -> isImageOrVideo(path)).collect(Collectors.toSet());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}