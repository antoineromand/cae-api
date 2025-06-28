package com.pickandeat.authentication.infrastructure.repository;

import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.infrastructure.model.CredentialsEntity;
import com.pickandeat.authentication.infrastructure.model.RoleEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CredentialsRepositoryImpl implements ICredentialsRepository {

    private final CredentialsEntityJPARepository iCredentialsJPARespository;
    private final RoleEntityJPARepository roleEntityJPARepository;

    public CredentialsRepositoryImpl(
            CredentialsEntityJPARepository credentialsRepository,
            RoleEntityJPARepository roleRepository) {
        this.iCredentialsJPARespository = credentialsRepository;
        this.roleEntityJPARepository = roleRepository;
    }

    @Override
    public Optional<Credentials> findByEmail(String email) {
        Optional<CredentialsEntity> entityModel = this.iCredentialsJPARespository.findByEmail(email);

        return entityModel.map(CredentialsEntity::toDomain);
    }

    @Override
    public UUID save(Credentials credentials) {
        RoleEntity roleEntity =
                this.roleEntityJPARepository
                        .findByName(credentials.getRole().name().toString())
                        .orElseThrow(() -> new RuntimeException("placeholder: role pas bon"));
        var credentialsEntity = CredentialsEntity.fromDomain(credentials, roleEntity);
        CredentialsEntity savedCredentials = this.iCredentialsJPARespository.save(credentialsEntity);
        return savedCredentials.getId();
    }

    @Override
    public Optional<Credentials> findByUserId(String userId) {
        Optional<CredentialsEntity> entityModel =
                this.iCredentialsJPARespository.findById(UUID.fromString(userId));

        return entityModel.map(CredentialsEntity::toDomain);
    }
}
