# Order-processor

Logic:

- Read each line and create an order object
- First buy/sell transaction of a company is always CLOSED.
- Further transactions of the company is tallied based on availability.
- If we encounter a buy txn, check if there is enough quantity of stocks of company being previously sold.
- If we encounter a sell txn, check if there is enough quantity of stocks of company being previously bought.

eg.
1,Buy,ABC,10 -> CLOSED as it is first txn of company stock
2,Sell,XYZ,15 -> CLOSED as it is first txn of company stock
3,Sell,ABC,13 -> OPEN because we cannot fully match sell quanity with bought quantity and 13 > 10
4,Buy,XYZ,10 -> CLOSED because we can fully match buy quanity with sold quantity and 10 < 15. Available is 5 now
5,Buy,XYZ,8 -> OPEN because we cannot fully match buy quanity with sold quantity and 8 < 5

Assumptions made:

- First buy/sell transaction of a company is always CLOSED.
- If we don't have enough quantity to buy or sell we dont execute partial transaction. ie. if we execute 10 units for sell but we have only 5 units. We dont partially sell 5, but keep txn as open.

