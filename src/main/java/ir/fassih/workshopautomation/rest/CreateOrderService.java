package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderItemEntity;
import ir.fassih.workshopautomation.manager.GoodsCategoryManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.manager.UserManager;
import ir.fassih.workshopautomation.repository.OrderGoodsRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Secured("USER")
@RestController
@RequestMapping("/rest/createOrder")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CreateOrderService implements RestUtils {

    private GoodsManager goodsManager;
    private ModelMapper mapper;
    private GoodsCategoryManager categoryManager;
    private OrderManager orderManager;
    private UserManager  userManager;



    @GetMapping("/search")
    public Page<GoodsListDto> loadItems(@RequestParam Map<String, String> params) {
        SearchModel searchModel = createSearchModel(params);
        searchModel.getFilters().put("EQ:deleted", "false");
        return goodsManager.search(searchModel).map(source -> mapper.map(source, GoodsListDto.class));
    }

    @PostMapping("/editOrder/{id}")
    public void editOrder(@PathVariable("id") Long orderId, @RequestBody GoodsManager.OrderDto dto) {
        goodsManager.editOrder( orderId, dto );
    }


    @GetMapping("/loadOrder/{id}")
    public LoadOrderDto loadOrder(@PathVariable("id") Long orderId) {
        OrderEntity orderEntity = orderManager.find(orderId);
        LoadOrderDto dto = new LoadOrderDto();
        if(orderEntity != null && orderEntity.isEditable() &&
            userManager.loadCurrentUser().getId().equals(orderEntity.getCreator().getId())) {
            dto.setMetadata( Optional.ofNullable(orderEntity.getItems()).orElseGet(Collections::emptyList)
                .stream().map(OrderGoodsEntity::getGoods).map(GoodsEntity::getId)
                .distinct()
                .map(goodsManager::loadMetadataForCreateOrder)
                .collect(Collectors.toList()) );
            dto.setOrder( orderEntity );
        }
        return dto;
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

    @Data
    private static class LoadOrderDto {
        private List<GoodsManager.ProductMetadata> metadata;
        private OrderEntity order;
    }

}
