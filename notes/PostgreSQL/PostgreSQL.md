





```shell
brew install postgresql

brew services start postgresql


# 命令行登陆 postgres 数据库
$ psql postgres
# 创建新用户
CREATE USER andy;
# 创建新数据库
CREATE DATABASE andy_db WITH OWNER andy;
# 修改密码：andy用户的密码为123456
\password andy
```





#### 常用psql命令

```javascript
# 查看所有用户
\du

# 查看所有数据库
\l

# 切换当前数据库
\c {dbname}

# 查看当前库下所有的表
\dt

# 查看指定表
\d {tablename}

# 查看数据目录
SHOW data_directory;

# 退出psql
\q
```



#### 常用SQL命令

```javascript
# 创建数据库
CREATE DATABASE test;

# 创建表(记得使用\c命令切换数据库)
CREATE TABLE t1(id int,body varchar(100));

# 创建用户
CREATE USER test WITH PASSWORD 'Test#1357';

# 修改密码
ALTER USER test WITH PASSWORD 'Test#2468';

# 指定用户添加指定角色
ALTER USER test createdb;

# 赋予指定账户指定数据库所有权限
GRANT ALL PRIVILEGES ON DATABASE test TO test;

# 移除指定账户指定数据库所有权限
REVOKE ALL PRIVILEGES ON DATABASE test TO test;
```



5432

### 