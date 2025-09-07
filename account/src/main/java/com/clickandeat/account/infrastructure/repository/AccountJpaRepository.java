package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.infrastructure.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsAccountEntityByPhoneNumber(String phoneNumber);
}
