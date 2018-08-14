package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.repository.GoodsRawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoodsRawMaterialManager extends AbstractManager<GoodsRawMaterialEntity, Long> {

    @Autowired
    private RawMaterialManager rawMaterialManager;

    @Autowired
    private RawMaterialCategoryManager categoryManager;

    @Autowired
    private GoodsManager goodsManager;

    @Autowired
    public GoodsRawMaterialManager(GoodsRawMaterialRepository repository) {
        super(repository);
    }


    @Override
    public void save(GoodsRawMaterialEntity entity) {
        entity.setSelectAble( entity.getMaterial() == null );
        super.save(entity);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> createOptions() {
        Map<String, Object> map = new HashMap<>();
        map.put("categories", categoryManager.loadNotDeletes());
        map.put("materials", rawMaterialManager.loadNotDeletes());
        map.put("products", goodsManager.loadNotDeletes());
        return map;
    }

}
