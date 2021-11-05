package ru.polyan.onlinecart.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@ApiModel(description = "Параметры запроса для списка товаров")
public class ProductsRequestDTO {

    @ApiModelProperty(value = "Минимальная цена", required = false, example = "15")
    private BigDecimal minprice;
    @ApiModelProperty(value = "Максимальная цена", required = false, example = "40")
    private BigDecimal maxprice;
    @ApiModelProperty(value = "Фильтр по названию товара", required = false, example = "Молоко")
    private String title;
    @ApiModelProperty(value = "ID выбранной категории", required = false, example = "1")
    private Long categoryId;
    @ApiModelProperty(value = "Номер страницы", required = false, example = "1")
    @Min(0)
    private int page;
    @ApiModelProperty(value = "Количество записей на странице", required = false, example = "5")
    @Min(1)
    private int recordsOnPage;

    public ProductsRequestDTO() {
        this.minprice = new BigDecimal(-1);
        this.maxprice = new BigDecimal(-1);
        this.title = "";
        this.page = 0;
        this.recordsOnPage = 5;
        this.categoryId = new Long(-1);
    }
}
