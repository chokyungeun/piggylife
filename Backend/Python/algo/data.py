import pandas as pd
import pymysql

from sqlalchemy import create_engine


def getStore():
    print("########## select Store ##########")
    conn = pymysql.connect(host='52.78.169.231', user='piggy', password='piggy', db='piggy')
    sql = "select * from store"
    stores = pd.read_sql_query(sql, conn)
    conn.close()

    return stores

def getPost():
    print("########## select Post ##########")
    conn = pymysql.connect(host='52.78.169.231', user='piggy', password='piggy', db='piggy')
    sql = "select u_id, s_id, is_like from post"
    posts = pd.read_sql_query(sql, conn)
    conn.close()

    return posts

def getPredict():
    print("########## select Predict ##########")
    conn = pymysql.connect(host='52.78.169.231', user='piggy', password='piggy', db='piggy')
    sql = "select * from predict"
    posts = pd.read_sql_query(sql, conn, index_col='u_id')
    conn.close()

    return posts

def save_recommendation(df):
    print("########## insert recommendation ##########")
    engine = create_engine("mysql+pymysql://piggy:" + "piggy" + "@52.78.169.231:3306/piggy?charset=utf8", encoding='utf-8')
    conn = engine.connect()
    df.to_sql(name="user_recommend", con=engine, if_exists='replace', index=False)
    conn.close()

def save_area(df):
    print("########## insert area ##########")
    engine = create_engine("mysql+pymysql://piggy:" + "piggy" + "@52.78.169.231:3306/piggy?charset=utf8", encoding='utf-8')
    conn = engine.connect()
    df.to_sql(name="area_recommend", con=engine, if_exists='replace', index=False)
    conn.close()

def save_matching(df):
    print("########## insert matching ##########")
    engine = create_engine("mysql+pymysql://piggy:" + "piggy" + "@52.78.169.231:3306/piggy?charset=utf8", encoding='utf-8')
    conn = engine.connect()
    df.to_sql(name="matching", con=engine, if_exists='replace', index=False)
    conn.close()

def save_predict(df):
    print("########## insert predict ##########")
    engine = create_engine("mysql+pymysql://piggy:" + "piggy" + "@52.78.169.231:3306/piggy?charset=utf8", encoding='utf-8')
    conn = engine.connect()
    df.to_sql(name="predict", con=engine, if_exists='replace')
    conn.close()