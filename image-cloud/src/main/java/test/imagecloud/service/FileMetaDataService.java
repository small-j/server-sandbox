package test.imagecloud.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.imagecloud.domain.FileMetaData;
import test.imagecloud.domain.Member;
import test.imagecloud.domain.StorageManager;
import test.imagecloud.dto.FileDataDto;
import test.imagecloud.dto.FileMetaDataDto;
import test.imagecloud.repository.FileMetaDataRepository;
import test.imagecloud.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class FileMetaDataService {

    private final FileMetaDataRepository fileMetaDataRepository;
    private final MemberRepository memberRepository;
    private final StorageManager storageManager;

    @Transactional
    public String uploadImage(FileMetaDataDto fileMetaDataDto, FileDataDto imageData) {
        Member member = memberRepository.findById(fileMetaDataDto.getMemberId());

        FileMetaData fileMetaData = FileMetaData.createImageMetaData(member, storageManager, fileMetaDataDto, imageData);
        fileMetaDataRepository.save(fileMetaData);

        return fileMetaData.getFilePath();
    }
}
