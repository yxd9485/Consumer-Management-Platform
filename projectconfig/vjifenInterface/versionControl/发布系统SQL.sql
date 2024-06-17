-- 菜案及权限
insert into sys_function values('400110','活动页面配置','400000','templateUi/showTemplateUiList.do','活动页面配置','2','',null, '1', '0', '200', '0', NOW(), 'admin', NOW(), 'admin');	
update sys_function set SEQUENCE_NUM = '200' where FUNCTION_KEY = '400110';	
insert into sys_role_function_relation values(UUID(),'1', '400110');		
insert into sys_role_function_relation values(UUID(),'2', '400110');	
insert into sys_role_function_relation values(UUID(),'4', '400110');	