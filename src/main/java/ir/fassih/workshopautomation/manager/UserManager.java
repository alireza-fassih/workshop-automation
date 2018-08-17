package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserManager extends AbstractManager<UserEntity, Long> implements UserDetailsService {

    private UserRepository getMyRepository() {
        return (UserRepository) repository;
    }

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserManager(UserRepository repository, BCryptPasswordEncoder encoder) {
        super(repository, UserEntity.class);
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public UserEntity loadByUsername(String username) {
        return getMyRepository().findByUsername(username);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = loadByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return user;
    }

    @Override
    @Transactional
    public void save(UserEntity entity) {
        if (StringUtils.hasText(entity.getNewPassword())) {
            entity.setPassword(encoder.encode(entity.getNewPassword()));
        }
        super.save(entity);
    }

    @Transactional
    public void setEnable(Long id, boolean enable) {
        UserEntity user = repository.findOne(id);
        UserEntity.UserInfo userInfo = Optional.ofNullable(user.getInfo()).orElse(new UserEntity.UserInfo());
        userInfo.setEnabled( enable );
        user.setInfo( userInfo );
        repository.save(user);
    }

    @Override
    protected String[] ignoreFieldWhenUpdate() {
        return new String[] { "password" , "info" };
    }
}
