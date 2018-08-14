package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import ir.fassih.workshopautomation.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RawMaterialManager extends AbstractManager<RawMaterialEntity, Long> {

    private RawMaterialCategoryManager rawMaterialCategoryManager;

    @Autowired
    public RawMaterialManager(RawMaterialRepository repository,
                              RawMaterialCategoryManager rawMaterialCategoryManager) {
        super(repository);
        this.rawMaterialCategoryManager = rawMaterialCategoryManager;
    }

    @Transactional(readOnly = true)
    public List<RawMaterialEntity> findByCategory(Long category) {
        return repository.findAll((root, query, cb) ->
            cb.and(
                cb.equal(root.get("category").get("id"), category),
                cb.or(cb.notEqual(root.get("deleted"), Boolean.TRUE), cb.isNull(root.get("deleted")))
            )
        );
    }

    @Transactional(readOnly = true)
    public List<RawMaterialCategoryEntity> loadAllCategories() {
        return rawMaterialCategoryManager.loadNotDeletes();
    }
}
