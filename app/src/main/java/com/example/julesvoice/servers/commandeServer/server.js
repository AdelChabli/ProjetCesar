    // --------------------------- Déclaration de variables  ------------------------

    const port = 8085;
    const express = require('express');
    const app = express();
    const bodyParser = require('body-parser');
    var responseData = {};
    var ip = require("ip");
    const fs = require('fs');
    var path = require('path');
    var mediaserver = require('mediaserver');

    var server =
        app.listen(port, function()
        {
            console.log("Serveur en écoute sur le port " + port);

            console.log("http://"+ip.address()+":"+port+"");
        });

    app.use(bodyParser.urlencoded({limit:'50mb', extended:true}));
    app.use(bodyParser.json({limit:'50mb'}));

    // Requête post de ping
    app.post('/ping', (req, resp) =>
    {
      console.log("Réponse à un ping");
      resp.send("pingBack");
    });

    app.get('/getAction', function (req, resp)
    {

    });