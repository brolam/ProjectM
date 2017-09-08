'use strict';

// [START all]
// [START import]
// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');
const express = require('express');
const env = require('./env.js');

// The Firebase Admin SDK to access the Firebase Realtime Database. 
const admin = require('firebase-admin');
const app = express();
admin.initializeApp(functions.config().firebase);
app.set('view engine', 'ejs');
app.use('/', express.static('../public'));


app.get('/', (request, response) =>{
	response.render('index');
});

app.get('/job/new', (request, response) =>{
	response.render('newJob');
});

app.get('/job/applicationLink', (request, response) =>{
	response.render('applicationLink');
});

exports.sendEmailJobApplicationLink = functions.database.ref('/jobApplicationsLink/{key}')
    .onCreate(event => {
    let jobAppLink  = event.data.val()
    const mailjet = require ('node-mailjet')
  	.connect(process.env.MJ_APIKEY_PUBLIC, process.env.MJ_APIKEY_PRIVATE)
  	const requestEmail = mailjet
		  .post("send", {'version': 'v3.1'})
	  	.request({
	  	  "Messages":[
	   	     {
	   	         "From": {
	   	             "Email": "breno@brolam.com.br",
	                 "Name": "Breno Marques"
	   	         },
	   	         "To": [
	   	             {
	   	                 "Email": jobAppLink.userEmail,
	   	                 "Name": jobAppLink.userName
	   	             }
	   	         ],
	   	         "Subject": jobAppLink.jobTitle,
	   	         "TextPart": `Olá ${jobAppLink.userName} Acesse o endereço https://projectm-2340b.firebaseapp.com/job/applicationLink?key=${event.params.key} ${jobAppLink.jobTitle} para finalizar a sua candidatura.`,
	   	         "HTMLPart": `<h1>Olá ${jobAppLink.userName}</h1><p>Acesse o endereço <a href="https://projectm-2340b.firebaseapp.com/job/applicationLink?key=${event.params.key}">${jobAppLink.jobTitle}</a> para finalizar a sua candidatura.`,
	   	     }
	   	 ]
	  	})
	requestEmail
	  .then((result) => {
	    console.log(result.body)
	  })
	  .catch((err) => {
	    console.log(err.statusCode)
	  })

});

exports.app = functions.https.onRequest(app);

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
//exports.helloWorld = functions.https.onRequest((request, response) => {
/**
* This call sends an email to one recipient, using a validated sender address
* Do not forget to update the sender address used in the sample
*/

/*
	const mailjet = require ('node-mailjet')
  	.connect(process.env.MJ_APIKEY_PUBLIC, process.env.MJ_APIKEY_PRIVATE)
  	const requestEmail = mailjet
		  .post("send", {'version': 'v3.1'})
	  	.request({
	  	  "Messages":[
	   	     {
	   	         "From": {
	   	             "Email": "breno@brolam.com.br",
	                 "Name": "Breno Marques"
	   	         },
	   	         "To": [
	   	             {
	   	                 "Email": "brenomar@gmail.com",
	   	                 "Name": "Breno Marques"
	   	             }
	   	         ],
	   	         "Subject": "Your email flight plan!",
	   	         "TextPart": "Dear passenger, welcome to Mailjet! May the delivery force be with you!",
	   	         "HTMLPart": "<h3>Dear passenger, welcome to Mailjet!</h3><br />May the delivery force be with you!",
	   	     }
	   	 ]
	  	})
	requestEmail
	  .then((result) => {
	    console.log(result.body)
	  })
	  .catch((err) => {
	    console.log(err.statusCode)
	  })
	 response.send("Firebase Eve(1):" + process.env.MJ_APIKEY_PUBLIC + "Firebase Eve(2)" + process.env.MJ_APIKEY_PRIVATE);
});

exports.sendEmailJobApplicationLink = functions.database.ref('/jobApplicationsLink/{id}')
    .onCreate(event => {
    console.log(event.data.val());
    const mailjet = require ('node-mailjet')
  	.connect(process.env.MJ_APIKEY_PUBLIC, process.env.MJ_APIKEY_PRIVATE)
  	const requestEmail = mailjet
		  .post("send", {'version': 'v3.1'})
	  	.request({
	  	  "Messages":[
	   	     {
	   	         "From": {
	   	             "Email": "breno@brolam.com.br",
	                 "Name": "Breno Marques"
	   	         },
	   	         "To": [
	   	             {
	   	                 "Email": "brenomar@gmail.com",
	   	                 "Name": "Breno Marques"
	   	             }
	   	         ],
	   	         "Subject": "Your email flight plan!",
	   	         "TextPart": event.data.getKey() + " = " + event.data.val(),
	   	         "HTMLPart": "<h3>" + event.data.getKey() + "</h3><br />" + event.data.val(),
	   	     }
	   	 ]
	  	})
	requestEmail
	  .then((result) => {
	    console.log(result.body)
	  })
	  .catch((err) => {
	    console.log(err.statusCode)
	  })

});
*/


