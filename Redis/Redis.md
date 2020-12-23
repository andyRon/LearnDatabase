

## Redis入门学习

[Redis教程](https://www.w3cschool.cn/redis/)



### Redis简介

Redis 与其他 key - value 缓存产品相比的三个特点：

- Redis支持数据的**持久化**，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用。
- Redis不仅仅支持简单的key-value类型的数据，同时还提供**list，set，zset，hash**等数据结构的存储。
- Redis支持数据的**备份**，即master-slave模式的数据备份。

Redis 优势

- 性能极高 – Redis能读的速度是110000次/s,写的速度是81000次/s 。
- 丰富的数据类型 – Redis支持二进制案例的 Strings, Lists, Hashes, Sets 及 Ordered Sets 数据类型操作。
- 原子 – Redis的所有操作都是原子性的，同时Redis还支持对几个操作全并后的原子性执行。
- 丰富的特性 – Redis还支持 publish/subscribe, 通知, key 过期等等特性。

### 安装

macos

`brew install redis`

启动 Redis `redis-server`

打开新的命令行，启动客服端：

```bash
$ redis-cli
127.0.0.1:6379>
```

在用户命令下会自动建一个隐藏文件（`~/.rediscli_history`），用来保存`redis-cli`命令使用记录。

### 配置

配置文件所在`/usr/local/etc/redis.conf`

也可以在命令行中通过`config`命令获取或配置， 

获取使用格式：`redis 127.0.0.1:6379> CONFIG GET CONFIG_SETTING_NAME`(大小写无所谓)，

配置使用格式：`redis 127.0.0.1:6379> CONFIG SET CONFIG_SETTING_NAME NEW_CONFIG_VALUE`。

实例：

```bash
redis 127.0.0.1:6379> CONFIG GET loglevel

1) "loglevel"
2) "notice"
```

注意显示方式，先配置项名，然后紧接着配置项值。

`config get *`就是所得所有配置项。

```bash
redis 127.0.0.1:6379> CONFIG SET loglevel "notice"
OK
redis 127.0.0.1:6379> CONFIG GET loglevel

1) "loglevel"
2) "notice"
```



#### 参数说明

redis.conf 配置项说明如下：

1. Redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程

  **daemonize no**

2. 当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定

  **pidfile /var/run/redis.pid**

3. 指定Redis监听端口，默认端口为6379，作者在自己的一篇博文中解释了为什么选用6379作为默认端口，因为6379在手机按键上MERZ对应的号码，而MERZ取自意大利歌女Alessia Merz的名字

  **port 6379**

4. 绑定的主机地址

  **bind 127.0.0.1**

5.当 客户端闲置多长时间后关闭连接，如果指定为0，表示关闭该功能

  **timeout 300**

6. 指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为verbose

  **loglevel verbose**

7. 日志记录方式，默认为标准输出，如果配置Redis为守护进程方式运行，而这里又配置为日志记录方式为标准输出，则日志将会发送给/dev/null

  **logfile stdout**

8. 设置数据库的数量，默认数据库为0，可以使用SELECT <dbid>命令在连接上指定数据库id

  **databases 16**

9. 指定在多长时间内，有多少次更新操作，就将数据同步到数据文件，可以多个条件配合

  **save  **

  Redis默认配置文件中提供了三个条件：

  **save 900 1**

  **save 300 10**

  **save 60 10000**

  分别表示900秒（15分钟）内有1个更改，300秒（5分钟）内有10个更改以及60秒内有10000个更改。

 

10. 指定存储至本地数据库时是否压缩数据，默认为yes，Redis采用LZF压缩，如果为了节省CPU时间，可以关闭该选项，但会导致数据库文件变的巨大

  **rdbcompression yes**

11. 指定本地数据库文件名，默认值为dump.rdb

  **dbfilename dump.rdb**

12. 指定本地数据库存放目录

  **dir ./**

13. 设置当本机为slav服务时，设置master服务的IP地址及端口，在Redis启动时，它会自动从master进行数据同步

  **slaveof  **

14. 当master服务设置了密码保护时，slav服务连接master的密码

  **masterauth **

15. 设置Redis连接密码，如果配置了连接密码，客户端在连接Redis时需要通过AUTH <password>命令提供密码，默认关闭

  **requirepass foobared**

16. 设置同一时间最大客户端连接数，默认无限制，Redis可以同时打开的客户端连接数为Redis进程可以打开的最大文件描述符数，如果设置 maxclients 0，表示不作限制。当客户端连接数到达限制时，Redis会关闭新的连接并向客户端返回max number of clients reached错误信息

  **maxclients 128**

17. 指定Redis最大内存限制，Redis在启动时会把数据加载到内存中，达到最大内存后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理 后，仍然到达最大内存设置，将无法再进行写入操作，但仍然可以进行读取操作。Redis新的vm机制，会把Key存放内存，Value会存放在swap区

  **maxmemory **

18. 指定是否在每次更新操作后进行日志记录，Redis在默认情况下是异步的把数据写入磁盘，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为 redis本身同步数据文件是按上面save条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认为no

  **appendonly no**

19. 指定更新日志文件名，默认为appendonly.aof

   **appendfilename appendonly.aof**

20. 指定更新日志条件，共有3个可选值：
  **no**：表示等操作系统进行数据缓存同步到磁盘（快）
  **always**：表示每次更新操作后手动调用fsync()将数据写到磁盘（慢，安全）
  **everysec**：表示每秒同步一次（折衷，默认值）

  **appendfsync everysec**

 

21. 指定是否启用虚拟内存机制，默认值为no，简单的介绍一下，VM机制将数据分页存放，由Redis将访问量较少的页即冷数据swap到磁盘上，访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）

   **vm-enabled no**

22. 虚拟内存文件路径，默认值为/tmp/redis.swap，不可多个Redis实例共享

   **vm-swap-file /tmp/redis.swap**

23. 将所有大于vm-max-memory的数据存入虚拟内存,无论vm-max-memory设置多小,所有索引数据都是内存存储的(Redis的索引数据 就是keys),也就是说,当vm-max-memory设置为0的时候,其实是所有value都存在于磁盘。默认值为0

   **vm-max-memory 0**

24. Redis swap文件分成了很多的page，一个对象可以保存在多个page上面，但一个page上不能被多个对象共享，vm-page-size是要根据存储的 数据大小来设定的，作者建议如果存储很多小对象，page大小最好设置为32或者64bytes；如果存储很大大对象，则可以使用更大的page，如果不 确定，就使用默认值

   **vm-page-size 32**

25. 设置swap文件中的page数量，由于页表（一种表示页面空闲或使用的bitmap）是在放在内存中的，，在磁盘上每8个pages将消耗1byte的内存。

   **vm-pages 134217728**

26. 设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的，可能会造成比较长时间的延迟。默认值为4

   **vm-max-threads 4**

27. 设置在向客户端应答时，是否把较小的包合并为一个包发送，默认为开启

  **glueoutputbuf yes**

28. 指定在超过一定的数量或者最大的元素超过某一临界值时，采用一种特殊的哈希算法

  **hash-max-zipmap-entries 64**

  **hash-max-zipmap-value 512**

29. 指定是否激活重置哈希，默认为开启（后面在介绍Redis的哈希算法时具体介绍）

  **activerehashing yes**

30. 指定包含其它的配置文件，可以在同一主机上多个Redis实例之间使用同一份配置文件，而同时各个实例又拥有自己的特定配置文件

  **include /path/to/local.conf**



### 数据类型

五中数据类型

#### String（字符串）

string是redis最基本的类型，一个key对应一个value，二进制安全的，就是是string可以包含任何数据。比如jpg图片或者序列化的对象 。一个键最大能存储512MB。

```bash
127.0.0.1:6379> SET name "andyron"
OK
127.0.0.1:6379> GET name
"andyron"
```

命令：`SET`  `GET`

#### Hash（哈希）

Redis hash 是一个键值对集合。每个 hash 可以存储 232 - 1 键值对（40多亿）。

```bash
127.0.0.1:6379> HMSET runoobkey name "redis tutorial" description "redis basic commands for caching" likes 20 visitors 23000
OK
127.0.0.1:6379> HGETALL runoobkey
1) "name"
2) "redis tutorial"
3) "description"
4) "redis basic commands for caching"
5) "likes"
6) "20"
7) "visitors"
8) "23000"
```

#### List（列表）

Redis 列表是简单的字符串列表，按照插入顺序排序。可以添加一个元素到列表的头部（左边）或者尾部（右边）。

```bash
127.0.0.1:6379> lpush andyron redis
(integer) 1
127.0.0.1:6379> lpush andyron java
(integer) 2
127.0.0.1:6379> lpush andyron css
(integer) 3
127.0.0.1:6379> lpush andyron mongodb
(integer) 4
127.0.0.1:6379> lrange andyron 0 10
1) "mongodb"
2) "css"
3) "java"
4) "redis"
```

列表最多可存储 232 - 1 元素 (4294967295, 每个列表可存储40多亿)。

#### Set（集合）

Redis的Set是string类型的**无序不重复**集合。

集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。

```bash
127.0.0.1:6379> sadd Mr.rong php
(integer) 1
127.0.0.1:6379> sadd Mr.rong Swift
(integer) 1
127.0.0.1:6379> sadd Mr.rong C
(integer) 1
127.0.0.1:6379> sadd Mr.rong C
(integer) 0
127.0.0.1:6379> smembers Mr.rong
1) "C"
2) "Swift"
3) "php"
```

集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。



#### zset(sorted set：有序集合)

类似于Set，不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。

`zadd`命令的使用格式：`zadd key score member `。

```bash
127.0.0.1:6379> zadd superman 10 java
(integer) 1
127.0.0.1:6379> zadd superman 8 swift
(integer) 1
127.0.0.1:6379> zadd superman 7 python
(integer) 1
127.0.0.1:6379> zadd superman 5 c
(integer) 1
127.0.0.1:6379> zadd superman 1 c++
(integer) 1
127.0.0.1:6379> zrangebyscore superman 4 10
1) "c"
2) "python"
3) "swift"
4) "java"
```

### Redis 数据备份与恢复

命令`save`在redis启动时的目录下不存一个`dump.rdb`文件。

```bash
127.0.0.1:6379> SAVE 
OK
```

如果需要**恢复数据**，只需将备份文件 (dump.rdb) 移动到 redis要启动的目录，并启动服务即可。

想要知道当前启动的目录可以使用`config get`命令：

```bash
127.0.0.1:6379> config get dir
1) "dir"
2) "/Users/andyron"
```

另外`bgsave`命令就是把保存操作在后台进行。

### Redis 安全



### Redis 性能测试

`redis-benchmark [option] [option value]` 这个命令不是redis-cli下的命令。

```bash
$ redis-benchmark -n 100000

====== PING_INLINE ======
  100000 requests completed in 1.33 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

98.31% <= 1 milliseconds
99.78% <= 2 milliseconds
99.93% <= 3 milliseconds
99.95% <= 4 milliseconds
99.95% <= 5 milliseconds
99.97% <= 6 milliseconds
100.00% <= 7 milliseconds
100.00% <= 7 milliseconds
75187.97 requests per second

====== PING_BULK ======
  100000 requests completed in 1.27 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

98.80% <= 1 milliseconds
99.79% <= 2 milliseconds
99.91% <= 3 milliseconds
99.96% <= 4 milliseconds
99.99% <= 5 milliseconds
100.00% <= 6 milliseconds
78678.20 requests per second

====== SET ======
  100000 requests completed in 1.36 seconds
  50 parallel clients
  3 bytes payload
  keep alive: 1

97.43% <= 1 milliseconds
99.48% <= 2 milliseconds
99.79% <= 3 milliseconds
99.92% <= 4 milliseconds
99.97% <= 5 milliseconds
100.00% <= 5 milliseconds
73583.52 requests per second

.....

```

指定参数的测试，下面的意思是主机为 127.0.0.1，端口号为 6379，执行的命令为 set,lpush，请求数为 10000，通过 -q 参数让结果只显示每秒执行的请求数。

```bash
$redis-benchmark -h 127.0.0.1 -p 6379 -t set,lpush -n 100000 -q

SET: 73475.39 requests per second
LPUSH: 79239.30 requests per second
```

#### 详细参数

| 序号 | 选项      | 描述                                       | 默认值    |
| :--- | :-------- | :----------------------------------------- | :-------- |
| 1    | **-h**    | 指定服务器主机名                           | 127.0.0.1 |
| 2    | **-p**    | 指定服务器端口                             | 6379      |
| 3    | **-s**    | 指定服务器 socket                          |           |
| 4    | **-c**    | 指定并发连接数                             | 50        |
| 5    | **-n**    | 指定请求数                                 | 10000     |
| 6    | **-d**    | 以字节的形式指定 SET/GET 值的数据大小      | 2         |
| 7    | **-k**    | 1=keep alive 0=reconnect                   | 1         |
| 8    | **-r**    | SET/GET/INCR 使用随机 key, SADD 使用随机值 |           |
| 9    | **-P**    | 通过管道传输 <numreq> 请求                 | 1         |
| 10   | **-q**    | 强制退出 redis。仅显示 query/sec 值        |           |
| 11   | **--csv** | 以 CSV 格式输出                            |           |
| 12   | **-l**    | 生成循环，永久执行测试                     |           |
| 13   | **-t**    | 仅运行以逗号分隔的测试命令列表。           |           |
| 14   | **-I**    | Idle 模式。仅打开 N 个 idle 连接并等待。   |           |

### Redis 客户端连接

### Redis 管道技术

### Redis 分区

### Java 使用 Redis



### Redis命令

??