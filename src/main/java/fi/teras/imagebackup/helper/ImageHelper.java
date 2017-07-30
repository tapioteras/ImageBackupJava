package fi.teras.imagebackup.helper;

import fi.teras.imagebackup.printout.Summary;
import fi.teras.imagebackup.resolver.Resolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImageHelper {
    private static final Logger log = LoggerFactory.getLogger("fi.teras.imagebackup.helper.ImageHelper");

    public static void backupImages(Path from, Path to) throws IOException {
        List<Path> succeeded = new ArrayList<>();
        List<Path> failed = new ArrayList<>();

        log.info("from: " + from + ", to: " + to + "\n");
        getImages(from).stream().forEach(imagePath -> {
            try {
                log.info("\n\n----- Processing file " + imagePath + "... -----");
                Resolver.resolvePathTo(imagePath, to);
                succeeded.add(imagePath);
            } catch (Exception e) {
                failed.add(imagePath);
            }
        });
        Summary.printSummary(succeeded, failed);
    }

    private static Collection<Path> getImages(Path from) {
        try {
            return Files.walk(Paths.get(from.toString())).filter(path -> FileHelper.isImageOrVideo(path)).collect(Collectors.toSet());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}