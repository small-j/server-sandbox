package test.imagecloud.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import test.imagecloud.domain.FileMetaData;

@Repository
@RequiredArgsConstructor
public class FileMetaDataRepository {

    private final EntityManager em;

    public void save(FileMetaData fileMetaData) { // 무슨 클래스? 인터페이스? 로 받아야할까?
        em.persist(fileMetaData);
    }
}
