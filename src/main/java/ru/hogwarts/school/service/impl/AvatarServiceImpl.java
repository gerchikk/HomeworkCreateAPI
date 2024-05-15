package ru.hogwarts.school.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.NotFoundStudentException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.datical.liquibase.ext.init.InitProjectUtil.getExtension;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {

    private final String avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarServiceImpl(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.avatarsDir = avatarsDir;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("method uploadAvatar is run");
        Student student = studentRepository.getById(studentId);
        // строчка ниже работает для MacOs.
        //Path filePath = Path.of(new File("").getAbsolutePath() + avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Path filePath = Path.of(avatarsDir, studentId + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateImagePreview(filePath));

        avatarRepository.save(avatar);
    }

    private String getExtensions(String fileName) {
        logger.info("Was invoked method for get file extension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private byte[] generateImagePreview(Path filePath)throws IOException {
        logger.info("The method for saving a small copy of the avatar is called");
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is,1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight()/(image.getWidth()/100);
            BufferedImage preview = new BufferedImage(100,height,image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image,0,0,100,height,null);
            graphics2D.dispose();

            ImageIO.write(preview,getExtensions(filePath.getFileName().toString()),baos);
            return baos.toByteArray();
        }
    }

    @Override
    public List<Avatar> getAllAvatars(Integer pageNumber, Integer sizePage) {
        logger.info("Was invoked method for displaying avatars by page");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, sizePage);
        return avatarRepository.findAll(pageRequest).getContent();
    }
    public Avatar findAvatar(Long studentId) {
        logger.info("Called method for getting avatar");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }
    public List<Avatar> getAllAvatarStudentPage(Integer numberPage, Integer numberSize) {
        logger.info("The method for displaying avatars by page is called");
        PageRequest pageRequest = PageRequest.of(numberPage - 1, numberSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}