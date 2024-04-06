package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.sandbox.pinterestclone.domain.Category;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private int id;
    private String name;

    public static CategoryResponse of(Category entity) {
        return new CategoryResponse(entity.getId(), entity.getName());
    }
}
