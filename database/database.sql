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
    card_id     int     PRIMARY KEY              COMMENT '卡号',
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

DROP DATABASE IF EXISTS  bank;
CREATE DATABASE bank;
USE bank;
CREATE TABLE account(
    
    card_id   INT    PRIMARY KEY                 COMMENT '用户标识符',
    user_name INT                                COMMENT '用户名',
    user_phone varchar(11)                       COMMENT '用户预留手机号',  
    money     DOUBLE                             COMMENT '余额'
);