# Banking System
A java client app that is used to manage banks transactions with 3 types of servers.

## Description

### Client Side

The project is a Java app that acts as a system for bank account. Each bank customer has an account that is 
associated with basic information, like account number, password and balance. Customers have the following
operations on their accounts:

1. Login (account number, password): service will be denied by the server if these information are not
validated at the beginning of the connection.

2. Deposit (account number, amount): The Java client should send the amount to the server. The server
updates the balance of the account by increasing the amount . The client then received the new balance
as an HTTP response and display it.

3. Withdraw (account number, amount): The Java client should send the amount to the server. The server
updates the balance of the account by decreasing the amount . The client then received the new
balance as an HTTP response and display it.

4. Query (account number, #operations): The client asks the server for the last number of operations
performed on the account (up to last 5 operations). The client receives all transaction information
from the server and display it in a table.

5. History (account number): The client asks the server for all changes in balance. Then it displays the
information as a chart on the client side.

### Server Side

I have implemented three servers. PHP, Servlet and JSP server to serve the same client app with MySQL database.

