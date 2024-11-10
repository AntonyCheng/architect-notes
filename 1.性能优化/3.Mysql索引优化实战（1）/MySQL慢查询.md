# 概念

MySQL的慢查询，全名是**慢查询日志**，是MySQL提供的一种日志记录，用来记录在MySQL中**响应时间超过阀值**的语句。

具体环境中，运行时间超过long_query_time值的SQL语句，则会被记录到慢查询日志中。

long_query_time的默认值为10，意思是记录运行10秒以上的语句。

默认情况下，MySQL数据库并不启动慢查询日志，需要手动来设置这个参数。

当然，**如果不是调优需要的话，一般不建议启动该参数**，因为开启慢查询日志会或多或少带来一定的性能影响。

慢查询日志支持将日志记录写入文件和数据库表。

# 参数

slow_query_log：是否开启慢查询日志，**1表示开启，0表示关闭**。

log-slow-queries ：旧版（5.6以下版本）MySQL数据库慢查询日志存储路径。可以不设置该参数，系统则会默认给一个缺省的文件host_name-slow.log

slow-query-log-file：新版（5.6及以上版本）MySQL数据库慢查询日志存储路径。可以不设置该参数，系统则会默认给一个缺省的文件host_name-slow.log

long_query_time：慢查询阈值，当查询时间多于设定的阈值时，记录日志。

log_queries_not_using_indexes：未使用索引的查询也被记录到慢查询日志中（可选项）。

log_output：日志存储方式。log_output='FILE'表示将日志存入文件，默认值是'FILE'。log_output='TABLE'表示将日志存入数据库。

# 配置

## slow_query_log

默认情况下slow_query_log的值为OFF，表示慢查询日志是禁用的，可以通过设置slow_query_log的值来开启，如下所示：

```shell
mysql> show variables  like '%slow_query_log%';
 +---------------------+-----------------------------------------------+
 | Variable_name       | Value                                         |
 +---------------------+-----------------------------------------------+
 | slow_query_log      | OFF                                           |
 | slow_query_log_file | /home/WDPM/MysqlData/mysql/DB-Server-slow.log |
 +---------------------+-----------------------------------------------+
 2 rows in set (0.00 sec)
 
mysql> set global slow_query_log=1;
 Query OK, 0 rows affected (0.09 sec)
```

使用set global slow_query_log=1开启了慢查询日志只对当前数据库生效，MySQL重启后则会失效。

如果要永久生效，就必须修改配置文件my.cnf（其它系统变量也是如此）。

my.cnf要增加或修改参数slow_query_log 和slow_query_log_file，如下所示

slow_query_log = 1

slow_query_log_file = /tmp/mysql_slow.log

然后重启MySQL服务器。

## slow_query_log_file

这个参数用于指定慢查询日志的存放路径，缺省情况是host_name-slow.log文件。

```shell
mysql> show variables like 'slow_query_log_file';
 +---------------------+-----------------------------------------------+
 | Variable_name       | Value                                         |
 +---------------------+-----------------------------------------------+
 | slow_query_log_file | /home/WDPM/MysqlData/mysql/DB-Server-slow.log |
 +---------------------+-----------------------------------------------+
 1 row in set (0.00 sec)
```

## long_query_time

开启了慢查询日志后，什么样的SQL才会记录到慢查询日志里面呢？

这个是由参数long_query_time控制，默认情况下long_query_time的值为10秒，可以使用命令修改，也可以在my.cnf参数里面修改。

关于运行时间正好等于long_query_time的情况，并不会被记录下来。

也就是说，在mysql源码里是判断大于long_query_time，而非大于等于。

从MySQL 5.1开始，long_query_time开始以微秒记录SQL语句运行时间，之前仅用秒为单位记录。

如果记录到表里面，只会记录整数部分，不会记录微秒部分。

```shell
mysql> show variables like 'long_query_time%';
 +-----------------+-----------+
 | Variable_name   | Value     |
 +-----------------+-----------+
 | long_query_time | 10.000000 |
 +-----------------+-----------+
 1 row in set (0.00 sec)
 
mysql> set global long_query_time=4;
 Query OK, 0 rows affected (0.00 sec)
 
mysql> show variables like 'long_query_time';
 +-----------------+-----------+
 | Variable_name   | Value     |
 +-----------------+-----------+
 | long_query_time | 10.000000 |
 +-----------------+-----------+
 1 row in set (0.00 sec)
```

如上所示，我修改了变量long_query_time，但是查询变量long_query_time的值还是10，难道没有修改到呢？

注意：使用命令 set global long_query_time=4修改后，需要重新连接或新开一个会话才能看到修改值。

用show variables like 'long_query_time'查看是当前会话的变量值。

也可以不用重新连接会话，而是用show global variables like 'long_query_time';。

## log_output

log_output参数指定日志的存储方式。

log_output='FILE'表示将日志存入文件，默认值也是'FILE'。

log_output='TABLE'表示将日志存入数据库，这样日志信息就会被写入到mysql.slow_log表中。

同时也支持两种日志存储方式，配置的时候以逗号隔开即可，如：log_output='FILE,TABLE'。

日志记录到系统的专用日志表中，要比记录到文件耗费更多的系统资源。

因此对于需要启用慢查询日志，又需要能够获得更高的系统性能，那么**建议优先记录到文件**。

```shell
mysql> show variables like '%log_output%';
 +---------------+-------+
 | Variable_name | Value |
 +---------------+-------+
 | log_output    | FILE  |
 +---------------+-------+
 1 row in set (0.00 sec)
 
mysql> set global log_output='TABLE';
 Query OK, 0 rows affected (0.00 sec)
 
mysql> show variables like '%log_output%';
 +---------------+-------+
 | Variable_name | Value |
 +---------------+-------+
 | log_output    | TABLE |
 +---------------+-------+
 1 row in set (0.00 sec)
 
mysql> select sleep(5) ;
 +----------+
 | sleep(5) |
 +----------+
 |        0 |
 +----------+
 1 row in set (5.00 sec)
 
mysql>
 
mysql> select * from mysql.slow_log;
 +---------------------+---------------------------+------------+-----------+-----------+---------------+----+----------------+-----------+-----------+-----------------+-----------+
 | start_time          | user_host                 | query_time | lock_time | rows_sent | rows_examined | db | last_insert_id | insert_id | server_id | sql_text        | thread_id |
 +---------------------+---------------------------+------------+-----------+-----------+---------------+----+----------------+-----------+-----------+-----------------+-----------+
 | 2016-06-16 17:37:53 | root[root] @ localhost [] | 00:00:03   | 00:00:00  |         1 |             0 |    |              0 |         0 |         1 | select sleep(3) |         5 |
 | 2016-06-16 21:45:23 | root[root] @ localhost [] | 00:00:05   | 00:00:00  |         1 |             0 |    |              0 |         0 |         1 | select sleep(5) |         2 |
 +---------------------+---------------------------+------------+-----------+-----------+---------------+----+----------------+-----------+-----------+-----------------+-----------+
 2 rows in set (0.00 sec)
```

## log_queries_not_using_indexes

该系统变量指定**未使用索引的查询**也被记录到慢查询日志中（可选项）。

如果调优的话，建议开启这个选项。

另外，开启了这个参数，其实使用full index scan的SQL也会被记录到慢查询日志。

```shell
mysql> show variables like 'log_queries_not_using_indexes';
 +-------------------------------+-------+
 | Variable_name                 | Value |
 +-------------------------------+-------+
 | log_queries_not_using_indexes | OFF   |
 +-------------------------------+-------+
 1 row in set (0.00 sec)
 
mysql> set global log_queries_not_using_indexes=1;
 Query OK, 0 rows affected (0.00 sec)
 
mysql> show variables like 'log_queries_not_using_indexes';
 +-------------------------------+-------+
 | Variable_name                 | Value |
 +-------------------------------+-------+
 | log_queries_not_using_indexes | ON    |
 +-------------------------------+-------+
 1 row in set (0.00 sec)
```

## log_slow_admin_statements

这个系统变量表示，是否将慢管理语句例如ANALYZE TABLE和ALTER TABLE等记入慢查询日志。

```shell
mysql> show variables like 'log_slow_admin_statements';
 +---------------------------+-------+
 | Variable_name             | Value |
 +---------------------------+-------+
 | log_slow_admin_statements | OFF   |
 +---------------------------+-------+
 1 row in set (0.00 sec)
```

## slow_queries

```shell
mysql> show global status like '%Slow_queries%';
 +---------------+-------+
 | Variable_name | Value |
 +---------------+-------+
 | Slow_queries  | 2104  |
 +---------------+-------+
 1 row in set (0.00 sec)
```

另外，还有log_slow_slave_statements 和 --log-short-format 参数，可到MySQL网站了解。

# mysqldumpslow工具

在生产环境中，如果要手工分析日志，查找、分析SQL，显然是个体力活。

MySQL提供了日志分析工具mysqldumpslow

查看mysqldumpslow的帮助信息：

```shell
[root@DB-Server ~]# mysqldumpslow --help
 Usage: mysqldumpslow [ OPTS... ] [ LOGS... ]
 
Parse and summarize the MySQL slow query log. Options are
 
  --verbose    verbose
  --debug      debug
  --help       write this text to standard output
 
  -v           verbose
  -d           debug
  -s ORDER     what to sort by (al, at, ar, c, l, r, t), 'at' is default（排序方式）
                 al: average lock time（平均锁定时间）
                 ar: average rows sent（平均返回记录数）
                 at: average query time（平均查询时间）
                  c: count（访问计数）
                  l: lock time（锁定时间）
                  r: rows sent（返回记录）
                  t: query time（查询时间）
   -r           reverse the sort order (largest last instead of first)
   -t NUM       just show the top n queries（返回前面n条数据）
   -a           don't abstract all numbers to N and strings to 'S'
   -n NUM       abstract numbers with at least n digits within names
   -g PATTERN   grep: only consider stmts that include this string（正则匹配模式，大小写不敏感）
   -h HOSTNAME  hostname of db server for *-slow.log filename (can be wildcard),
                default is '*', i.e. match all
   -i NAME      name of server instance (if using mysql.server startup script)
   -l           don't subtract lock time from total time
```

比如，得到返回记录集最多的10个SQL。

```shell
mysqldumpslow -s r -t 10 /database/mysql/mysql06_slow.log
```

得到访问次数最多的10个SQL。

```shell
mysqldumpslow -s c -t 10 /database/mysql/mysql06_slow.log
```

得到按照时间排序的前10条里面含有左连接的查询语句。

```shell
mysqldumpslow -s t -t 10 -g “left join” /database/mysql/mysql06_slow.log
```

另外建议在使用这些命令时结合 | 和more 使用 ，否则有可能出现刷屏的情况。

```shell
mysqldumpslow -s r -t 20 /mysqldata/mysql/mysql06-slow.log | more
```

