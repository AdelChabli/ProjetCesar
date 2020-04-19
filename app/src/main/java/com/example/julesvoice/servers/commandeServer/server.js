    // --------------------------- Déclaration de variables  ------------------------

    const port = 3013;
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
    app.get('/ping', (req, resp) =>
    {
      console.log("Réponse à un ping");
      resp.send("pingBack");
    });

    app.post('/getSpeech', function (req, resp)
    {
        var speechtxt = req.body.speech;

        console.log(speechtxt);

        if(speechtxt.includes('joue') || speechtxt.includes('lance') || speechtxt.includes('écoute'))
        {
            responseData.action = "musique";

            console.log("Action = lancer une musique");

            if(speechtxt.includes("musique"))
            {
                console.log("titre = " + speechtxt.substring(speechtxt.lastIndexOf("musique") + 8, speechtxt.length));
                responseData.commande = speechtxt.substring(speechtxt.lastIndexOf("musique") + 8, speechtxt.length);
            }
            else if(speechtxt.includes("écouter"))
            {
                console.log("titre = " + speechtxt.substring(speechtxt.lastIndexOf("écouter") + 8, speechtxt.length));
                responseData.commande = speechtxt.substring(speechtxt.lastIndexOf("écouter") + 8, speechtxt.length);
            }
        }

        resp.json(responseData);
    });