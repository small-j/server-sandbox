package server.sandbox.pinterestclone.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageCategory extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_category_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_meta_id")
    private Image image;

    private int categoryId;

    public static ImageCategory create(Image image, int categoryId) {
        ImageCategory imageCategory = ImageCategory.builder()
                .categoryId(categoryId)
                .image(image)
                .build();

        image.addCategory(imageCategory);

        return imageCategory;
    }
}
