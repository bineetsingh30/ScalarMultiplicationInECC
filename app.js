let express = require('express');
let app = express();
let body_parser = require('body-parser');

app.use(body_parser.urlencoded({extended:true}))

app.use('/', express.static("./public"))

function validateInputs(p,a,b,k,x,y){
    let eq1  = (x*x*x + a*x + b*1)%p;
    let eq2 = 4*a*a*a + 27*b*b;
    let eq3 = (y*y)%p;
    // console.log(eq1+"   "+eq2+" "+eq3)
    if(eq2){
        // console.log("1")
        if(eq3==eq1){
            // console.log("1")
            if(x<p && y<p){
                // console.log("1")
                return 1;
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }else{
        return 0;
    }
}

app.post('/api/getdata',(req,res)=>{
    let p = req.body.p;
    let a = req.body.a;
    let b = req.body.b;
    let k = req.body.k;
    let x = req.body.px;
    let y = req.body.py;
    let v = validateInputs(p,a,b,k,x,y);
    // console.log(v)
    if(v){
        const cmd=require('node-cmd');
        cmd.get(`java -cp "${__dirname}/bin/" ECC ${a} ${b} ${p} ${k} ${x} ${y}`, (err, data, strderr) => {
            if (err) {
                console.log('error', err)
            } else {
                // console.log("Result => " + data)
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
    }
})




app.listen(3000);
