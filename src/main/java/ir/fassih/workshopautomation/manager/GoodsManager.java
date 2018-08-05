package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.repository.AbstractRepository;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import ir.fassih.workshopautomation.rest.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsManager extends AbstractManager<GoodsEntity, Long> {

    @Autowired
    public GoodsManager(GoodsRepository repository) {
        super(repository);
    }

    @Transactional
    public void addChangeAbleMaterial(Long id, GoodsService.ChangeAbleMaterial material) {
        GoodsRawMaterialEntity en = new GoodsRawMaterialEntity();
        en.setTitle(material.getTitle())
                .setSelectAble(true)
                .setCategory(material.getCategory())
                .setImportFactor(material.getImportFactor());
        addRawMaterialToGoods(id, en);
    }

    @Transactional
    public void addStaticMaterial(Long id, GoodsService.StaticMaterial material) {
        GoodsRawMaterialEntity en = new GoodsRawMaterialEntity();
        en.setTitle(material.getTitle())
                .setSelectAble(true)
                .setMaterial(material.getMaterial())
                .setImportFactor(material.getImportFactor());
        addRawMaterialToGoods(id, en);
    }

    private void addRawMaterialToGoods(Long id, GoodsRawMaterialEntity goodsRawMaterialEntity) {
        GoodsEntity entity = repository.findOne(id);
        entity.getRawMaterials()
                .add(goodsRawMaterialEntity);
        repository.save(entity);

    }

}
