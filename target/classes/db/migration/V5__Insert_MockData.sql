-- Info
-- GOODS_ID: 1, 2, 3, 4, 5
-- SHOP_ID: 11, 22
--

-- Mock Goods Data
INSERT INTO GOODS (ID, SHOP_ID, NAME, DESCRIPTION, DETAILS, IMG_URL, PRICE, STOCK, STATUS)
VALUES ('1', '11', 'soap', 'good soap', 'good coconut soap', 'http://goods/1/url', 500, 10, 'ok');

INSERT INTO GOODS (ID, SHOP_ID, NAME, DESCRIPTION, DETAILS, IMG_URL, PRICE, STOCK, STATUS)
VALUES ('2', '11', 'apple', 'good apple', 'good red apple', 'http://goods/2/url', 100, 200, 'ok');

INSERT INTO GOODS (ID, SHOP_ID, NAME, DESCRIPTION, DETAILS, IMG_URL, PRICE, STOCK, STATUS)
VALUES ('3', '11', 'chair', 'good chair', 'good steel chair', 'http://goods/3/url', 1000, 5, 'ok');

INSERT INTO GOODS (ID, SHOP_ID, NAME, DESCRIPTION, DETAILS, IMG_URL, PRICE, STOCK, STATUS)
VALUES ('4', '22', 'bread', 'good bread', 'good oat bread', 'http://goods/4/url', 400, 8, 'ok');

INSERT INTO GOODS (ID, SHOP_ID, NAME, DESCRIPTION, DETAILS, IMG_URL, PRICE, STOCK, STATUS)
VALUES ('5', '22', 'cake', 'good cake', 'good cheese cake', 'http://goods/5/url', 100, 10, 'ok');

-- Mock Shop Data
INSERT INTO SHOP (ID, NAME, DESCRIPTION, IMG_URL, OWNER_USER_ID, STATUS)
VALUES ('11', 'shop 1', 'shop 1 selling goods', 'http://shop/1/url', 111, 'ok');

INSERT INTO SHOP (ID, NAME, DESCRIPTION, IMG_URL, OWNER_USER_ID, STATUS)
VALUES ('22', 'shop 2', 'shop 2 selling goods', 'http://shop/2/url', 222, 'ok');

-- Mock Shopping Cart Data
-- user1
INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123456', '1', '11', '1', '5', 'ok');

INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123457', '1', '11', '2', '10', 'ok');

INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123458', '1', '11', '3', '3', 'ok');

INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123459', '1', '22', '4', '6', 'ok');

INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123460', '1', '22', '5', '7', 'ok');

-- user2
INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123470', '2', '11', '2', '8', 'ok');

INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123471', '2', '22', '4', '11', 'ok');

INSERT INTO SHOPPING_CART (ID, USER_ID, SHOP_ID, GOODS_ID, NUMBER, STATUS)
VALUES ('123472', '2', '22', '5', '9', 'ok');

