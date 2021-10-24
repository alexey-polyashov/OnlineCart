package ru.polyan.onlinecart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.polyan.onlinecart.model.Category;

@Data
@NoArgsConstructor
@ApiModel(description = "Сущность 'Категория'")
public class CategoryDto implements Comparable<CategoryDto>{
    @ApiModelProperty(value = "ID")
    private Long id;
    @ApiModelProperty(value = "Наименование")
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
