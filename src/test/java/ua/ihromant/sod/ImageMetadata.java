package ua.ihromant.sod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageMetadata {
    private String name;
    private int imagesCount;
    private int imageWidth;
    private int imageHeight;
}
