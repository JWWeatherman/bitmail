# Vue-Play-Seed
---

This app is a basic trivia game. All answers are verified from within the server to ensure users are not able to cheat.

App uses VueJs on the front end for the view.

The Play! server contains the model. App is using Reactivemongo for data persistence, making this a good seed for handling JWT's.

Build for WebPack is handled via Play! hook and sbt.

Prerequisites
  - NodeJS v- 7.10.1
    - Use [N](https://github.com/tj/n)
  - WebPack installed globally 
    - ```npm install webpack -g``` 
  - MongoDB install globally
    - Goto [MongoDb](https://docs.mongodb.com/manual/installation/)
  
Install Dependencies
```
 # node modules
 npm install
``` 

Run app 
```
# sbt 
sbt ~run // for live reload front and back
sbt run // otherwise

# test(none yet, but are coming)
sbt test
```

 