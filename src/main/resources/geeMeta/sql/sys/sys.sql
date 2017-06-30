/**
* 格式说明：每条语句之间必须用注释“@sql ”进行分割，@sql后跟sqlId
*/

-- 创建表
-- @sql sys_user_select_all
SELECT * from sys_user where 1=1
 @if $.name==NULL || $.name==""
  and name=`$.name`
 @/if

-- @sql findPermissionsByLoginName
SELECT * from sys_psermission where loginName=`$.loginName`