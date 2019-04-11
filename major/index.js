let express = require('express');
let app = express();
let body_parser = require('body-parser');

app.set('view engine', 'ejs');
app.use(body_parser.urlencoded({extended:true}))

app.get('/home',(req,res)=>{
    res.render('home.ejs')
})

app.post('/api/call_function',(req,res)=>{
    let p = req.body.p;
    let a = req.body.a;
    let b = req.body.b;
    let k = req.body.k;
    let px = req.body.px;
    let py = req.body.py;
    //myfunction(p,q,n);

})

app.listen(3000);