DROP DATABASE if EXISTS WeGo;
CREATE DATABASE WeGo CHARACTER SET utf8;
USE WeGo;

CREATE TABLE  user_info(
    user_id     int PRIMARY KEY AUTO_INCREMENT   COMMENT '用户标识符',
    user_name   varchar(32) NOT NULL UNIQUE      COMMENT '用户名',
    pwd         varchar(32) NOT NULL             COMMENT '密码'
);
CREATE TABLE user_card(
    user_id     int                              COMMENT '用户标识符',
    card_id     varchar(32)     PRIMARY KEY      COMMENT '卡号',
    user_phone  varchar(11)                      COMMENT '用户预留手机号',      
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE item(
    item_id     int PRIMARY KEY                  COMMENT '商品标识符，nfc标签里的编号数据',
    item_name   varchar(32) NOT NULL             COMMENT '商品名称',
    item_num    int DEFAULT 0                    COMMENT '商品余量',
    item_price  double                           COMMENT '商品单价'
);


CREATE TABLE order_info(
    order_id    int PRIMARY KEY AUTO_INCREMENT   COMMENT '订单标识符',
    user_id     int                              COMMENT '用户标识符', 
    order_price double                           COMMENT '订单价格',
    order_time  datetime                         COMMENT '订单时间',
    order_status int                             COMMENT '订单支付状态',
    FOREIGN KEY (user_id) REFERENCES user_info(user_id)
);

CREATE TABLE order_item(
    order_id int                                 COMMENT '订单标识符',
    item_id  int                                 COMMENT '商品标识符',
    item_num int                                 COMMENT '商品数量',
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES order_info(order_id),
    FOREIGN KEY (item_id)  REFERENCES item(item_id)
);

INSERT INTO user_info values('1','foo@example.com','1234567890');
INSERT INTO user_info values('2','123@example.com','1234567890');

INSERT INTO item values('1','可口可乐','100','3.5');
INSERT INTO item values('2','茶泡饭','123','5.5');
INSERT INTO item values('3','章光101','20','100');
INSERT INTO item values('4','原谅帽','4','233');
INSERT INTO item values('5','拖鞋','10','15');

INSERT INTO order_info values('1','1','7','2017-7-9 14:50:50','0');
INSERT INTO order_info values('2','1','11','2017-7-9 14:50:50','0');
INSERT INTO order_info values('3','1','100','2017-7-9 14:50:50','0');
INSERT INTO order_info values('4','1','15','2017-7-9 14:50:50','0');

INSERT INTO order_item values('1','1','2');
INSERT INTO order_item values('2','2','2');
INSERT INTO order_item values('3','3','1');
INSERT INTO order_item values('4','5','1');

INSERT INTO user_card values('1','123123812391837444','12345678901');
INSERT INTO user_card values('1','123123123123918373','12345678901');
INSERT INTO user_card values('1','123124543564391837','12345678901');


DROP DATABASE IF EXISTS  bank;
CREATE DATABASE bank;
USE bank;
CREATE TABLE account(
    
    card_id   varchar(32)    PRIMARY KEY         COMMENT '用户标识符',
    user_name INT                                COMMENT '用户名',
    user_phone varchar(32)                       COMMENT '用户预留手机号',  
    money     DOUBLE                             COMMENT '余额'
);

INSERT INTO account values('123123812391837444','123123','12345678901','10000.0');
INSERT INTO account values('123123123123918373','121223','12345678901','10000.0');
INSERT INTO account values('123124543564391837','123123','12345678901','10000.0');
INSERT INTO account values('123123834534683723','123123','12345678901','10000.0');
INSERT INTO account values('123123813463141372','123123','12345678901','10000.0');
INSERT INTO account values('123123812312665737','123123','12345678901','10000.0');

INSERT INTO account values('123123812312665755','123123','12345678911','10000.0');

INSERT INTO account values('123123812312665766','233233','12345678922','20000.0');

INSERT INTO account values('123123812312665777','456456','12345678933','30000.0');

INSERT INTO account values('123123812312665788','111111','12345678944','40000.0');

INSERT INTO account values('123123812312665799','222222','12345678955','50000.0');

INSERT INTO account values('123123812312665756','333333','12345678966','60000.0');

INSERT INTO account values('123123812312665757','444444','12345678977','70000.0');

INSERT INTO account values('123123812312665758','555555','12345678988','80000.0');

INSERT INTO account values('123123812312665759','666666','12345678999','90000.0');

INSERT INTO account values('123123812312665761','777777','12345678912','110000.0');

INSERT INTO account values('123123812312665762','888888','12345678923','120000.0');

INSERT INTO account values('123123812312665763','999999','12345678934','130000.0');

INSERT INTO account values('123123812312665764','000000','12345678945','140000.0');

INSERT INTO account values('123123812312665765','696969','12345678956','150000.0');

INSERT INTO account values('123123812312665767','969696','12345678967','160000.0');
