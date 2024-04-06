MongoDB
----



https://pdai.tech/md/db/nosql-mongo/mongo.html



https://www.w3cschool.cn/mongodb/mongodb-1uxs37ih.html

## 基础教程

### MongoDB应用场景

- 游戏场景，使用 MongoDB 存储游戏用户信息，用户的装备、积分等直接以内嵌文档的形式存储，方便查询、更新

- 物流场景，使用 MongoDB 存储订单信息，订单状态在运送过程中会不断更新，以 MongoDB 内嵌数组的形式来存储，一次查询就能将订单所有的变更读取出来。

- 社交场景，使用 MongoDB 存储存储用户信息，以及用户发表的朋友圈信息，通过地理位置索引实现附近的人、地点等功能

- 物联网场景，使用 MongoDB 存储所有接入的智能设备信息，以及设备汇报的日志信息，并对这些信息进行多维度的分析

- 视频直播，使用 MongoDB 存储用户信息、礼物信息等。

- 运维监控系统。在一些大型的项目中，监控是必不可少的。监控系统要监控的内容，可能是随时多变的，这时候使用MongoDB就体现了很大的便利。**不需要去修改数据库的结构**，直接根据业务需要灵活调整即可。大大**降低了开发成本**。

- 第三方信息的抓取与存储

  我们在一些业务场景中难免会去使用到第三方的数据，当接入多个第三方平台时，这时候我们需要考虑到**每个平台数据格式不一致，自身的存储系统结构设计**等情况。这时候我们使用MongoDB来存储就很好的避免了这个问题。

- 应用服务器的日志记录

  日常我们会把一些应用日志存储到文本格式的文件中，这样不便于查看同时也不便于统计等。通过MongoDB存储，既可以很好的存储、统计同时也方便不同的业务场景下**日志数据格式不一致**等情况。

这些应用场景中，数据操作方面的共同特点是：

（1）数据量大

（2）写入操作频繁（读写都很频繁）

（3）价值较低的数据，对事务性要求不高 对于这样的数据，我们更适合使用MongoDB来实现数据的存储。



### 安装

- 拉取镜像

```
docker pull mongo
```

- 创建容器

```
docker run -di --name mongo-service --restart=always -p 27017:27017 -v ~/data/mongodata:/data mongo
```

- 使用navicat链接MongoDB测试

或使用命令行进入

```sh
$ docker exec -it mongo-service bash
$ mongo
```



### MongoDB 概念解析

| SQL术语/概念 | MongoDB术语/概念 | 解释/说明                           |
| :----------- | :--------------- | :---------------------------------- |
| database     | database         | 数据库                              |
| table        | collection       | 数据库表/集合                       |
| row          | document         | 数据记录行/文档                     |
| column       | field            | 数据字段/域                         |
| index        | index            | 索引                                |
| table joins  |                  | 表连接,MongoDB不支持                |
| primary key  | primary key      | 主键,MongoDB自动将_id字段设置为主键 |

![](images/image-20240406085444119.png)

#### 数据库

有一些数据库名是保留的，可以直接访问这些有特殊作用的数据库。

- **admin**： 从权限的角度来看，这是"**root**"数据库。要是将一个用户添加到这个数据库，这个用户自动继承所有数据库的权限。一些特定的服务器端命令也只能从这个数据库运行，比如列出所有的数据库或者关闭服务器。
- **local:** 这个数据永远不会被复制，可以用来存储限于本地单台服务器的任意集合
- **config**: 当Mongo用于分片设置时，config数据库在内部使用，用于保存分片的相关信息。



```
show dbs
use [数据库名]
db
```

#### 文档

==文档（Document）==是一个键值(key-value)对(即BSON)。MongoDB 的文档不需要设置相同的字段，并且相同的字段不需要相同的数据类型，这与关系型数据库有很大的区别，也是 MongoDB 非常突出的特点。

需要注意的是：

1. 文档中的键/值对是有序的。
2. 文档中的值不仅可以是在双引号里面的字符串，还可以是其他几种数据类型（甚至可以是整个嵌入的文档)。
3. MongoDB区分类型和大小写。
4. MongoDB的文档不能有重复的键。
5. 文档的键是字符串。除了少数例外情况，键可以使用任意UTF-8字符。

文档键命名规范：

- 键不能含有\0 (空字符)。这个字符用来表示键的结尾。
- .和$有特别的意义，只有在特定环境下才能使用。
- 以下划线"_"开头的键是保留的(不是严格要求的)。

#### 集合

集合就是 MongoDB 文档组，类似于 RDBMS （关系数据库管理系统：Relational Database Management System)中的表格。

集合存在于数据库中，集合没有固定的结构，这意味着你在对集合可以插入不同格式和类型的数据，但通常情况下我们插入集合的数据都会有一定的关联性。



##### capped collections

Capped collections 就是固定大小的collection。

它有很高的性能以及队列过期的特性(过期按照插入的顺序). 有点和 "RRD" 概念类似。

Capped collections是高性能自动的维护对象的插入顺序。它非常适合类似记录日志的功能 和标准的collection不同，你必须要显式的创建一个capped collection， 指定一个collection的大小，单位是字节。collection的数据存储空间值提前分配的。

Capped collections 可以按照文档的插入顺序保存到集合中，而且这些文档在磁盘上存放位置也是按照插入顺序来保存的，所以当我们更新Capped collections 中文档的时候，更新后的文档不可以超过之前文档的大小，这样话就可以确保所有文档在磁盘上的位置一直保持不变。

由于 Capped collection 是按照文档的插入顺序而不是使用索引确定插入位置，这样的话可以提高增添数据的效率。MongoDB 的操作日志文件 **oplog.rs** 就是利用 Capped Collection 来实现的。

要注意的是指定的存储大小包含了数据库的头信息。

```
db.createCollection("mycoll", {capped:true, size:100000})
```

- 在capped collection中，你能添加新的对象。
- 能进行更新，然而，对象不会增加存储空间。如果增加，更新就会失败 。
- 数据库不允许进行删除。使用`drop()`方法删除collection所有的行。
- 注意: 删除之后，你必须显式的重新创建这个collection。
- 在32bit机器中，capped collection最大存储为1e9( 1X109)个字节。



#### 元数据





#### MongoDB 数据类型

| 数据类型           | 描述                                                         |
| :----------------- | :----------------------------------------------------------- |
| String             | 字符串。存储数据常用的数据类型。在 MongoDB 中，UTF-8 编码的字符串才是合法的。 |
| Integer            | 整型数值。用于存储数值。根据你所采用的服务器，可分为 32 位或 64 位。 |
| Boolean            | 布尔值。用于存储布尔值（真/假）。                            |
| Double             | 双精度浮点值。用于存储浮点值。                               |
| Min/Max keys       | 将一个值与 BSON（二进制的 JSON）元素的最低值和最高值相对比。 |
| Arrays             | 用于将数组或列表或多个值存储为一个键。                       |
| Timestamp          | 时间戳。记录文档修改或添加的具体时间。                       |
| Object             | 用于内嵌文档。                                               |
| Null               | 用于创建空值。                                               |
| Symbol             | 符号。该数据类型基本上等同于字符串类型，但不同的是，它一般用于采用特殊符号类型的语言。 |
| Date               | 日期时间。用 UNIX 时间格式来存储当前日期或时间。你可以指定自己的日期时间：创建 Date 对象，传入年月日信息。 |
| Object ID          | 对象 ID。用于创建文档的 ID。                                 |
| Binary Data        | 二进制数据。用于存储二进制数据。                             |
| Code               | 代码类型。用于在文档中存储 JavaScript 代码。                 |
| Regular expression | 正则表达式类型。用于存储正则表达式。                         |

### MongoDB 连接





### 创建、删除数据库

```
use DATABASE_NAME
db.dropDatabase()
db.collection.drop()
```





### 插入文档

文档的数据结构和JSON基本一样。

所有存储在集合中的数据都是BSON格式。

BSON是一种类json的一种二进制形式的存储格式,简称Binary JSON。

```
db.COLLECTION_NAME.insert(document)
```



```
db.col.insert({title: 'MongoDB 教程', 
    description: 'MongoDB 是一个 Nosql 数据库',
    by: 'w3cschool',
    url: 'http://www.w3cschool.cn',
    tags: ['mongodb', 'database', 'NoSQL'],
    likes: 100
})
```

```
db.col.find()
```



可以把数据定义为一变量：

```
document=({title: 'MongoDB 教程', 
    description: 'MongoDB 是一个 Nosql 数据库',
    by: 'w3cschool',
    url: 'http://www.w3cschool.cn',
    tags: ['mongodb', 'database', 'NoSQL'],
    likes: 100
});
```

然后讲变量插入：

```
db.col.insert(document)
```

插入文档你也可以使用 `db.col.save(document) `命令。如果不指定 **_id** 字段 `save() `方法类似于 `insert() `方法。如果指定 **_id** 字段，则会更新该 **_id** 的数据。



### 更新文档

**`update()`** 和`save() `

```
db.collection.update(
   <query>,
   <update>,
   {
     upsert: <boolean>,
     multi: <boolean>,
     writeConcern: <document>
   }
)
```

- **query** : update的查询条件，类似sql update查询的where子句。
- **update** : update的对象和一些更新的操作符（如$,$inc...）等，也可以理解为 sql update查询的set子句
- **upsert** : 可选，这个参数的意思是，如果不存在update的记录，是否插入**objNew**,true为插入，默认是false，不插入。
- **multi** : 可选，mongodb 默认是false,只更新找到的第一条记录，如果这个参数为true,就把按条件查出来多条记录全部更新。
- **writeConcern** :可选，抛出异常的级别。

```
db.col.update({'title':'MongoDB 教程'},{$set:{'title':'MongoDB'}},{multi:true})
```



```
db.collection.save(
   <document>,
   {
     writeConcern: <document>
   }
)
```

- **document** : 文档数据。
- **writeConcern** :可选，抛出异常的级别。

```
db.col.save({
    "_id" : ObjectId("660ff7b7b9aff65b9d9a8ab0"),
    "title" : "MongoDB",
    "description" : "MongoDB 是一个 Nosql 数据库",
    "by" : "W3Cshcool",
    "url" : "http://www.w3cschool.cn",
    "tags" : [
            "mongodb",
            "NoSQL"
    ],
    "likes" : 110
})
```



```
db.col.find().pretty()  // 易读方式查看
```





### 删除文档

```
db.collection.remove(
   <query>,
   {
     justOne: <boolean>,
     writeConcern: <document>
   }
)
```

- **query** :（可选）删除的文档的条件。
- **justOne** : （可选）如果设为 true 或 1，则只删除一个文档。
- **writeConcern** :（可选）抛出异常的级别。

```
db.col.remove({'title':'MongoDB 教程'})
```



删除一条

```
db.COLLECTION_NAME.remove(DELETION_CRITERIA,1)
```

删除所有

```
db.col.remove({})
```



### 查询文档

```
db.COLLECTION_NAME.find()[.pretty()]
```



#### MongoDB 与 RDBMS Where 语句比较

如果你熟悉常规的 SQL 数据，通过下表可以更好的理解 MongoDB 的条件语句查询：

| 操作       | 格式                         | 范例                                            | RDBMS中的类似语句            |
| :--------- | :--------------------------- | :---------------------------------------------- | :--------------------------- |
| 等于       | **`{<key>:<value>}`**        | **`db.col.find({"by":"w3cschool"}).pretty()`**  | **`where by = 'w3cschool'`** |
| 小于       | **`{<key>:{$lt:<value>}}`**  | **`db.col.find({"likes":{$lt:50}}).pretty()`**  | **`where likes < 50`**       |
| 小于或等于 | **`{<key>:{$lte:<value>}}`** | **`db.col.find({"likes":{$lte:50}}).pretty()`** | **`where likes <= 50`**      |
| 大于       | **`{<key>:{$gt:<value>}}`**  | **`db.col.find({"likes":{$gt:50}}).pretty()`**  | **`where likes > 50`**       |
| 大于或等于 | **`{<key>:{$gte:<value>}}`** | **`db.col.find({"likes":{$gte:50}}).pretty()`** | **`where likes >= 50`**      |
| 不等于     | **`{<key>:{$ne:<value>}}`**  | **`db.col.find({"likes":{$ne:50}}).pretty()`**  | `**where likes != 50**`      |

#### AND 条件

```
db.col.find({key1:value1, key2:value2}).pretty()
```

#### OR 条件

关键字 **`$or`**

```
db.col.find(
   {
      $or: [
	     {key1: value1}, {key2:value2}
      ]
   }
).pretty()
```

```
db.col.find({$or:[{"by":"w3cschool"},{"title": "MongoDB 教程"}]}).pretty()
```

#### AND 和 OR 联合使用

```
db.col.find({"likes": {$gt:50}, $or: [{"by": "w3cschool"},{"title": "MongoDB 教程"}]}).pretty()
```

### 条件操作符

```
db.col.insert({
    title: 'PHP 教程', 
    description: 'PHP 是一种创建动态交互性站点的强有力的服务器端脚本语言。',
    by: 'w3cschool',
    url: 'http://www.w3cschool.cn',
    tags: ['php'],
    likes: 200
})

db.col.insert({title: 'Java 教程', 
    description: 'Java 是由Sun Microsystems公司于1995年5月推出的高级程序设计语言。',
    by: 'w3cschool',
    url: 'http://www.w3cschool.cn',
    tags: ['java'],
    likes: 150
})

db.col.insert({title: 'MongoDB 教程', 
    description: 'MongoDB 是一个 Nosql 数据库',
    by: 'w3cschool',
    url: 'http://www.w3cschool.cn',
    tags: ['mongodb'],
    likes: 100
})
```

```
db.col.find({"likes" : {$gt : 100}}).pretty()

db.col.find({"likes" : {$gte : 100}}).pretty()

db.col.find({likes : {$lt : 150}})
db.col.find({likes : {$lte : 150}})

db.col.find({likes : {$lt :200, $gt : 100}})
```



### $type条件操作符

`$type`操作符是基于BSON类型来检索集合中匹配的数据类型，并返回结果。

MongoDB 中可以使用的类型如下表所示：

| **类型**                | **数字** | **备注**       |
| :---------------------- | :------- | :------------- |
| Double                  | 1        |                |
| String                  | 2        |                |
| Object                  | 3        |                |
| Array                   | 4        |                |
| Binary data             | 5        |                |
| Undefined               | 6        | 已废弃。       |
| Object id               | 7        |                |
| Boolean                 | 8        |                |
| Date                    | 9        |                |
| Null                    | 10       |                |
| Regular Expression      | 11       |                |
| JavaScript              | 13       |                |
| Symbol                  | 14       |                |
| JavaScript (with scope) | 15       |                |
| 32-bit integer          | 16       |                |
| Timestamp               | 17       |                |
| 64-bit integer          | 18       |                |
| Min key                 | 255      | Query with -1. |
| Max key                 | 127      |                |



```
db.col.find({"title" : {$type : 2}})
```



### Limit与Skip方法

`limit()`方法来读取指定数量的数据外，使用`skip()`方法来跳过指定数量的数据

```
db.COLLECTION_NAME.find().limit(NUMBER)
```

```
db.COLLECTION_NAME.find().limit(NUMBER).skip(NUMBER)
```



### 排序

 

在MongoDB中使用使用`sort()`方法对数据进行排序，`sort()`方法可以通过参数指定排序的字段，并使用 1 和 -1 来指定排序的方式，其中 1 为升序排列，而-1是用于降序排列。

```
db.COLLECTION_NAME.find().sort({KEY:1})
```

```
db.col.find().sort({"title":-1})
```

### 索引

MongoDB使用 `ensureIndex()` 方法来创建索引。

```
db.COLLECTION_NAME.ensureIndex({KEY:1})
```



```
db.mycol.ensureIndex({"title":1})
db.mycol.ensureIndex({"title":1,"description":-1})
```

`ensureIndex() `接收可选参数，可选参数列表如下：

| Parameter          | Type          | Description                                                  |
| :----------------- | :------------ | :----------------------------------------------------------- |
| background         | Boolean       | 建索引过程会阻塞其它数据库操作，background可指定以后台方式创建索引，即增加 "background" 可选参数。 "background" 默认值为**false**。 |
| unique             | Boolean       | 建立的索引是否唯一。指定为true创建唯一索引。默认值为**false**. |
| name               | string        | 索引的名称。如果未指定，MongoDB的通过连接索引的字段名和排序顺序生成一个索引名称。 |
| dropDups           | Boolean       | 在建立唯一索引时是否删除重复记录,指定 true 创建唯一索引。默认值为 **false**. |
| sparse             | Boolean       | 对文档中不存在的字段数据不启用索引；这个参数需要特别注意，如果设置为true的话，在索引字段中不会查询出不包含对应字段的文档.。默认值为 **false**. |
| expireAfterSeconds | integer       | 指定一个以秒为单位的数值，完成 TTL设定，设定集合的生存时间。 |
| v                  | index version | 索引的版本号。默认的索引版本取决于mongod创建索引时运行的版本。 |
| weights            | document      | 索引权重值，数值在 1 到 99,999 之间，表示该索引相对于其他索引字段的得分权重。 |
| default_language   | string        | 对于文本索引，该参数决定了停用词及词干和词器的规则的列表。 默认为英语 |
| language_override  | string        | 对于文本索引，该参数指定了包含在文档中的字段名，语言覆盖默认的language，默认值为 language. |



### 聚合

MongoDB中聚合(aggregate)主要用于处理数据(诸如统计平均值,求和等)，并返回计算后的数据结果。有点类似sql语句中的 count(*)。

```
db.COLLECTION_NAME.aggregate(AGGREGATE_OPERATION)
```



```
> db.col.aggregate([{$group: {_id: "$title", nums: {$sum:1}}}])
{ "_id" : "Java 教程", "nums" : 1 }
{ "_id" : "PHP 教程", "nums" : 1 }
{ "_id" : "MongoDB", "nums" : 2 }
{ "_id" : "MongoDB 教程", "nums" : 2 }
```

似sql语句：

```
select title _id, count(*) nums from col group title;
```



一些聚合的表达式:

| 表达式    | 描述                                           | 实例                                                         |
| :-------- | :--------------------------------------------- | :----------------------------------------------------------- |
| $sum      | 计算总和。                                     | db.mycol.aggregate([{$group : {_id : "$by_user", num_tutorial : {$sum : "$likes"}}}]) |
| $avg      | 计算平均值                                     | db.mycol.aggregate([{$group : {_id : "$by_user", num_tutorial : {$avg : "$likes"}}}]) |
| $min      | 获取集合中所有文档对应值得最小值。             | db.mycol.aggregate([{$group : {_id : "$by_user", num_tutorial : {$min : "$likes"}}}]) |
| $max      | 获取集合中所有文档对应值得最大值。             | db.mycol.aggregate([{$group : {_id : "$by_user", num_tutorial : {$max : "$likes"}}}]) |
| $push     | 在结果文档中插入值到一个数组中。               | db.mycol.aggregate([{$group : {_id : "$by_user", url : {$push: "$url"}}}]) |
| $addToSet | 在结果文档中插入值到一个数组中，但不创建副本。 | db.mycol.aggregate([{$group : {_id : "$by_user", url : {$addToSet : "$url"}}}]) |
| $first    | 根据资源文档的排序获取第一个文档数据。         | db.mycol.aggregate([{$group : {_id : "$by_user", first_url : {$first : "$url"}}}]) |
| $last     | 根据资源文档的排序获取最后一个文档数据         | db.mycol.aggregate([{$group : {_id : "$by_user", last_url : {$last : "$url"}}}]) |

#### 管道的概念 🔖

管道在Unix和Linux中一般用于将当前命令的输出结果作为下一个命令的参数。

MongoDB的聚合管道将MongoDB文档在一个管道处理完毕后将结果传递给下一个管道处理。管道操作是可以重复的。

表达式：处理输入文档并输出。表达式是无状态的，只能用于计算当前聚合管道的文档，不能处理其它的文档。

这里我们介绍一下聚合框架中常用的几个操作：

- $project：修改输入文档的结构。可以用来重命名、增加或删除域，也可以用于创建计算结果以及嵌套文档。
- $match：用于过滤数据，只输出符合条件的文档。$match使用MongoDB的标准查询操作。
- $limit：用来限制MongoDB聚合管道返回的文档数。
- $skip：在聚合管道中跳过指定数量的文档，并返回余下的文档。
- $unwind：将文档中的某一个数组类型字段拆分成多条，每条包含数组中的一个值。
- $group：将集合中的文档分组，可用于统计结果。
- $sort：将输入文档排序后输出。
- $geoNear：输出接近某一地理位置的有序文档。



### 复制(副本集) 🔖

MongoDB复制是将数据同步在多个服务器的过程。

复制提供了数据的冗余备份，并在多个服务器上存储数据副本，提高了数据的可用性， 并可以保证数据的安全性。

复制还允许您从硬件故障和服务中断中恢复数据。



### 分片🔖

在Mongodb里面存在另一种集群，就是分片技术,可以满足MongoDB数据量大量增长的需求。

当MongoDB存储海量的数据时，一台机器可能不足以存储数据也足以提供可接受的读写吞吐量。这时，我们就可以通过在多台机器上分割数据，使得数据库系统能存储和处理更多的数据。



### 备份(mongodump)与恢复(mongorerstore) 🔖



### 监控

mongostat 和 mongotop 两个命令来监控MongoDB的运行情况。

mongostat是mongodb自带的状态检测工具，在命令行下使用。它会间隔固定时间获取mongodb的当前运行状态，并输出。如果你发现数据库突然变慢或者有其他问题的话，你第一手的操作就考虑采用mongostat来查看mongo的状态。



mongotop也是mongodb下的一个内置工具，mongotop提供了一个方法，用来跟踪一个MongoDB的实例，查看哪些大量的时间花费在读取和写入数据。 mongotop提供每个集合的水平的统计数据。默认情况下，mongotop返回值的每一秒。



### MongoDB Java



### MongoDB PHP 扩展🔖



## MongoDB 高级教程

### 关系

MongoDB 的关系表示多个文档之间在逻辑上的相互联系。

文档间可以通过嵌入和引用来建立联系。

MongoDB 中的关系可以是：

- 1:1 (1对1)
- 1: N (1对多)
- N: 1 (多对1)
- N: N (多对多)



### 引用

在上一章节MongoDB关系中我们提到了MongoDB的引用来规范数据结构文档。

MongoDB 引用有两种：

- 手动引用（Manual References）
- DBRefs



### 覆盖索引查询



### 查询分析

explain() 和 hint()



```
db.users.find({gender:"M"},{user_name:1,_id:0}).explain()
```



### 原子操作

mongodb不支持事务，所以，在你的项目中应用时，要注意这点。无论什么设计，都不要要求mongodb保证数据的完整性。

但是mongodb提供了许多原子操作，比如文档的保存，修改，删除等，都是原子操作。

所谓原子操作就是要么这个文档保存到Mongodb，要么没有保存到Mongodb，不会出现查询到的文档没有保存完整的情况



### 高级索引



### 索引限制

额外开销
每个索引占据一定的存储空间，在进行插入，更新和删除操作时也需要对索引进行操作。所以，如果你很少对集合进行读取操作，建议不使用索引。



内存(RAM)使用
由于索引是存储在内存(RAM)中,你应该确保该索引的大小不超过内存的限制。

如果索引的大小大于内存的限制，MongoDB会删除一些索引，这将导致性能下降。



查询限制
索引不能被以下的查询使用：

- 正则表达式及非操作符，如 `$nin`, `$not`, 等。
- 算术运算符，如 `$mod`, 等。
- `$where` 子句

所以，检测你的语句是否使用索引是一个好的习惯，可以用explain来查看。



索引键限制
从2.6版本开始，如果现有的索引字段的值超过索引键的限制，MongoDB中不会创建索引。



插入文档超过索引键限制
如果文档的索引字段值超过了索引键的限制，MongoDB不会将任何文档转换成索引的集合。与mongorestore和mongoimport工具类似。



最大范围

- 集合中索引不能超过64个
- 索引名的长度不能超过125个字符
- 一个复合索引最多可以有31个字段

### ObjectId

ObjectId 是一个12字节 BSON 类型数据，有以下格式：

- 前4个字节表示时间戳
- 接下来的3个字节是机器标识码
- 紧接的两个字节由进程id组成（PID）
- 最后三个字节是随机数。

MongoDB中存储的文档必须有一个"_id"键。这个键的值可以是任何类型的，默认是个ObjectId对象。

在一个集合里面，每个集合都有唯一的"_id"值，来确保集合里面每个文档都能被唯一标识。

MongoDB采用ObjectId，而不是其他比较常规的做法（比如自动增加的主键）的主要原因，因为在多个 服务器上同步自动增加主键值既费力还费时。

```
> newObjectId = ObjectId()
ObjectId("661010d2b9aff65b9d9a8ab6")
> newObjectId.getTimestamp()
ISODate("2024-04-05T14:55:14Z")
> newObjectId.str
661010d2b9aff65b9d9a8ab6
```



### Map Reduce

Map-Reduce是一种计算模型，简单的说就是将大批量的工作（数据）分解（MAP）执行，然后再将结果合并成最终结果（REDUCE）。

MongoDB提供的Map-Reduce非常灵活，对于大规模数据分析也相当实用。

```
db.collection.mapReduce(
   function() {emit(key,value);},  //map 函数
   function(key,values) {return reduceFunction},   //reduce 函数
   {
      out: collection,
      query: document,
      sort: document,
      limit: number
   }
)
```

使用 MapReduce 要实现两个函数 Map 函数和 Reduce 函数,Map 函数调用 emit(key, value), 遍历 collection 中所有的记录, 将key 与 value 传递给 Reduce 函数进行处理。

Map 函数必须调用 emit(key, value) 返回键值对。

参数说明:

- **map** ：映射函数 (生成键值对序列,作为 reduce 函数参数)。
- **reduce** 统计函数，reduce函数的任务就是将key-values变成key-value，也就是把values数组变成一个单一的值value。。
- **out** 统计结果存放集合 (不指定则使用临时集合,在客户端断开后自动删除)。
- **query** 一个筛选条件，只有满足条件的文档才会调用map函数。（query。limit，sort可以随意组合）
- **sort** 和limit结合的sort排序参数（也是在发往map函数前给文档排序），可以优化分组机制
- **limit** 发往map函数的文档数量的上限（要是没有limit，单独使用sort的用处不大）



### 全文检索

全文检索对每一个词建立一个索引，指明该词在文章中出现的次数和位置，当用户查询时，检索程序就根据事先建立的索引进行查找，并将查找的结果反馈给用户的检索方式。

这个过程类似于通过字典中的检索字表查字的过程。

启用全文检索：

```
db.adminCommand({setParameter:true,textSearchEnabled:true})
```

```
mongod --setParameter textSearchEnabled=true
```



### MongoDB 正则表达式

正则表达式是使用单个字符串来描述、匹配一系列符合某个句法规则的字符串。

许多程序设计语言都支持利用正则表达式进行字符串操作。

MongoDB 使用 **`$regex`** 操作符来设置匹配字符串的正则表达式。

MongoDB使用PCRE (Perl Compatible Regular Expression) 作为正则表达式语言。

不同于全文检索，我们使用正则表达式不需要做任何配置。



### GridFS

GridFS 用于存储和恢复那些超过16M（BSON文件限制）的文件(如：图片、音频、视频等)。

GridFS 也是文件存储的一种方式，但是它是存储在MonoDB的集合中。

GridFS 可以更好的存储大于16M的文件。

GridFS 会将大文件对象分割成多个小的chunk(文件片段),一般为256k/个,每个chunk将作为MongoDB的一个文档(document)被存储在chunks集合中。



GridFS 用两个集合来存储一个文件：fs.files与fs.chunks。

每个文件的实际内容被存在chunks(二进制数据)中,和文件有关的meta数据(filename,content_type,还有用户自定义的属性)将会被存在files集合中。



### 固定集合

MongoDB 固定集合（Capped Collections）是性能出色且有着固定大小的集合，对于大小固定，我们可以想象其就像一个环形队列，当集合空间用完后，再插入的元素就会覆盖最初始的头部的元素！

```
db.createCollection("cappedLogCollection",{capped:true,size:10000,max:1000})
```



### 自动增长

MongoDB 没有像 SQL 一样有自动增长的功能， MongoDB 的 _id 是系统自动生成的12字节唯一标识。

但在某些情况下，我们可能需要实现 ObjectId 自动增长功能。

由于 MongoDB 没有实现这个功能，我们可以通过编程的方式来实现，以下我们将在 counters 集合中实现_id字段自动增长。