package com.mourad.digitalBanking.repositories;

import com.mourad.digitalBanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
