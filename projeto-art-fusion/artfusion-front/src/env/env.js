
const envProd = require('./env-prod.json')
const envDev = require('./env-dev.json')
let env;

if(process.env['NODE_ENV']==="production"){
        env = envProd
}else{
    env = envDev
}

export  default  env;