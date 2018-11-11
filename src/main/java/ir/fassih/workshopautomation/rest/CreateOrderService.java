package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderItemEntity;
import ir.fassih.workshopautomation.manager.GoodsCategoryManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import ir.fassih.workshopautomation.manager.OrderManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Secured("USER")
@RestController
@RequestMapping("/rest/createOrder")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CreateOrderService implements RestUtils {

    private GoodsManager goodsManager;
    private ModelMapper mapper;
    private GoodsCategoryManager categoryManager;

    @GetMapping("/search")
    public Page<GoodsListDto> loadItems(@RequestParam Map<String, String> params) {
        SearchModel searchModel = createSearchModel(params);
        searchModel.getFilters().put("EQ:deleted", "false");
        return goodsManager.search(searchModel).map(source -> mapper.map(source, GoodsListDto.class));
    }


    @GetMapping("/options")
    public Map<String, Object> loadOptions() {
        Map<String, Object> items = new HashMap<>();
        items.put("categories",
                categoryManager.search(createSearchModel(new HashMap<>()))
                        .map(source -> mapper.map(source, CategoryDto.class)).getContent());
        return items;
    }

    @GetMapping("/{id}")
    public GoodsManager.ProductMetadata loadGoodsMetadata(@PathVariable("id") Long id) {
        return goodsManager.loadMetadataForCreateOrder(id);
    }

    @PostMapping("/calculatePrice")
    public OrderEntity calculatePrice(@RequestBody GoodsManager.OrderDto dto) {
        return goodsManager.calculatePrice(dto);
    }


    @PostMapping("/submitOrder")
    public void submitOrder(@RequestBody GoodsManager.OrderDto dto, Principal principal) {
        goodsManager.submitOrder(dto, principal);
    }


    @Data
    public static class GoodsListDto {
        private Long id;
        private String title;
        private String categoryTitle;
    }

    @Data
    public static class CategoryDto {
        private Long id;
        private String title;
    }

}
