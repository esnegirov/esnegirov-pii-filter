package com.slice.dao;

import com.slice.models.RegexpTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("regexpRepository")
public interface RegexpTableRepository extends  JpaRepository <RegexpTable, Integer> {
}
