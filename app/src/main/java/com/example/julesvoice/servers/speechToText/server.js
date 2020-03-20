// --------------------------- Déclaration de variables  ------------------------

const port = 3012;
const express = require('express');
const app = express();
const bodyParser = require('body-parser');
var responseData = {};
var ip = require("ip");
const fs = require('fs');
var answerText = "";

// Ecoute du serveur
var server =
    app.listen(port, function()
    {
        console.log("Serveur en écoute sur le port " + port);

        console.log("http://"+ip.address()+":"+port);
    });


// Permet de régler la taille des données pouvant être récupéré en http
app.use(bodyParser.urlencoded({limit:'50mb', extended:true}));
app.use(bodyParser.json({limit:'50mb'}));

// Requête post de ping

app.get('/', (req, resp) =>
{
  console.log("Connexion au serveur");
  resp.send("Bienvenu sur le serveur speech to text");
});

app.get('/ping', (req, resp) =>
{
  console.log("Réponse à un ping");
  resp.send("pingBack");
});

// Requête speechToText faisant appeller à l'api Google speech
app.post('/speechToText', (req, resp) =>
{

  // Pour pouvoir utiliser l'api google il faut import mon fichier key générer sur la plateforme google avant de run le serveur
  // export GOOGLE_APPLICATION_CREDENTIALS="/home/adel/StudioProjects/ProjetCesar/app/src/main/java/com/example/julesvoice/servers/speechToText/ArchitectureApplicative-ee1e353fc90a.json"

  // Récupère le fichier wav base 64
  var base64File = req.body.wav.replace('data:audio/wav; codecs=opus;base64,', '');

  // Enregistre le fichier sur le serveur et fait appelle à la fonction speechToText
  fs.writeFile("test.wav", base64File, 'base64', function(err)
  {
    speechToText(function(speechToText) {
      resp.send(answerText);
    })
  });
});


// Fonction d'appelle à l'api Google speech
// https://github.com/googleapis/nodejs-speech/blob/master/samples/recognize.js
function speechToText(callback)
{
  async function main() {
    console.log("Speech en cours");

    // Appelle à la librairie google speech
    const speech = require('@google-cloud/speech');
    const fs = require('fs');

    // On créer un client
    const client = new speech.SpeechClient();

    // le nom du fichier wav
    const fileName = 'test.wav';

    // transforme le fichier enregistrer en base 64
    const file = fs.readFileSync(fileName);
    const audioBytes = file.toString('base64');

    // Encodage du fichier audio
    const audio = {
      content: audioBytes,
    };

    // Json configuration avec la possibilité de faire du français avec un peu d'anglais en alternative
    const config = {
      enableAutomaticPunctuation: false,
      encoding: 'LINEAR16',
      languageCode: 'fr-FR',
      sampleRateHertz: 16000,
      model: 'default',
      alternativeLanguageCodes: ['en-NG', 'fr-FR'],
    };
    const request = {
      audio: audio,
      config: config,
    };

    // Appelle à l'api google et attente de la recognition
    const [response] = await client.recognize(request);
    const transcription = response.results
      .map(result => result.alternatives[0].transcript)
      .join('\n');
    console.log(`Transcription: ${transcription}`);

    // Récupération de la transcription dans une variable globale utiliser lors de la response
    answerText = transcription;
    callback(speechToText);
  }
  main().catch(console.error);
}
