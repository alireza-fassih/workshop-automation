package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderItemEntity;
import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import ir.fassih.workshopautomation.repository.OrderGoodsRepository;
import lombok.Data;
import org.hibernate.loader.plan.build.internal.FetchGraphLoadPlanBuildingStrategy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GoodsManager extends AbstractManager<GoodsEntity, Long> {


    @Autowired
    private ModelMapper mapper;

    private RawMaterialManager rawMaterialManager;
    private UserManager userManager;
    private OrderManager orderManager;
    private GoodsCategoryManager categoryManager;

    @Autowired
    private OrderGoodsRepository orderGoodsRepository;

    @Autowired
    private OrderStateManager stateManager;


    @Autowired
    public GoodsManager(GoodsRepository repository, RawMaterialManager rawMaterialManager,
                        UserManager userManager, OrderManager orderManager,
                        GoodsCategoryManager categoryManager) {
        super(repository, GoodsEntity.class);
        this.rawMaterialManager = rawMaterialManager;
        this.userManager = userManager;
        this.orderManager = orderManager;
        this.categoryManager = categoryManager;
    }

    @Transactional
    public void submitOrder(OrderDto order, Principal principal) {
        OrderEntity entity = new OrderEntity();

        entity.setDescription(order.getDescription());
        StateOfOrderEntity state = new StateOfOrderEntity();
        state.setOrder(entity);
        state.setCreateDate(new Date());
        state.setState(stateManager.loadFirstStates());
        entity.putToState(state);

        List<OrderGoodsEntity> collect = order.getItems().stream()
                .map(item -> this.calculatePriceInternal(item.getGoods().getId(), item.getCount(), item.getItems()))
                .collect(Collectors.toList());
        collect.forEach( item -> item.setOrder(entity));
        entity.setItems(collect);
        if (principal != null) {
            UserEntity userEntity = userManager.loadByUsername(principal.getName());
            entity.setCreator(userEntity);
        }
        orderManager.save(entity);
    }


    @Transactional(readOnly = true)
    public OrderEntity calculatePrice(OrderDto order) {
        OrderEntity entity = new OrderEntity();
        List<OrderGoodsEntity> collect = order.getItems().stream()
                .map(item -> this.calculatePriceInternal(item.getGoods().getId(), item.getCount(), item.getItems()))
                .collect(Collectors.toList());
        collect.forEach( item -> item.setOrder(entity));
        entity.setItems(collect);
        return entity;
    }

    private OrderGoodsEntity calculatePriceInternal(Long id, Long count, Collection<OrderItemEntity> orders) {
        UserEntity currentUser = userManager.loadCurrentUser();
        OrderGoodsEntity orderEntity = new OrderGoodsEntity();
        orderEntity.setCount(count);
        List<OrderItemEntity> orderItems = new ArrayList<>();
        GoodsEntity entity = find(id);
        orderEntity.setGoods(entity);
        Map<Boolean, List<GoodsRawMaterialEntity>> items =
                entity.getRawMaterials().stream().filter(e -> !Boolean.TRUE.equals(e.getDeleted()))
                        .collect(Collectors.groupingBy(GoodsRawMaterialEntity::isSelectAble));

        Function<GoodsRawMaterialEntity, OrderItemEntity> convertor = en -> {
            OrderItemEntity item = new OrderItemEntity();
            item.setTitle(en.getTitle());
            item.setOrder(orderEntity);
            item.setMetadata(en);
            item.setMaterial(en.getMaterial());
            item.setPrice(this.calculatePriseBasedOnUser(en.getImportFactor(), en.getMaterial().getUnitPrice(), currentUser));
            return item;
        };
        orderItems.addAll(Optional.ofNullable(items.get(Boolean.FALSE)).orElse(new ArrayList<>())
                .stream().map(convertor).collect(Collectors.toList()));
        List<GoodsRawMaterialEntity> selectAbleItem =
                Optional.ofNullable(items.get(Boolean.TRUE)).orElse(new ArrayList<>());
        if (selectAbleItem.size() != orders.size()) {
            throw new IllegalStateException();
        }

        for (OrderItemEntity order : orders) {
            Long metadataId = order.getMetadata().getId();
            Optional<GoodsRawMaterialEntity> metadata =
                    selectAbleItem.stream().filter(en -> en.getId().equals(metadataId)).findFirst();
            if (!metadata.isPresent()) {
                throw new IllegalStateException();
            }
            GoodsRawMaterialEntity goodsRawMaterialEntity = metadata.get();
            RawMaterialEntity rawMaterialEntity = rawMaterialManager.find(order.getMaterial().getId());

            if (!goodsRawMaterialEntity.getCategory().getId().equals(rawMaterialEntity.getCategory().getId())) {
                throw new IllegalStateException();
            }
            OrderItemEntity item = new OrderItemEntity();
            item.setTitle(goodsRawMaterialEntity.getTitle());
            item.setOrder(orderEntity);
            item.setMetadata(goodsRawMaterialEntity);
            item.setMaterial(rawMaterialEntity);
            item.setPrice(this.calculatePriseBasedOnUser(goodsRawMaterialEntity.getImportFactor(), rawMaterialEntity.getUnitPrice() , currentUser));
            orderItems.add(item);
        }
        orderEntity.setItems(orderItems);
        return orderEntity;
    }

    private long calculatePriseBasedOnUser(float importFactor, long price, UserEntity user) {
        Float prisePercentage = Optional.ofNullable(user.getPrisePercentage()).orElse(Float.valueOf(1F));
        return Float.valueOf((importFactor * price) * prisePercentage).longValue();
    }

    @Transactional(readOnly = true)
    public ProductMetadata loadMetadataForCreateOrder(Long id) {
        GoodsEntity product = repository.findOne(id);
        ProductMetadata productMetadata = mapper.map(product, ProductMetadata.class);
        productMetadata.setMetadata(product.getRawMaterials().stream()
                .filter(e -> !Boolean.TRUE.equals(e.getDeleted()))
                .filter(GoodsRawMaterialEntity::isSelectAble)
                .map(gr -> mapper.map(gr, MaterialMetadata.class)).collect(Collectors.toList()));
        for (MaterialMetadata m : productMetadata.getMetadata()) {
            m.setValues(
                    rawMaterialManager.findByCategory(m.getCategoryId())
                            .stream().map(r -> mapper.map(r, MaterialValue.class))
                            .collect(Collectors.toList()));
        }
        return productMetadata;
    }

    @Transactional
    public void editOrder(Long orderId, OrderDto dto) {
        OrderEntity orderEntity = orderManager.find(orderId);
        if(orderEntity != null && orderEntity.isEditable() &&
                userManager.loadCurrentUser().getId().equals(orderEntity.getCreator().getId())) {
            orderGoodsRepository.deleteByOrder(orderEntity);
            Collection<OrderGoodsEntity> newItems = calculatePrice(dto).getItems();
            newItems.forEach( it -> it.setOrder( orderEntity ) );
            orderEntity.setItems(newItems);
            orderEntity.setDescription(dto.getDescription());
            orderManager.save(orderEntity);
        }
    }

    @Data
    public static class ProductMetadata {
        private Long id;
        private String title;
        private List<MaterialMetadata> metadata;
    }

    @Data
    public static class MaterialMetadata {
        private Long id;
        private String title;
        private Long categoryId;
        private List<MaterialValue> values;
    }

    @Data
    public static class MaterialValue {
        private Long id;
        private String title;
    }


    @Data
    public static class OrderDto {
        private List<OrderGoodsEntity> items;
        private String description;
    }
}
