const express = require('express');
const app = express();
const cookieParser = require('cookie-parser')
const bodyParser = require('body-parser')
app.use(express.json())
app.use(express.urlencoded({extended: true}))
app.use(cookieParser());
app.use(bodyParser());
    
app.use('/', express.static("./public"));


var server = app.listen(2000, function () {
    var port = server.address().port;
    console.log("App now running on port", port);
  });

