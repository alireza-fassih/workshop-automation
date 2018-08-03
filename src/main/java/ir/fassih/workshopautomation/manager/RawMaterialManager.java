package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.repository.AbstractRepository;
import ir.fassih.workshopautomation.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawMaterialManager extends AbstractManager<RawMaterialEntity, Long> {

    @Autowired
    public RawMaterialManager(RawMaterialRepository repository) {
        super(repository);
    }

}
