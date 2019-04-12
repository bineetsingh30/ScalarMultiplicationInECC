let express = require('express');
let app = express();
let body_parser = require('body-parser');

app.use(body_parser.urlencoded({extended:true}))

app.use('/', express.static("./public"))

app.post('/api/getdata',(req,res)=>{
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
            let attributes = {}
            attributes["time"] = [];
            attributes["adds"] = [];
            attributes["doubles"] = [];
            attributes["precomputations"] = [];
            attributes["hammingwt"] = [];
            
            for(let i = 0; i < result.length - 1; i+=5){
                attributes["time"].push(parseInt(result[i]))
                attributes["adds"].push(parseInt(result[i+1]))
                attributes["doubles"].push(parseInt(result[i+2]))
                attributes["precomputations"].push(parseInt(result[i+3]))
                attributes["hammingwt"].push(parseInt(result[i+4]))
            }

            res.send(attributes)
        }

        
    });


})




app.listen(3000);
