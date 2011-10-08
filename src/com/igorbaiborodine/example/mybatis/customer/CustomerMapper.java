/*******************************************************************************
 * Copyright 2011 Igor Baiborodine
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.igorbaiborodine.example.mybatis.customer;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

public interface CustomerMapper {
	
	String BASE_COLUMN_LIST = "customer_id, store_id, first_name, last_name" +
			", email, address_id, active, create_date, last_update";
  
	String INSERT = "insert into customer (store_id, first_name"
			+ " ,last_name, email, address_id, active, create_date, last_update)"
			+ " values (#{storeId,jdbcType=TINYINT}, #{firstName,jdbcType=VARCHAR}"
			+ " ,#{lastName,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, #{addressId,jdbcType=SMALLINT}" 
			+ " ,#{active,jdbcType=BIT}, #{createDate,jdbcType=TIMESTAMP}, #{lastUpdate,jdbcType=TIMESTAMP})";

	String SELECT_BY_PRIMARY_KEY = "select " + BASE_COLUMN_LIST 
			+ " from customer where customer_id = #{customerId}";

	String DELETE_BY_PRIMARY_KEY = "delete from customer where customer_id = #{customerId}";

	String UPDATE_BY_PRIMARY_KEY = "update customer set"
			+ " store_id = #{storeId}, first_name = #{firstName},"
			+ " last_name = #{lastName}, email = #{email},"
			+ " address_id = #{addressId}, active = #{active},"
			+ " create_date = #{createDate}, last_update = #{lastUpdate}"
			+ " where customer_id = #{customerId}";
	
	String STOR_PROC_REWARDS_REPORT = "call rewards_report("
			+ "#{min_monthly_purchases,jdbcType=TINYINT,mode=IN},"
			+ "#{min_dollar_amount_purchased,jdbcType=DECIMAL,mode=IN},"
			+ "#{count_rewardees,jdbcType=INTEGER,mode=OUT})";

	@Insert(INSERT)
	@Options(useGeneratedKeys = true, keyProperty = "customer_id", flushCache = true)
	@SelectKey(statement = "SELECT LAST_INSERT_ID();", 
			before = false, 
			keyProperty = "customerId", 
			resultType = Short.class)
	int insert(Customer record_);
	
	@Select(SELECT_BY_PRIMARY_KEY)
	@Results(value = {
		@Result(column="customer_id", property="customerId"),
		@Result(column="store_id", property="storeId"),
		@Result(column="first_name", property="firstName"),
		@Result(column="last_name", property="lastName"),
		@Result(column="email", property="email"),
		@Result(column="address_id", property="addressId"),
		@Result(column="active", property="active"),
		@Result(column="create_date", property="createDate"),
		@Result(column="last_update", property="lastUpdate")
	})	
    Customer selectByPrimaryKey(Short customerId);

	@Update(UPDATE_BY_PRIMARY_KEY)
	@Options(flushCache = true)
    int updateByPrimaryKey(Customer record); 

	@Options(flushCache = true)
    @Delete(DELETE_BY_PRIMARY_KEY)
	int deleteByPrimaryKey(Short customerId);
    
	@Select(STOR_PROC_REWARDS_REPORT)
	@Options(statementType = StatementType.CALLABLE)
	@Results(value = {
		@Result(column="customer_id", property="customerId"),
		@Result(column="store_id", property="storeId"),
		@Result(column="first_name", property="firstName"),
		@Result(column="last_name", property="lastName"),
		@Result(column="email", property="email"),
		@Result(column="address_id", property="addressId"),
		@Result(column="active", property="active"),
		@Result(column="create_date", property="createDate"),
		@Result(column="last_update", property="lastUpdate")
	})	
    List<Customer> getRewardsReport(Map<String, Object> params);
	
}
