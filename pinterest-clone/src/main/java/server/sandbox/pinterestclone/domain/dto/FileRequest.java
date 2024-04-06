package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileRequest {
    private InputStream inputStream;
    private String contentType;
    private long contentLength;
}
