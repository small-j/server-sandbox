package server.sandbox.pinterestclone.storage;

import server.sandbox.pinterestclone.domain.dto.FileRequest;

public interface StorageManager {
    public String uploadFile(String filename, FileRequest fileRequest);
}
