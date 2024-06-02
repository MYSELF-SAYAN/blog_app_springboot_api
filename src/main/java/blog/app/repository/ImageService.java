package blog.app.repository;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageService {
    public Map upload(MultipartFile file);
}
