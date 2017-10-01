# ImageBackupJava
Image backup Powershell script alternative (Java) implementation.

# Run 
Uberjar (contains dependencies) is included to this repo. The jar can be run with following console command:
```
java -jar image-backup-java-1.0-jar-with-dependencies.jar /absolute/path/from /absoulte/path/to
```
# Compile and run
For example that way (command copied from my dev environment - sudo because access policies):
```
mvn clean package && sudo java -jar /home/teraslinux/ImageBackupJava/target/image-backup-java-1.0-jar-with-dependencies.jar /home/teraslinux/OneDrive/cameraRoll /media/teraslinux/daatta/kuvat_backup
```