package com.ae.community.service;

import com.ae.community.aws.S3Uploader;
import com.ae.community.domain.Images;
import com.ae.community.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImagesService {

    private final S3Uploader s3Uploader;
    private final ImagesRepository imagesRepository;

    public Images save(Images image) {return  imagesRepository.save(image); }

    public Images create(Long postIdx, String imgUrl, int imgRank) {
        Images images = new Images();
        images.setPostIdx(postIdx);
        images.setImgUrl(imgUrl);
        images.setImgRank(imgRank);
        return images;
    }


    public void deleteByPostIdx(Long postIdx) {
        imagesRepository.deleteByPostIdx(postIdx);
    }

    public Optional<Images> findByIdx(Long imageIdx) { return imagesRepository.findById(imageIdx);
    }

    public void deleteImage(Long imageIdx) { imagesRepository.deleteById(imageIdx);    }

    public List<Images> findByPostIdx(Long postIdx) {
        return imagesRepository.findByPostIdx(postIdx);
    }

    public Long getImagesCnt(Long postIdx) {
        return imagesRepository.countByPostIdx(postIdx);
    }


    public void uploadImages(Long postIdx, List<MultipartFile> multipartFileList) throws IOException {
        int img_rank = 1;

        for(int i = 0; i < multipartFileList.size(); i++) {
            MultipartFile multipartFile = multipartFileList.get(i);
            String img_url = "empty";
            if(multipartFile != null) {
                if(!multipartFile.isEmpty()) {
                    img_url = s3Uploader.upload(multipartFile, "static");
                    Images images = create(postIdx, img_url, img_rank);
                    save(images);
                    img_rank++;
                }
            }
        }
    }
}
