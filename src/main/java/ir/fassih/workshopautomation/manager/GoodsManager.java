package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderItemEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GoodsManager extends AbstractManager<GoodsEntity, Long> {

    private RawMaterialManager rawMaterialManager;
    private UserManager userManager;
    private OrderManager orderManager;
    private GoodsCategoryManager categoryManager;


    @Autowired
    public GoodsManager(GoodsRepository repository, RawMaterialManager rawMaterialManager,
                        UserManager userManager, OrderManager orderManager,
                        GoodsCategoryManager categoryManager) {
        super(repository);
        this.rawMaterialManager = rawMaterialManager;
        this.userManager = userManager;
        this.orderManager = orderManager;
        this.categoryManager = categoryManager;
    }

    @Transactional
    public void submitOrder(Long id, List<OrderItemEntity> orders, Principal principal) {
        OrderEntity orderEntity = calculatePrice(id, orders);
        if( principal != null ) {
            UserEntity userEntity = userManager.loadByUsername(principal.getName());
            orderEntity.setCreator(userEntity);
        }
        orderManager.save( orderEntity );
    }

    @Transactional(readOnly = true)
    public OrderEntity calculatePrice(Long id, List<OrderItemEntity> orders) {
        OrderEntity orderEntity = new OrderEntity();
        List<OrderItemEntity> orderItems = new ArrayList<>();
        GoodsEntity entity = find(id);
        orderEntity.setTitle(entity.getTitle());
        orderEntity.setGoods(entity);
        Map<Boolean, List<GoodsRawMaterialEntity>> items =
                entity.getRawMaterials().stream().collect(Collectors.groupingBy(GoodsRawMaterialEntity::isSelectAble));

        Function<GoodsRawMaterialEntity, OrderItemEntity> convertor = en -> {
            OrderItemEntity item = new OrderItemEntity();
            item.setTitle(en.getTitle());
            item.setOrder(orderEntity);
            item.setMetadata(en);
            item.setMaterial(en.getMaterial());
            item.setPrice(Float.valueOf(en.getImportFactor() * en.getMaterial().getUnitPrice()).longValue());
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
            item.setTitle(goodsRawMaterialEntity.getTitle() + ":( " + rawMaterialEntity.getTitle() + " )");
            item.setOrder(orderEntity);
            item.setMetadata(goodsRawMaterialEntity);
            item.setMaterial(rawMaterialEntity);
            item.setPrice(Float.valueOf(goodsRawMaterialEntity.getImportFactor() * rawMaterialEntity.getUnitPrice()).longValue());
            orderItems.add(item);
        }
        orderEntity.setItems(orderItems);
        return orderEntity;
    }

    @Transactional(readOnly = true)
    public List<GoodsCategoryEntity> loadAllCategories() {
        return categoryManager.loadNotDeletes();
    }
}
