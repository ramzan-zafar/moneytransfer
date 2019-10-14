## Money Transfer API
Java API for money transfer between accounts

### How to Run

mvn exec:java

### Technologies

* Spark Java with embeded container
* Dagger 2 for dependency injection 
* Mockito (for mocking database calls in unit testing)

injection framework from google like Guice)
* H2 for in memory database


Application starts a jetty server on localhost port 
8088.In memory H2 database with predefined data you can find in demo.sql

Few example calls are given below.
* localhost:8088/user/1
* localhost:8088/account/1

#### Money Transfer "POST" request

localhost:8088/transfermoney?fromAccountId=1&toAccountId=2&amountToTransfer=20&currency=USD

### Testing
TDD and BDD practice has been followed in developing the API in following
areas
* Unit Testing
* Integration Testing
* Functional Testing 

### API Usage

HTTP METHOD | PATH | USAGE
--- | --- | ---
GET| /users | Get all users
GET| /user/:id | Get user by its id
POST| /user/:id | Create a new user
PUT| /user/:id | Update an existing user
DELETE| /user/:id | Delete an exisiting user
GET| /accounts | Get all accounts
GET| /account/:id | Get account details by id
GET| /account/:id/balance | Get account balance by accountId
PUT| /account/:id | Create a new account
DELETE| /account/:id | Remove account by accountId
PUT| /account/:id/withdraw/:amount | Withdraw money from account
PUT| /account/:id/deposit/:amount | Deposit money from account
POST| /transfermoney | Perform transaction between 2 user accounts

#### Sample JSON for User and Account

##### User
        {
           "id": "1",
           "firstName": "Jane",
           "lastName": "doe",
           "email": "jane@gmail.com"
        }
        
        {
           "id": "2",
           "firstName": "jhon",
           "lastName": "doe",
           "email": "jhon@gmail.com"
        }
       

##### Account
        {
            "accountID": "1",
            "userId": "1",
            "balance": 100,
            "currency": "USD"
        }
        
        {
            "accountID": "2",
            "userId": "2",
            "balance": 200,
            "currency": "USD"
        }
        
        

### Https Status
* 200 OK: The request has succeeded
* 400 Bad Request: The request could not be understood by the server
* 404 Not Found: The requested resource cannot be found
* 500 Internal Server Error: The server encountered an unexpected condition 

