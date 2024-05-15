package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface AvatarService {
    public void uploadAvatar(Long studentId, MultipartFile avatar) throws IOException;

    List<Avatar> getAllAvatars(Integer pageNumber, Integer sizePage);

    Avatar findAvatar(Long studentId);

    List<Avatar> getAllAvatarStudentPage(Integer numberPage, Integer numberSize);
}
