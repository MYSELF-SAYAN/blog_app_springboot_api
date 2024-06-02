package blog.app.service;

import blog.app.repository.ImageService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Service
public class ImageUploadImpl implements ImageService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file) {
        //upload image
        try {
           Map data= this.cloudinary.uploader().upload(file.getBytes(), Map.of());
           return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
