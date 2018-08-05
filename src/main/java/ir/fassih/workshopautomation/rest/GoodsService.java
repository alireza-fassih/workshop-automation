package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/goods")
public class GoodsService extends AbstractRestService<GoodsEntity, Long> {

    @Autowired
    public GoodsService(GoodsManager manager) {
        super(manager);
    }


    private GoodsManager getMyManager() {
        return ( GoodsManager ) manager;
    }


    @PatchMapping("/{id}/addChangeAble")
    public void addChangeAbleMaterial(@PathVariable("id") Long id, @RequestBody ChangeAbleMaterial material) {
        getMyManager().addChangeAbleMaterial(id, material);
    }

    @PatchMapping("/{id}/addStatic")
    public void addChangeAbleMaterial(@PathVariable("id") Long id, @RequestBody StaticMaterial material) {
        getMyManager().addStaticMaterial(id, material);
    }

    @Data
    public static class ChangeAbleMaterial {
        private RawMaterialCategoryEntity category;
        private Float importFactor;
        private String title;
    }


    @Data
    public static class StaticMaterial {
        private RawMaterialEntity material;
        private Float importFactor;
        private String title;
    }

}
