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
    let x = req.body.px;
    let y = req.body.py;

    const cmd=require('node-cmd');
    cmd.get(`java -cp "/home/bineet/Downloads/Major/Scalar Multiplication/bin/" ECC ${a} ${b} ${p} ${k} ${x} ${y}`, (err, data, strderr) => {
        if (err) {
            console.log('error', err)
         } else {
            console.log("Result => " + data)
            let result = data.split(" ")
            console.log(result)

            for(r of result){
                
            }
            
        }
    });


})




app.listen(3000);
