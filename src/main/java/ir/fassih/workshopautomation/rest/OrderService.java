package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.manager.GoodsCategoryManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/createOrder")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService implements RestUtils {

    private GoodsManager goodsManager;
    private ModelMapper mapper;
    private GoodsCategoryManager categoryManager;

    @GetMapping("/search")
    public Page<GoodsDto> loadItems(@RequestParam Map<String, String> params) {
        SearchModel searchModel = createSearchModel(params);
        searchModel.getFilters().put("EQ:deleted", "false");
        return goodsManager.search(searchModel).map(source -> mapper.map(source, GoodsDto.class));
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
    public List<GoodsRawMaterialDto> loadGoodsMetadata(@PathVariable("id") Long id) {
        return goodsManager.loadGoodsMetadata(id).stream().filter( GoodsRawMaterialEntity::isSelectAble )
                .map( m -> mapper.map( m , GoodsRawMaterialDto.class ) )
                .collect( Collectors.toList()) ;
    }


    @Data
    public static class GoodsRawMaterialDto {
        private String id;
        private String title;
        private Long categoryId;
    }

    @Data
    public static class GoodsDto {
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
