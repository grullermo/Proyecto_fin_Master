const express = require('express');
const mysql = require('mysql');

const bodyParser = require('body-parser');

const PORT = process.env.PORT || 3308

const app = express();

app.use(bodyParser.json());

var connection = mysql.createConnection({  
    host:'localhost',
    port:'3307',
    user: 'xxxxxx',
    password: 'xxxxxxx',
    database: 'myProyectoAgro' 
});

//Route
app.get('/', (req, res)=>{
res.send('Welcome to my api')
});

//all plantas
app.get('/plantas', (req, res) =>{
const sql = 'select * from planta';
connection.query(sql, (error, results) => {
    if (error) throw error;
    if (results.length > 0){
        res.json(results);
    }else {
        res.send('sin resultado');
    }

});


});

//all plantas
app.get('/plantas/:cod_planta', (req, res) =>{

    const {cod_planta}= req.params
    const sql = `select cod_planta,nombre from planta where cod_planta = ${cod_planta}`;
    connection.query(sql, (error, result) => {
        if (error) throw error;
        if (result.length > 0){
         res.json(result);
        }else {
           res.send('sin resultado');
     }

});
});

//all plantas
app.post('/addfrutos', (req, res) =>{
    const sql = 'insert into fruto set ?';
    const customerObj = { 
    cod_fruto: req.body.cod_fruto, 
    cod_planta: req.body.cod_planta,
    descripcion: req.body.descripcion,
    fecha_recoleccion: req.body.fecha_recoleccion, 
    peso: req.body.peso  
    } 

    connection.query(sql, customerObj, error =>{
        if (error) throw error;
        
        });
    var respuesta = {
         error: false,
         codigo: 200,
         mensaje: 'Fruto creado',
         data: customerObj 
     }
    res.send(respuesta);  

    }); 
            

//all plantas
app.put('/updateplantas/:id', (req, res) =>{
res.send("actualizar plantas")
});

app.delete('/delete/:id', (req, res) =>{
    res.send('borrar planta')
} )

//check coneect
connection.connect(error => {
    if (error) throw error;
    console.log('Database server running!');
});

app.listen(PORT, () => console.log(`Server running on port ${PORT} `));
