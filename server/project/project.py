# -*- coding:utf-8 templates:utf-8 -*-

from flask import Flask, session, redirect, render_template, request, jsonify
import sys
import time
import traceback
import urllib2, json, urllib
import re
from os import urandom
import os
import platform
from sqlalchemy.orm import sessionmaker
from sqlalchemy import Column, ForeignKey, String, create_engine, Integer, DATETIME, TEXT, BIGINT, or_, desc
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.dialects.mysql import DOUBLE

# 系统内部全局变量
MANAGER = ['123456']
PASSWORD = ['123456']
MY_CARD_ID = '1234567890'

SERVER_MYSQL_ACCOUNT = 'root'
SERVER_MYSQL_PASSWORD = 'mysqlpasswd'

# app配置
app = Flask(__name__, template_folder='templates')

app.secret_key = urandom(24)




if platform.system() == 'Linux':
    app.debug = False
    app.config['error_log_file_path'] = os.path.dirname(os.path.abspath(__file__))+'/log/error_log.txt'
    app.config['op_log_file_path'] = os.path.dirname(os.path.abspath(__file__))+'/log/op_log.txt'
    app.config['database_connect'] = "mysql://root:mysqlpasswd@localhost:3306/WeGo?charset=utf8"
    app.config['database_connect_bank'] = "mysql://root:mysqlpasswd@localhost:3306/bank?charset=utf8"

else:
    app.debug = True
    app.config['error_log_file_path'] = 'F:error_log.txt'
    app.config['op_log_file_path'] = 'F:op_log.txt'
    app.config['database_connect'] = "mysql://root:@localhost:3306/WeGo?charset=utf8"
    app.config['database_connect_bank'] = "mysql://root:@localhost:3306/bank?charset=utf8"

app.config['error_log_file'] = open(app.config['error_log_file_path'], 'a+')
app.config['op_log_file'] = open(app.config['op_log_file_path'], 'a+')

# 数据库引擎初始化
Base = declarative_base()
## 服务端
DBEngine = create_engine(app.config['database_connect'])
DBSession = sessionmaker(bind=DBEngine)
## 银行模拟
DBEngine_bank = create_engine(app.config['database_connect_bank'])
DBSession_bank = sessionmaker(bind=DBEngine_bank)

defaultEncoding = "utf-8"
reload(sys)
sys.setdefaultencoding(defaultEncoding)


# start 数据库类

# 移动端用户
class User(Base):
    # 表名
    __tablename__ = 'user_info'
    # 字段
    user_id = Column(Integer, primary_key=True, autoincrement=True)
    user_name = Column(String, unique=True)
    pwd = Column(String)

    # 构造函数
    def __init__(self, user_name, pwd):
        '''
        :param user_name: str
        :param pwd: str
        '''
        self.user_name = user_name
        self.pwd = pwd


# 银行卡
class Card(Base):
    # 表名
    __tablename__ = 'user_card'
    # 字段
    user_id = Column(Integer)
    card_id = Column(Integer, primary_key=True)
    user_phone = Column(String)

    # 构造函数
    def __init__(self, user_id, card_id, user_phone):
        '''
        :param user_id: str
        :param card_id: str
        :param user_phone: str
        '''
        self.user_id = int(user_id)
        self.card_id = card_id
        self.user_phone = user_phone


# 商品
class Item(Base):
    # 表名
    __tablename__ = 'item'
    # 字段
    item_id = Column(Integer, primary_key=True)
    item_name = Column(String)
    item_num = Column(Integer)
    item_price = Column(DOUBLE)

    # 构造函数
    def __init__(self, item_id, item_name, item_num, item_price):
        '''
        :param item_id:  str
        :param item_name: str
        :param item_num: str
        :param item_price: str
        '''
        self.item_id = int(item_id)
        self.item_name = item_name
        self.item_num = int(item_num)
        self.item_price = float(item_price)

    # 成员函数
    def changePrice(self, new_price):
        '''
        :param new_price: str
        :return: boolean
        '''
        self.item_price = float(new_price)
        return True

    def changeNum(self, num):
        '''
        :param num: str
        :return: boolean
        '''
        self.item_num = self.item_num + float(num)
        return True


# 订单
class Order(Base):
    # 表名
    __tablename__ = 'order_info'
    # 字段
    order_id = Column(Integer, primary_key=True, autoincrement=True)
    user_id = Column(Integer)
    order_price = Column(DOUBLE)
    order_time = Column(DATETIME)
    order_status = Column(Integer)

    # 构造函数
    def __init__(self, user_id, order_price):
        '''
        :param user_id: str
        :param order_price: str
        '''
        self.user_id = user_id
        self.order_price = order_price
        self.order_time = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(time.time()))
        self.order_status = 0

    # 成员函数
    def payed(self):
        '''
        :return: boolean
        '''
        self.order_status = 1
        return True


# order_item
class Order_item(Base):
    # 表名
    __tablename__ = 'order_item'
    # 字段
    order_id = Column(Integer, primary_key=True)
    item_id = Column(Integer, primary_key=True)
    item_num = Column(Integer)

    # 构造函数
    def __init__(self, order_id, item_id, item_num):
        '''
        :param order_id:  str
        :param item_id:  str
        :param item_num: str
        '''
        self.order_id = int(order_id)
        self.item_id = int(item_id)
        self.item_num = int(item_num)


class Account(Base):
    # 表名
    __tablename__ = 'account'
    # 字段
    card_id = Column(Integer, primary_key=True)
    user_name = Column(Integer)
    money = Column(DOUBLE)
    user_phone = Column(String)

    # 构造函数
    def __init__(self, card_id, user_name, money, user_phone):
        '''
        :param card_id:  str
        :param user_name:  str
        :param money:  str
        :param user_phone: str
        '''
        self.card_id = card_id
        self.user_name = user_name
        self.money = float(money)
        self.user_phone = user_phone


# end 数据库类

# start 操作方法

# 注册
def register(user_name, pwd):
    '''
    :param user_name: str
    :param pwd: str
    :return: int
    '''
    localSession = DBSession()
    try:
        user = User(user_name=user_name, pwd=pwd)
        localSession.add(user)
        localSession.commit()
        id = user.user_id
        localSession.close()
        return id
    except:
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return -1


# 检查登录
def checkLogin(user_name, pwd):
    '''
    :param user_name: str
    :param pwd: str
    :return: int
    '''
    localSession = DBSession()
    try:
        user = localSession.query(User).filter(User.user_name == user_name, User.pwd == pwd).one()
        id = user.user_id
        localSession.close()
        return id
    except:
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return -1


# 管理员登录
def checkManagerLogin(id, pwd):
    '''
    :param id: str
    :param pwd: str
    :return: boolean
    '''
    try:
        for index, item in enumerate(MANAGER):
            if item == id and PASSWORD[index] == pwd:
                return True
        return False
    except:
        traceback.print_exc(file=app.config['error_log_file'])
        return False


# 增加银行卡
def addCard(user_id, pwd, card_id, user_phone):
    '''
    :param id: str
    :param pwd: str
    :param card_id: str
    :return: boolean
    '''
    localSession = DBSession()
    try:
        localSession.query(User).filter(User.user_id == int(user_id),
                                        User.pwd == pwd).one()  # if user not exist, it will throw a except

        if validCard(card_id=card_id, user_phone=user_phone) == False:
            return False
        print '123'
        card = Card(user_id=int(user_id), card_id=card_id, user_phone=user_phone)
        localSession.add(card)
        localSession.commit()
        localSession.close()
        return True
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False

def getCardList(user_id,pwd):
    localSession = DBSession()
    try:
        res = []
        localSession.query(User).filter(User.user_id==int(user_id),User.pwd==pwd).one()
        cardList = localSession.query(Card).filter(Card.user_id==int(user_id))
        ''':type :list[Card]'''

        for card in cardList:
            c = {}
            c['card_id']= card.card_id
            c['user_phone'] = card.user_phone
            res.append(c)
        localSession.close()
        return res
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        return None

def removeUserCard(user_id, pwd, card_id):
    localSession = DBSession()
    try:
        localSession.query(User).filter(User.user_id==user_id, User.pwd==pwd).one()
        card = localSession.query(Card).filter(Card.card_id==card_id, Card.user_id==user_id).one()
        localSession.delete(card)
        localSession.commit()
        localSession.close()
        return True
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False

# 检索用户的订单记录
def getOrderList(user_id, pwd):
    '''
    :param user_id: str
    :return: list[]
    '''

    localSession = DBSession()
    try:
        localSession.query(User).filter(User.user_id == int(user_id),
                                        User.pwd == pwd).one()  # if user not exist, it will throw a except
        list = []
        orderList = localSession.query(Order).filter(Order.user_id == int(user_id))
        ''':type :list[Order]'''
        for item in orderList:
            order = {}
            order['order_id'] = item.order_id
            order['user_id'] = item.user_id
            order['order_time'] = str(item.order_time)
            print order['order_time']
            order['order_price'] = str(round(item.order_price, 2))
            if item.order_status != 1:
                order['order_status'] = '未支付'
            else:
                order['order_status'] = '已支付'
            list.append(order)
        localSession.close()
        return list
    except:
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return None


# 增加新订单
def addNewOrder(user_id, pwd, itemList):
    '''
    :param user_id: str
    :param pwd: str
    :param totalPrice: str
    :param itemList: list[]
    :return: boolean
    '''
    localSession = DBSession()
    try:
        totalPrice = 0
        localSession.query(User).filter(User.user_id == int(user_id),
                                        User.pwd == pwd).one()  # if user not exist, it will throw a except
        for item in itemList:
            i = localSession.query(Item).filter(Item.item_id==int(item['itemId'])).one()
            totalPrice = totalPrice + i.item_price * int(item['num'])# sum up the price
            if int(i.item_num)<int(item['num']): # check the remain
                return False

        order = Order(user_id=user_id, order_price=totalPrice)
        localSession.add(order)
        localSession.commit()
        id = order.order_id
        print "order_status"
        print order.order_status
        for item in itemList:
            order_item = Order_item(order_id=id, item_id=int(item['itemId']), item_num=int(item['num']))
            localSession.add(order_item)
            localSession.commit()
        localSession.close()
        return id
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False


# 获取订单详情
def getOrderDetail(user_id, pwd, order_id):
    '''
    :param user_id: str
    :param pwd:  str
    :param order_id:  str
    :return: list[]
    '''
    localSession = DBSession()
    try:
        localSession.query(User).filter(User.user_id == int(user_id),
                                        User.pwd == pwd).one()  # if user not exist, it will throw a except

        print order_id
        list = []
        order_itemList = localSession.query(Order_item).filter(Order_item.order_id == order_id)
        ''':type :list[Order_item]'''
        for item in order_itemList:
            order_item = {}
            order_item['item_id'] = item.item_id
            order_item['item_num'] = item.item_num
            order_item['item_name'] = localSession.query(Item.item_name).filter(Item.item_id == order_item['item_id']).one()
            list.append(order_item)
        localSession.close()
        return list
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return None


# 支付订单
def payOrder(user_id, pwd, order_id):
    localSession = DBSession()
    try:
        localSession.query(User).filter(User.user_id == user_id, User.pwd == pwd).one()

        order = localSession.query(Order).filter(Order.order_id == order_id).one()
        ''':type :Order'''
        cardList = localSession.query(Card).filter(Card.user_id == user_id)
        ''':type :list[Card]'''
        for card in cardList:
            if transfer(source_card=card.card_id, destination_card=MY_CARD_ID, money=order.order_price) == True:
                break

        itemList = localSession.query(Order_item).filter(Order_item.order_id == order_id)
        for item in itemList:
            target = localSession.query(Item).filter(Item.item_id == item.item_id).one()
            ''':type :Item'''
            target.item_num = target.item_num - item.item_num
            localSession.commit()

        order.order_status = 1
        localSession.commit()
        localSession.close()
        return True
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False


# 增加货物
def addNewItem(id, name, num, price):
    localSession = DBSession()
    try:
        item = Item(item_id=int(id), item_name=name, item_num=int(num), item_price=float(price))
        localSession.add(item)
        localSession.commit()
        localSession.close()
        return True
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False


# 增加库存
def addItemNum(id, num):
    localSession = DBSession()
    try:
        item = localSession.query(Item).filter(Item.item_id == int(id)).one()
        ''':type :Item'''
        item.item_num = int(item.item_num) + int(num)
        localSession.commit()
        localSession.close()
        return True
    except:
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False

def changeItemPrice(id,price):
    localSession = DBSession()
    try:
        if int(price)<0:
            return False
        item = localSession.query(Item).filter(Item.item_id==int(id)).one()
        ''':type :Item'''
        item.changePrice(new_price=float(price))
        localSession.commit()
        localSession.close()
        return True
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False

# 清理库存
def clearItemNum(id):
    localSession = DBSession()
    try:
        item = localSession.query(Item).filter(Item.item_id==int(id)).one()
        item.item_num = 0
        localSession.commit()
        localSession.close()
        return True
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False
# 获取商品列表
def getItemList(page=0):
    localSession = DBSession()
    try:
        itemList = localSession.query(Item).offset(int(page) * 20).limit(21)
        ''':type :list[Item]'''
        list = []
        for item in itemList:
            tmp = {}
            tmp['item_id'] = item.item_id
            tmp['item_num'] = item.item_num
            tmp['item_name'] = item.item_name
            tmp['item_price'] = str(round(item.item_price, 2))
            list.append(tmp)
        localSession.close()
        return list
    except:
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False


# end   操作方法

# start 银行接口
# 校验银行卡有效性
def validCard(card_id, user_phone):
    localSession = DBSession_bank()
    try:
        localSession.query(Account).filter(Account.card_id == card_id, Account.user_phone == user_phone).one()
        return True
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        return False


def transfer(source_card, destination_card, money):
    '''
    :param source_card:  str
    :param destination_card: str
    :param money: int
    :return: boolean
    '''
    localSession = DBSession_bank()
    try:
        source_account = localSession.query(Account).filter(Account.card_id == int(source_card)).one()
        ''':type :Account'''
        if source_account.money < money:
            return False
        else:
            destination_account = localSession.query(Account).filter(Account.card_id == int(destination_card)).one()
            ''':type :Account'''
            source_account.money = source_account.money - money
            destination_account.money = destination_account.money - money
            localSession.commit()
            return True
    except:
        traceback.print_exc(file=app.config['error_log_file'])
        localSession.close()
        return False


# end 银行接口
@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/login', methods=['GET', 'POST'])
def check_login():
    try:
        # print 1
        if request.method == 'GET':
            # print 2
            user_name = request.args.get('name').encode('utf8')
            user_pwd = request.args.get('password').encode('utf8')
        else:
            # print 3
            user_name = request.form.get('name').encode('utf8')
            user_pwd = request.form.get('password').encode('utf8')

        print user_name
        print user_pwd

        val = checkLogin(user_name=user_name, pwd=user_pwd)
        print val
        res = {}
        res['status'] = False
        res['id'] = -1
        if val == -1:
            val = register(user_name=user_name, pwd=user_pwd)
            if val <= 0:
                return jsonify(res)
            else:
                res['status'] = True
                res['id'] = val
                return jsonify(res)
        else:
            res['status'] = True
            res['id'] = val
            return jsonify(res)
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        res = {}
        res['status'] = False
        res['id'] = -1
        return jsonify(res)


@app.route('/order-list', methods=['GET', 'POST'])
def get_order_list():
    '''
    :return:dict['status':boolean,
                 'data':list[dict[]]
    '''
    try:
        if request.method == 'GET':
            user_id = request.args.get('id').encode('utf8')
            user_pwd = request.args.get('password').encode('utf8')
        else:
            user_id = request.form.get('id').encode('utf8')
            user_pwd = request.form.get('password').encode('utf8')

        orderList = getOrderList(user_id=user_id, pwd=user_pwd)
        res = {}
        res['status'] = True
        res['data'] = orderList
        # print orderList
        return jsonify(res)

    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        res = {}
        res['status'] = False
        res['data'] = None
        return jsonify(res)


@app.route('/order-detail',methods=['GET','POST'])
def get_order_detail():
    '''
    :return:dict['status':boolean,
                 'data':list[dict[]]
    '''
    try:
        if request.method == 'GET':
            user_id = request.args.get('id').encode('utf8')
            user_pwd = request.args.get('password').encode('utf8')
            order_id = request.args.get('order_id').encode('utf8')
        else:
            user_id = request.form.get('id').encode('utf8')
            user_pwd = request.form.get('password').encode('utf8')
            order_id = request.form.get('order_id').encode('utf8')
        detail = getOrderDetail(user_id=user_id, pwd=user_pwd, order_id=order_id)
        res = {}
        res['status'] = True
        res['data'] = detail
        print detail
        return jsonify(res)

    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        res = {}
        res['status'] = False
        res['data'] = None
        return jsonify(res)

@app.route('/card-list', methods=['GET','POST'])
def card_list():
    '''
        :return:dict['status':boolean,
                     'data':list[dict[]]
        '''
    try:
        if request.method == 'GET':
            user_id = request.args.get('id').encode('utf8')
            user_pwd = request.args.get('password').encode('utf8')
            # order_id = request.args.get('order_id').encode('utf8')
        else:
            user_id = request.form.get('id').encode('utf8')
            user_pwd = request.form.get('password').encode('utf8')
            # order_id = request.form.get('order_id').encode('utf8')
        cardList = getCardList(user_id=user_id, pwd=user_pwd)
        res = {}
        res['status'] = True
        res['data'] = cardList
        print cardList
        return jsonify(res)

    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        res = {}
        res['status'] = False
        res['data'] = None
        return jsonify(res)

@app.route('/add-card', methods=['GET','POST'])
def add_card():
    '''
    :return:dict['status':boolean]
    '''
    try:
        if request.method == 'GET':
            user_id = request.args.get('id').encode('utf8')
            user_pwd = request.args.get('password').encode('utf8')
            card_id = request.args.get('card_id').encode('utf8')
            user_phone = request.args.get('phone_number').encode('utf8')
        else:
            user_id = request.form.get('id').encode('utf8')
            user_pwd = request.form.get('password').encode('utf8')
            card_id = request.form.get('card_id').encode('utf8')
            user_phone = request.form.get('phone_number').encode('utf8')
        val = addCard(user_id=user_id, pwd=user_pwd, card_id=card_id, user_phone=user_phone)
        res = {}
        res['status'] = val
        res['data'] = None
        print val
        return jsonify(res)

    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        res = {}
        res['status'] = False
        res['data'] = None
        return jsonify(res)


@app.route('/pay-order')
def pay_order():
    '''
    :return:dict['status':boolean,
                 'data':list[dict[]]
    '''
    try:
        user_id = request.args.get('id').encode('utf8')
        user_pwd = request.args.get('password').encode('utf8')
        order_id = request.args.get('order_id').encode('utf8')
        val = payOrder(user_id=user_id, pwd=user_pwd, order_id=order_id)
        res = {}
        res['status'] = val
        res['data'] = None
        print res
        return jsonify(res)

    except:
        traceback.print_exc(file=app.config['error_log_file'])
        res = {}
        res['status'] = False
        res['data'] = None
        return jsonify(res)


@app.route('/add-order', methods=['POST'])
def add_order():
    '''
    :return:dict['status':boolean,
                 'data':list[dict[]]
    '''
    try:
        user_id = request.form.get('id').encode('utf8')
        user_pwd = request.form.get('password').encode('utf8')

        item_list_str = request.form.get('itemList')
        item_list = request.form.getlist('itemList')

        #total_price = request.form.get('totalPrice').encode('utf8')
        # itemList = item
        # [{"itemId": 2001, "num": 2}, {"itemId": 101, "num": 1}]
        #print type(item_list_str)
        #print type
        itemList = eval(item_list_str)
        print itemList
        val = addNewOrder(user_id=user_id, pwd=user_pwd, itemList=itemList)
        if val == False:
            return jsonify({'status':False,'data':None})

        payOrder(user_id=user_id, pwd=user_pwd, order_id=val)

        res = {}
        res['status'] = True
        res['data'] = None
        print res
        return jsonify(res)

    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        res = {}
        res['status'] = False
        res['data'] = None
        return jsonify(res)

@app.route('/remove-card', methods=['GET','POST'])
def remove_card():
    try:
        if request.method == 'GET':
            user_id = request.args.get('id').encode('utf8')
            user_pwd = request.args.get('password').encode('utf8')
            card_id = request.args.get('card_id').encode('utf8')
        else:
            user_id = request.form.get('id').encode('utf8')
            user_pwd = request.form.get('password').encode('utf8')
            card_id = request.form.get('card_id').encode('utf8')
        val = removeUserCard(user_id=int(user_id), pwd=user_pwd,card_id=card_id)
        if val == True:
            return jsonify({'status':True, 'data':None})
        else:
            return jsonify({'status':False, 'data':None})
    except:
        traceback.print_exc()
        traceback.print_exc(file=app.config['error_log_file'])
        return jsonify({'status': False, 'data': None})

@app.route('/manage-login', methods=['GET', 'POST'])
def manage_login():
    if request.method == 'GET':
        return render_template('login.html')
    else:
        try:
            user = request.form.get('id').encode('utf8')
            pwd = request.form.get('pwd').encode('utf8')
            val = checkManagerLogin(id=user, pwd=pwd)
            res = {}
            if val == True:
                res['status'] = 0
                session['MANAGE'] = True
                return jsonify(res)
            else:
                res['status'] = -1
                return jsonify(res)
        except:
            return jsonify({'status': -1})


@app.route('/back', methods=['GET', 'POST'])
def back():
    if request.method == 'GET':
        if 'MANAGE' not in session:
            return redirect('/manage-login')
        active = {}
        if request.args.get('page'):
            active['currentPage'] = int(request.args.get('page').encode('utf8'))

        else:
            active['currentPage'] = 0
        itemList = getItemList(page=active['currentPage'])
        if active['currentPage'] == 0:
            active['pre'] = False
        else:
            active['pre'] = True

        if len(itemList)==21:
            active['next'] = True
            itemList.pop()
        else:
            active['next'] = False


        return render_template('item_list.html', itemList=itemList,active=active)
    else:
        try:
            if 'MANAGE' not in session:
                return jsonify({'status':False})
            method = request.form.get('method').encode('utf8')

            if method == 'add-new-item':
                id = request.form.get('id').encode('utf8')
                name = request.form.get('name').encode('utf8')
                num = int(request.form.get('num').encode('utf8'))
                price = float(request.form.get('price').encode('utf8'))
                if price < 0:
                    return jsonify({'status': -2})
                if num < 0:
                    return jsonify({'status': -3})

                val = addNewItem(id=id, name=name, num=num, price=price)
                if val == True:
                    return jsonify({'status': 0})
                else:
                    return jsonify({'status': -1})

            elif method == 'add-item-num':
                id = request.form.get('id').encode('utf8')
                num = request.form.get('num').encode('utf8')
                print num
                val = addItemNum(id=id, num=num)
                if val == True:
                    return jsonify({'status': 0})
                else:
                    return jsonify({'status': -1})

            elif method == 'change-item-price':
                id = request.form.get('id').encode('utf8')
                price = request.form.get('price').encode('utf8')
                val = changeItemPrice(id=int(id), price=float(price))
                if val == True:
                    return jsonify({'status': 0})
                else:
                    return jsonify({'status': -1})
            elif method == 'clear-item-num':
                id = request.form.get('id').encode('utf8')
                val = clearItemNum(id)
                if val == True:
                    return jsonify({'status':0})
                else:
                    return jsonify({'status':-1})

        except:
            traceback.print_exc()
            traceback.print_exc(file=app.config['error_log_file'])
            return jsonify({'status': -1})



if __name__ == '__main__':
    app.run(host='0.0.0.0')

# register(user_name='123',pwd='123')
# addCard(user_id='1',pwd='123',card_id='123456',user_phone='12345678901')
# addNewItem(id='1111',name='茶泡饭', num='100',price='12.32')
# addItemNum(id='1111', num='11')
# checkLogin(user_name='123', pwd='123')
# addNewItem(id='2222',name='可口可乐', num='120', price='3.5')
# addNewOrder(user_id='1',pwd='123', totalPrice='19.32',itemList=[{'id':'1111','num':1},{'id':'2222','num':2}])
