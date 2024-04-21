SQL总结
----



## 力扣

### [180. 连续出现的数字](https://leetcode.cn/problems/consecutive-numbers/)

```
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| num         | varchar |
+-------------+---------+
在 SQL 中，id 是该表的主键
id 是一个自增列
```

找出所有至少连续出现三次的数字。

返回的结果表中的数据可以按 **任意顺序** 排列。

```mysql
Select Distinct l1.Num As ConsecutiveNums 
From Logs l1, Logs l2, Logs l3
Where l1.Id = l2.Id - 1
And l2.Id = l3.Id - 1
And l1.Num = l2.Num
And l2.Num = l3.Num;
```

### [184. 部门工资最高的员工](https://leetcode.cn/problems/department-highest-salary/)

表： `Employee`

```
+--------------+---------+
| 列名          | 类型    |
+--------------+---------+
| id           | int     |
| name         | varchar |
| salary       | int     |
| departmentId | int     |
+--------------+---------+
在 SQL 中，id是此表的主键。
departmentId 是 Department 表中 id 的外键（在 Pandas 中称为 join key）。
此表的每一行都表示员工的 id、姓名和工资。它还包含他们所在部门的 id。
```

表： `Department`

```
+-------------+---------+
| 列名         | 类型    |
+-------------+---------+
| id          | int     |
| name        | varchar |
+-------------+---------+
在 SQL 中，id 是此表的主键列。
此表的每一行都表示一个部门的 id 及其名称。
```

查找出每个部门中薪资最高的员工。
按 **任意顺序** 返回结果表。

```
输出：
+------------+----------+--------+
| Department | Employee | Salary |
+------------+----------+--------+
| IT         | Jim      | 90000  |
| Sales      | Henry    | 80000  |
| IT         | Max      | 90000  |
+------------+----------+--------+
解释：Max 和 Jim 在 IT 部门的工资都是最高的，Henry 在销售部的工资最高。
```

#### 答案



```mysql
Select 
    d.name As Department, 
    e.name As Employee, 
    e.salary As Salary
From 
    Employee e, Department d
Where 
    e.departmentId = d.id
And 
    (e.departmentId, e.salary) In
    (
        Select departmentId, max(salary)
        From Employee
        Group By departmentId
    )
;
```

### [185. 部门工资前三高的所有员工](https://leetcode.cn/problems/department-top-three-salaries/)



```mysql
Select d.name Department, e.name Employee, e.salary Salary
From Employee e 
Left Join Department d 
On e.departmentId = d.id 
Where e.id In 
(
    Select e1.id 
    From Employee e1 
    Left Join Employee e2
    On e1.departmentId = e2.departmentId And e1.salary < e2.salary
    Group By e1.id
    Having Count(Distinct e2.salary) <=2
)
And e.departmentId In (Select id From Department)
Order By d.id ASC, e.salary DESC;
```



### [262. 行程和用户](https://leetcode.cn/problems/trips-and-users/)

表：`Trips`

```
+-------------+----------+
| Column Name | Type     |
+-------------+----------+
| id          | int      |
| client_id   | int      |
| driver_id   | int      |
| city_id     | int      |
| status      | enum     |
| request_at  | date     |     
+-------------+----------+
id 是这张表的主键（具有唯一值的列）。
这张表中存所有出租车的行程信息。每段行程有唯一 id ，其中 client_id 和 driver_id 是 Users 表中 users_id 的外键。
status 是一个表示行程状态的枚举类型，枚举成员为(‘completed’, ‘cancelled_by_driver’, ‘cancelled_by_client’) 。 
```

表：`Users`

```
+-------------+----------+
| Column Name | Type     |
+-------------+----------+
| users_id    | int      |
| banned      | enum     |
| role        | enum     |
+-------------+----------+
users_id 是这张表的主键（具有唯一值的列）。
这张表中存所有用户，每个用户都有一个唯一的 users_id ，role 是一个表示用户身份的枚举类型，枚举成员为 (‘client’, ‘driver’, ‘partner’) 。
banned 是一个表示用户是否被禁止的枚举类型，枚举成员为 (‘Yes’, ‘No’) 。
```

**取消率** 的计算方式如下：(被司机或乘客取消的非禁止用户生成的订单数量) / (非禁止用户生成的订单总数)。

编写解决方案找出 `"2013-10-01"` 至 `"2013-10-03"` 期间非禁止用户（**乘客和司机都必须未被禁止**）的取消率。非禁止用户即 banned 为 No 的用户，禁止用户即 banned 为 Yes 的用户。其中取消率 `Cancellation Rate` 需要四舍五入保留 **两位小数** 。

返回结果表中的数据 **无顺序要求** 。

```mysql
Select request_at `Day`, Round(Avg(status != 'completed'), 2) `Cancellation Rate`
From Trips t
Join Users u1 On (t.client_id = u1.users_id And u1.banned = 'No')
Join Users u2 On (t.driver_id = u2.users_id And u2.banned = 'No')
Where request_at Between '2013-10-01' And '2013-10-03'
Group By request_at;
```

### [577. 员工奖金](https://leetcode.cn/problems/employee-bonus/)



```mysql
Select name, bonus
From Employee e 
Left Join Bonus b 
On e.empid = b. empId 
Where b.bonus is null or b.bonus < 1000 ;
```

空值在数据库中的表示为 `null`。使用 `bonus is null`（而不是 `bonus = null`）判断奖金是否为 `null`





### [585. 2016年的投资](https://leetcode.cn/problems/investments-in-2016/) 🔖

`Insurance` 表：

```
+-------------+-------+
| Column Name | Type  |
+-------------+-------+
| pid         | int   |
| tiv_2015    | float |
| tiv_2016    | float |
| lat         | float |
| lon         | float |
+-------------+-------+
pid 是这张表的主键(具有唯一值的列)。
表中的每一行都包含一条保险信息，其中：
pid 是投保人的投保编号。
tiv_2015 是该投保人在 2015 年的总投保金额，tiv_2016 是该投保人在 2016 年的总投保金额。
lat 是投保人所在城市的纬度。题目数据确保 lat 不为空。
lon 是投保人所在城市的经度。题目数据确保 lon 不为空。
```

编写解决方案报告 2016 年 (`tiv_2016`) 所有满足下述条件的投保人的投保金额之和：

- 他在 2015 年的投保额 (`tiv_2015`) 至少跟一个其他投保人在 2015 年的投保额相同。
- 他所在的城市必须与其他投保人都不同（也就是说 (`lat, lon`) 不能跟其他任何一个投保人完全相同）。

`tiv_2016` 四舍五入的 **两位小数** 。

```mysql
select round(sum(tiv_2016),2) tiv_2016
from 
    (
        select tiv_2016,
        count(*)over(partition by tiv_2015) cn_2015,
        count(*)over(partition by lat,lon) cn_l
        from insurance
    ) t 
where cn_2015 >1 and cn_l = 1;
```



在 SQL 中，`COUNT(*) OVER (PARTITION BY tiv_2015)` 是一个==窗口函数==的表达式，它用于计算每个由 `PARTITION BY` 子句指定的分区内的行数。这里的 `tiv_2015` 是用于分区的列。

#### 详细解释：

- `COUNT(*)`：这是一个聚合函数，计算行数。`*` 表示计数时包括所有的行，不考虑 `NULL` 值。
- `OVER`：这个关键字用于指定一个窗口，窗口函数会在该窗口的行上操作。
- `PARTITION BY tiv_2015`：这部分指定了窗口函数应用的分区标准。`PARTITION BY` 类似于 `GROUP BY`，但它是用于窗口函数的。在这里，它将数据分成不同的分区，每个分区内的 `tiv_2015` 值相同。



### [586. 订单最多的客户](https://leetcode.cn/problems/customer-placing-the-largest-number-of-orders/)



```mysql
Select customer_number 
From Orders
Group By customer_number
Order BY count(*) DESC
Limit 1;
```



### [596. 超过5名学生的课](https://leetcode.cn/problems/classes-more-than-5-students/)



```mysql
Select class
From Courses
Group By class
Having count( Distinct student) >= 5;
```



### [601. 体育馆的人流量](https://leetcode.cn/problems/human-traffic-of-stadium/)

表：`Stadium`

```
+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| visit_date    | date    |
| people        | int     |
+---------------+---------+
visit_date 是该表中具有唯一值的列。
每日人流量信息被记录在这三列信息中：序号 (id)、日期 (visit_date)、 人流量 (people)
每天只有一行记录，日期随着 id 的增加而增加
```

 

编写解决方案找出每行的人数大于或等于 `100` 且 `id` 连续的三行或更多行记录。

返回按 `visit_date` **升序排列** 的结果表。



```mysql
Select Distinct s1.* 
From stadium s1, stadium s2, stadium s3
Where s1.people >= 100
And s2.people >= 100
And s3.people >= 100
And (
    (s1.id = s2.id - 1 And s2.id = s3.id - 1) Or
    (s1.id = s2.id - 1 And s1.id = s3.id + 1) Or
    (s1.id = s2.id + 1 And s2.id = s3.id + 1) 
)
Order By s1.id;
```

### [607. 销售员](https://leetcode.cn/problems/sales-person/)



```mysql
# 先找到向“RED”公司销售的，然后再在SalesPerson表中Not In
Select s.name 
From SalesPerson s 
Where s.sales_id Not In
    (
        Select o.sales_id
        From Orders o 
        Left Join Company c 
        On o.com_id = c.com_id
        Where c.name = "RED"
    )
;
```



### [608. 树节点](https://leetcode.cn/problems/tree-node/) 🔖

表：`Tree`

```
+-------------+------+
| Column Name | Type |
+-------------+------+
| id          | int  |
| p_id        | int  |
+-------------+------+
id 是该表中具有唯一值的列。
该表的每行包含树中节点的 id 及其父节点的 id 信息。
给定的结构总是一个有效的树。
```

树中的每个节点可以是以下三种类型之一：

- **"Leaf"**：节点是叶子节点。
- **"Root"**：节点是树的根节点。
- **"lnner"**：节点既不是叶子节点也不是根节点。

编写一个解决方案来报告树中每个节点的类型。

以 **任意顺序** 返回结果表。

#### 方法 1：使用 `UNION`



```mysql
SELECT
    id, 'Root' AS Type
FROM
    tree
WHERE
    p_id IS NULL

UNION

SELECT
    id, 'Leaf' AS Type
FROM
    tree
WHERE
    id NOT IN (SELECT DISTINCT
            p_id
        FROM
            tree
        WHERE
            p_id IS NOT NULL)
        AND p_id IS NOT NULL

UNION

SELECT
    id, 'Inner' AS Type
FROM
    tree
WHERE
    id IN (SELECT DISTINCT
            p_id
        FROM
            tree
        WHERE
            p_id IS NOT NULL)
        AND p_id IS NOT NULL
ORDER BY id;
```

#### 方法 II：使用流控制语句 `CASE`

```mysql
SELECT
    id AS `Id`,
    CASE
        WHEN tree.id = (SELECT atree.id FROM tree atree WHERE atree.p_id IS NULL)
          THEN 'Root'
        WHEN tree.id IN (SELECT atree.p_id FROM tree atree)
          THEN 'Inner'
        ELSE 'Leaf'
    END AS Type
FROM
    tree
ORDER BY `Id`
;
```

#### 方法 III；使用 `IF` 函数

```sql
SELECT
    atree.id,
    IF(ISNULL(atree.p_id),
        'Root',
        IF(atree.id IN (SELECT p_id FROM tree), 'Inner','Leaf')) Type
FROM
    tree atree
ORDER BY atree.id
```



### [626. 换座位](https://leetcode.cn/problems/exchange-seats/) 🔖



```sql
# Write your MySQL query statement below

Select Rank() Over(Order By (id-1)^1) As id, Student
From seat;
```





### [627. 变更性别](https://leetcode.cn/problems/swap-salary/)



```mysql
Update Salary
Set 
    sex = Case sex 
        When 'm' Then 'f'
        Else 'm'
    End;
```



### [1084. 销售分析III](https://leetcode.cn/problems/sales-analysis-iii/)



```mysql

Select product_id, product_name
From Product 
Where product_id In
    (
        Select product_id
        From Sales
        Group BY product_id
        Having sum(sale_date between "2019-01-01" and "2019-03-31") = count(sale_date)
    )
;
```



### [1141. 查询近30天活跃用户数](https://leetcode.cn/problems/user-activity-for-the-past-30-days-i/)



```mysql
select activity_date'day',count(distinct user_id)'active_users'
from activity
where activity_date between '2019-06-28' and '2019-07-27'
group by activity_date;
```



### [1179. 重新格式化部门表](https://leetcode.cn/problems/reformat-department-table/)

```mysql
select id,
    sum(case month when 'Jan' then revenue end) as Jan_Revenue,
    sum(case month when 'Feb' then revenue end) as Feb_Revenue,
    sum(case month when 'Mar' then revenue end) as Mar_Revenue,
    sum(case month when 'Apr' then revenue end) as Apr_Revenue,
    sum(case month when 'May' then revenue end) as May_Revenue,
    sum(case month when 'Jun' then revenue end) as Jun_Revenue,
    sum(case month when 'Jul' then revenue end) as Jul_Revenue,
    sum(case month when 'Aug' then revenue end) as Aug_Revenue,
    sum(case month when 'Sep' then revenue end) as Sep_Revenue,
    sum(case month when 'Oct' then revenue end) as Oct_Revenue,
    sum(case month when 'Nov' then revenue end) as Nov_Revenue,
    sum(case month when 'Dec' then revenue end) as Dec_Revenue
from Department
group by id
```



### [1393. 股票的资本损益](https://leetcode.cn/problems/capital-gainloss/)



```mysql
Select stock_name,
Sum(Case When operation='buy' then -price Else price End) As capital_gain_loss
From Stocks
Group By stock_name;
```



### [1484. 按日期分组销售产品](https://leetcode.cn/problems/group-sold-products-by-the-date/)

```mysql
Select
    sell_date,
    Count(Distinct product) As num_sold,
    Group_concat(Distinct product Order By product Separator ',') As products
From Activities
Group By sell_date
Order By sell_date;
```



### [1661. 每台机器的进程平均运行时间](https://leetcode.cn/problems/average-time-of-process-per-machine/)

```mysql
Select a1.machine_id,
round(avg(a2.timestamp - a1.timestamp), 3) As processing_time
From Activity a1 Join  Activity a2
On a1.machine_id = a2.machine_id
And a1.process_id = a2.process_id
And a1.activity_type = 'start'
And a2.activity_type = 'end'
Group By machine_id;
```



### [1795. 每个产品在不同商店的价格](https://leetcode.cn/problems/rearrange-products-table/)



```mysql
# Write your MySQL query statement below

SELECT product_id, 'store1' store, store1 price FROM products WHERE store1 IS NOT NULL
UNION
SELECT product_id, 'store2' store, store2 price FROM products WHERE store2 IS NOT NULL
UNION
SELECT product_id, 'store3' store, store3 price FROM products WHERE store3 IS NOT NULL;
```



### [1873. 计算特殊奖金](https://leetcode.cn/problems/calculate-special-bonus/)

```mysql
Select employee_id,
If(Mod(employee_id, 2) != 0 And Left(name, 1) != 'M', salary, 0) bonus 
From Employees
Order By employee_id;
```



### [1965. 丢失信息的雇员](https://leetcode.cn/problems/employees-with-missing-information/)



```mysql
Select e.employee_id
From employees e 
Left Join salaries s
On e.employee_id = s.employee_id
Where s.salary Is Null

Union

Select s.employee_id
From salaries s 
Left Join employees e
On s.employee_id = e.employee_id
Where e.name Is Null
Order By employee_id;
```



