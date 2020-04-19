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

    // Ecoute du serveur
    var server =
        app.listen(port, function()
        {
            console.log("Serveur en écoute sur le port " + port);

            console.log("http://"+ip.address()+":"+port+"/streamAudio");
        });


    // Permet de régler la taille des données pouvant être récupéré en http
    app.use(bodyParser.urlencoded({limit:'50mb', extended:true}));
    app.use(bodyParser.json({limit:'50mb'}));

    // Requête post de ping
    app.get('/ping', (req, resp) =>
    {
      console.log("Réponse à un ping");
      resp.send("pingBack");
    });

    app.get('/streamAudio/:titre', function (req, resp)
    {
        console.log("Titre = " + req.params.titre);

        var titre = req.params.titre;
        var music = path.join(__dirname, 'musique', req.params.titre +'.mp3');

        mediaserver.pipe(req, resp, music);
    });

    app.get('/streamVideo', function (req, resp)
    {
        console.log("Vidéo envoyée");
        var video = path.join(__dirname, 'videos', 'libre.mp4');

        mediaserver.pipe(req, resp, video);
    });
