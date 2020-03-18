# 基于java的内存型数据库

本项目是为完成湖南大学研究生数据库大作业，作者花了两天时间完成，后续不再进行维护

数据库支持的数据类型有：`number string boolean`

使用 javacc 作为词法分析器，数据库本题实现为简单的数据结构（未使用B+树），用了一点点设计模式

支持的sql语句有：

    create table table_name
    create colum colum_name colum_type in table_name
    
    drop table_name colum_name
    
    insert into table_name values (val1, val2, val3,...)
    
    update table_name set colum_name=val1, colum_name=val2 where
    
    select *|colum_name from table_name where 

