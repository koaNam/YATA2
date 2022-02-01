from confluent_kafka import Producer
import pandas as pd
import re


def acked(err, msg):
    if err is not None:
        print("Failed to deliver message: %s: %s" % (str(msg), str(err)))
    else:
        print("Message produced: %s" % (str(msg)))


def connect_kafka(host):
    conf = {'bootstrap.servers': host}
    return Producer(conf)


def remove_whitespace(string):
    return re.sub("\\s\\s+", " ", string)


if __name__ == '__main__':
    producer = connect_kafka("localhost:9092")

    for chunk in pd.read_csv("./data/articles1.csv", chunksize=1000,  encoding="UTF-8"):
        chunk = chunk.loc[:, ~chunk.columns.str.contains("Unnamed")].drop(columns=["url", "year", "month"])

        for doc in chunk.iterrows():
            document = doc[1]
            data = remove_whitespace(document.to_json())
            producer.produce("connect-test", key=document.date, value=data, callback=acked)


    producer.flush()
