package ru.polyan.onlinecart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.model.Category;

@Data
@NoArgsConstructor
public class CategoryDto implements Comparable<CategoryDto>{
    private Long id;
    private String title;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.title = category.getTitle();
    }

    @Override
    public int compareTo(CategoryDto o) {
        return this.title.compareTo(o.getTitle());
    }
}
