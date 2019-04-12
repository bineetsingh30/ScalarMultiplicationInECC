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
            // console.log(result)

            //create arrays for each attribute
            let time = [];
            let adds = [];
            let doubles = [];
            let precomputations = [];
            let hammingwt = [];
            
            for(let i = 0; i < result.length - 1; i+=5){
                time.push(result[i])
                adds.push(result[i+1])
                doubles.push(result[i+2])
                precomputations.push(result[i+3])
                hammingwt.push(result[i+4])
            }

            console.log(time)
            console.log(adds)
            console.log(doubles)
            console.log(precomputations)
            console.log(hammingwt)            
        }

        //call google api for graph and create a graphs page and return that page
        
    });


})




app.listen(3000);
