package com.tai.paysafe.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class UserRepositoryCustomImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    public  List<UserSummaryModel>  getUserSummaryModelByUsername(String username) {
        String queryString = "select  u.username , ub.account_name  from users u " +
                "left join user_bank ub  " +
                "on ub.user_id  = u.id " +
                "WHERE u.username = ?";
        //jdbcTemplate.queryForObject
        return jdbcTemplate.query(queryString,
                BeanPropertyRowMapper.newInstance(UserSummaryModel.class), new Object[]{username} );
    }
}
