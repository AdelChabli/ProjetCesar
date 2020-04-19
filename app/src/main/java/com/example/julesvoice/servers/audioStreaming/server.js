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

    app.get('/streamAudio/:titre/:type', function (req, resp)
    {
        console.log("Titre = " + req.params.titre);
        console.log("Type = " + req.params.type);

        var titre = req.params.titre;
        var type = req.params.type;

        titre = titre.split("_").join(" ");

        var file = "";

        if(type == "musique")
        {
            file = path.join(__dirname, 'musique', titre +'.mp3');
        }
        else
        {
            file = path.join(__dirname, 'video', titre +'.mp4');
        }

        mediaserver.pipe(req, resp, file);
    });

    app.get('/streamVideo', function (req, resp)
    {
        console.log("Vidéo envoyée");
        var video = path.join(__dirname, 'videos', 'libre.mp4');

        mediaserver.pipe(req, resp, video);
    });

    app.get('/fileExist/:titre', function(req, resp)
    {
        var titre = req.params.titre;

        titre = titre.split("_").join(" ");

        var pathMusique = './musique/'+titre+'.mp3';
        var pathVideo = './video/'+titre+'.mp4';

        if (fs.existsSync(pathMusique)) {
            return resp.send("musique");
        }

        if (fs.existsSync(pathVideo)) {
            return resp.send("video");
        }

        console.log("Fichier " + titre + " non trouvé");
        return resp.send("no");
    });

    app.get('/getAllMusic', function(req, resp)
    {
        var musicPath = path.join(__dirname, 'musique');
        var theFiles = "";

        fs.readdir(musicPath, function (err, files)
        {
            files.forEach(function (file) {
                console.log(file);
                file = file.split(".mp3").join("");
                theFiles = theFiles + file + "_";
            });

            theFiles = theFiles.substring(0, theFiles.length - 1);

            resp.send(theFiles);
        });
    });

    app.get('/getAllVideo', function(req, resp)
        {
            var musicPath = path.join(__dirname, 'video');
            var theFiles = "";

            fs.readdir(musicPath, function (err, files)
            {
                files.forEach(function (file) {
                    console.log(file);
                    file = file.split(".mp4").join("");
                    theFiles = theFiles + file + "_";
                });

                theFiles = theFiles.substring(0, theFiles.length - 1);

                resp.send(theFiles);
            });
        });


    function fileExist(filePath) {
     return new Promise((resolve, reject) => {
       fs.access(filePath, fs.F_OK, (err) => {
         if (err) {
           return false;
        }
         return true;
       })
     });
    }
