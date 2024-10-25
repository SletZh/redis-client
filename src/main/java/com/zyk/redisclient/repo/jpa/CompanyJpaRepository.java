package com.zyk.redisclient.repo.jpa;

import com.zyk.redisclient.bean.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyJpaRepository extends JpaRepository<Company,Long> {

}
