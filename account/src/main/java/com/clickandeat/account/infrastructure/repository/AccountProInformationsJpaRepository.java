package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.infrastructure.model.AccountProInformationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountProInformationsJpaRepository
    extends JpaRepository<AccountProInformationsEntity, Long> {}
