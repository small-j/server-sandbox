package server.sandbox.pinterestclone.storage;

import server.sandbox.pinterestclone.domain.dto.FileRequest;

import java.io.IOException;

public interface StorageManager {
    public String uploadFile(String filename, FileRequest fileRequest) throws IOException;
    public void deleteFile(String filename);
}
