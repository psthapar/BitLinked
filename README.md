# BitLinked
A simple full stack application that uses Blockchain to create a person profile.

# Features
This app allows to:
1) Create a new blockchain based profile of a person
2) Add new block(s) to the blockchain
3) Search one or profiles based on any keyword mentioned in any block
4) Retrieve full blockchain using an user-friendly id

# Tech Stack
This application uses:
1) BigChainDB to store and retrieve Blockchains (www.bigchaindb.com). BigchainDB allows developers and enterprise to deploy blockchain proof-of-concepts, platforms and applications with a blockchain database, supporting a wide range of industries and use cases.

2) My-SQL to persist user-friendly userIds of a person and encrypted key/handle to the person's profile in BigChainDB

3) QuickSearch (https://github.com/karliszigurs/QuickSearch) for in-memory searching & indexing

4) Java for middleware & REST APIs

5) JSP/ CSS/ HTML for front end
