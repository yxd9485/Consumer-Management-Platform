package com.dbt.datasource.util;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.util.RedisApiUtil;

public class DynamicDataSource /*extends AbstractRoutingDataSource*/ {
//	 private Logger log = Logger.getLogger(this.getClass());
//	 private Map<Object, Object> _targetDataSources;
//	 private static final String driverClassName="com.mysql.jdbc.Driver";
//	 private static final String defaultDataSourceName="dataSource";
//	 /**
//		 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#determineCurrentLookupKey()
//		 * @describe 数据源为空或者为0时，自动切换至默认数据源，即在配置文件中定义的dataSource数据源
//		 */
//	@Override
//	protected Object determineCurrentLookupKey() {
//		String dataSourceName = DbContextHolder.getDBType();
//		System.out.println("changeDataSource:"+dataSourceName);
//		if (StringUtils.isBlank(dataSourceName) || "saasDb".equals(dataSourceName)) {
//			dataSourceName = defaultDataSourceName;
//		} else {
//			// 动态更新数据源
//			String key = CacheKey.cacheKey.KEY_UPDATE_PROJECT_SERVER_INFO_LIST;
//			if(RedisApiUtil.getInstance().containsInSet(false, key, dataSourceName)){
//				if(null != _targetDataSources.get(dataSourceName)){
//					_targetDataSources.remove(dataSourceName);
//					RedisApiUtil.getInstance().removeSetValue(false, key, dataSourceName);
//				}
//			}
//
//			this.selectDataSource(dataSourceName);
//		}
//		log.debug("--------> use datasource " + dataSourceName);
//		System.out.println("--------> use datasource " + dataSourceName);
//		return dataSourceName;
//	}
	/**
	 * 到数据库中查找名称为dataSourceName的数据源
	 * 
	 * @param dataSourceName
	 */
//	private void selectDataSource(String dataSourceName) {
//		Object sid = DbContextHolder.getDBType();
//		if (StringUtils.isEmpty(dataSourceName)
//				|| dataSourceName.trim().equals("dataSource")) {
//			DbContextHolder.setDBType("dataSource");
//			return;
//		}
//		Object obj = this._targetDataSources.get(dataSourceName);
//		if (obj != null && sid.equals(dataSourceName)) {
//			return;
//		} else {
//			DataSource dataSource = this.getDataSource(dataSourceName);
//			if (null != dataSource) {
//				this.setDataSource(dataSourceName, dataSource);
//			}
//		}
//	}
//
//	@Override
//	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
//		this._targetDataSources = targetDataSources;
//		super.setTargetDataSources(this._targetDataSources);
//		afterPropertiesSet();
//	}
//
//	private void addTargetDataSource(String key, DataSource dataSource) {
//		this._targetDataSources.put(key, dataSource);
//		this.setTargetDataSources(this._targetDataSources);
//	}
//
//
//	private DataSource createDataSource(ServerInfo serverInfo) {
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setDriverClassName(driverClassName);
//		dataSource.setUrl(serverInfo.getServerJdbc());
//		dataSource.setUsername(serverInfo.getServerU());
//		dataSource.setPassword(serverInfo.getServerP());
//		dataSource.setInitialSize(3);
//		dataSource.setMinIdle(3);
//		dataSource.setMaxActive(serverInfo.getServerC());
//		dataSource.setTestOnBorrow(true);
//		dataSource.setValidationQuery("select 1");
//		System.out.println("create datasource "+ serverInfo.getItemValue());
//		return dataSource;
//	}
//	@SuppressWarnings("unchecked")
//	private DataSource getDataSource(String dataSourceName) {
//		ServerInfo serverInfo = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
//				.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).get(dataSourceName);
//		if(serverInfo!=null) {
//			log.warn(dataSourceName + "最大连接数：" + serverInfo.getServerC());
//			DataSource dataSource = this.createDataSource(serverInfo);
//			return dataSource;
//		}
//		return null;
//	}
//	private DataSource createDataSource(String driverClassName, String url,
//			String username, String password) {
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setDriverClassName(driverClassName);
//		dataSource.setUrl(url);
//		dataSource.setUsername(username);
//		dataSource.setPassword(password);
//		return dataSource;
//	}
//	/**
//	 * 鍒版暟鎹簱涓煡璇㈠悕绉颁负dataSourceName鐨勬暟鎹簮
//	 * 
//	 * @param dataSourceName
//	 * @return
//	 */
//	private DataSource getDataSource(String dataSourceName) {
//		this.selectDataSource(defaultDataSourceName);
//		this.determineCurrentLookupKey();
//		Connection conn = null;
//		try {
//			conn = this.getConnection();
//			StringBuilder builder = new StringBuilder();
//			builder.append("select server_jdbc,server_u,server_p from server_info where item_value=?");
//			
//			PreparedStatement ps = conn.prepareStatement(builder.toString());
//			ps.setString(1, dataSourceName);
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
// 
////				Integer type = rs.getInt("C_TYPE");
////				if (StringUtils.isNotEmpty(type)
////						&& type.intValue() == Constants.DataSourceType.DB
////								.intValue()) {
//				// DB
//				String url = rs.getString(1);
//				String userName = rs.getString(2);
//				String password = rs.getString(3);
////				String driverClassName = rs
////						.getString("C_DRIVER_CLASS_NAME");
//				DataSource dataSource = this.createDataSource(
//						driverClassName, url, userName, password);
//				return dataSource;
////				} else {
////					// JNDI
////					String jndiName = rs.getString("C_JNDI_NAME");
//// 
////					JndiDataSourceLookup jndiLookUp = new JndiDataSourceLookup();
////					DataSource dataSource = jndiLookUp.getDataSource(jndiName);
////					return dataSource;
////				}
// 
//			}
//			rs.close();
//			ps.close();
//		} catch (SQLException e) {
//			log.error(e);
//		} finally {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				log.error(e);
//			}
//		}
//		return null;
//	}
 
	/**
	 * 将已存在的数据源存储到内存中
	 * 
	 * @param dataSourceName
	 * @param dataSource
	 */
//	private void setDataSource(String dataSourceName, DataSource dataSource) {
//		this.addTargetDataSource(dataSourceName, dataSource);
//		DbContextHolder.setDBType(dataSourceName);
//	}
}
